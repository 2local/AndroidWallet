package com.android.l2l.twolocal.dataSourse.repository.crypto.bsc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.android.l2l.twolocal.R;
import com.android.l2l.twolocal.coin.CoinHelper;
import com.android.l2l.twolocal.dataSourse.local.db.AppDatabase;
import com.android.l2l.twolocal.dataSourse.local.prefs.UserSession;
import com.android.l2l.twolocal.dataSourse.remote.api.ApiConstants;
import com.android.l2l.twolocal.dataSourse.remote.currency.CryptoCurrencyRemoteDataSourceHelper;
import com.android.l2l.twolocal.dataSourse.repository.crypto.CryptoCurrencyRepositoryHelper;
import com.android.l2l.twolocal.model.TransactionGas;
import com.android.l2l.twolocal.model.Wallet;
import com.android.l2l.twolocal.model.WalletBalance;
import com.android.l2l.twolocal.model.WalletTransactionHistory;
import com.android.l2l.twolocal.model.enums.CryptoCurrencyType;
import com.android.l2l.twolocal.model.enums.FiatType;
import com.android.l2l.twolocal.model.mapper.Mapper_TransactionReceipt_To_WalletTransactionHistory;
import com.android.l2l.twolocal.model.CoinExchangeRate;
import com.android.l2l.twolocal.sdk.binancesmartchainsdk.util.BalanceUtils;
import com.android.l2l.twolocal.sdk.binancesmartchainsdk.util.CryptoWalletUtils;
import com.android.l2l.twolocal.sdk.binancesmartchainsdk.util.Erc20TokenWrapper;
import com.android.l2l.twolocal.utils.SecurityUtils;
import com.android.l2l.twolocal.utils.WalletFactory;

import org.bouncycastle.util.encoders.Hex;
import org.jetbrains.annotations.NotNull;
import org.web3j.abi.Utils;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Bip39Wallet;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.MnemonicUtils;
import org.web3j.crypto.WalletUtils;
import org.web3j.exceptions.MessageDecodingException;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import static com.android.l2l.twolocal.utils.constants.AppConstants.TRANSACTION_DROPPED;
import static com.android.l2l.twolocal.utils.constants.AppConstants.TRANSACTION_SEEN;

public class BSCRepository implements CryptoCurrencyRepositoryHelper {

    private final Context context;
    private final AppDatabase database;
    private final UserSession userSession;
    private final CryptoCurrencyRemoteDataSourceHelper etherApiInterface;
    private final Web3j web3j;
    private final CryptoCurrencyType currencyType;
    private final BigInteger BSC_GAS_LIMIT = new BigInteger("61000");

    @Inject
    public BSCRepository(Context context,
                         @Named("BSC") Web3j web3j,
                         CryptoCurrencyRemoteDataSourceHelper etherApiInterface,
                         AppDatabase database,
                         UserSession userSession,
                         CryptoCurrencyType currencyType) {
        this.context = context;
        this.web3j = web3j;
        this.database = database;
        this.userSession = userSession;
        this.etherApiInterface = etherApiInterface;
        this.currencyType = currencyType;
    }

    private String getContractAddress() {
        return CoinHelper.INSTANCE.getContractAddress(currencyType);
    }

    @NotNull
    @Override
    public Single<Boolean> createEtherWallet() {
        return Single.create(emitter -> {
            String password = "";
            try {
                Bip39Wallet bip39Wallet = WalletUtils.generateBip39Wallet(password, CryptoWalletUtils.getWalletDestinationDirectory(context));
                String privateKey = CryptoWalletUtils.getPrivateKeyFromMnemonic(bip39Wallet.getMnemonic());
                Credentials credentials = Credentials.create(privateKey);
                storeWallet(bip39Wallet.getMnemonic(), privateKey, bip39Wallet.getFilename(), credentials.getAddress());
                emitter.onSuccess(true);

            } catch (IOException | CipherException e) {
                e.printStackTrace();
                emitter.onError(e);
            }
        });
    }


    @SuppressLint("CheckResult")
    @NotNull
    @Override
    public Single<WalletBalance> getWalletBalance() {

        return Single.fromCallable(() -> {
            Credentials credentials = CryptoWalletUtils.getWalletCredential(context, database, currencyType);

//            TransactionReceiptProcessor transactionReceiptProcessor = new NoOpProcessor(web3j);
            TransactionManager transactionManager = new RawTransactionManager(web3j, credentials, getChainID());
            Erc20TokenWrapper contract = Erc20TokenWrapper.load(getContractAddress(), web3j, transactionManager, BigInteger.ZERO, BigInteger.ZERO);
            Address address = new Address(credentials.getAddress());
            BigInteger tokenBalance = contract.balanceOf(address).getValue();
            String tokenName = contract.name().getValue();
            String tokenSymbol = contract.symbol().getValue();
            BigInteger decimalCount = contract.decimals().getValue();

            BigDecimal tokenValueByDecimals = BalanceUtils.balanceByDecimal(tokenBalance, decimalCount);
            Log.v("getWalletBalance", tokenValueByDecimals.toString() + " " + tokenName + " " + tokenSymbol);
            return new WalletBalance(tokenValueByDecimals.toString(), currencyType.getMyName());
        });
    }


    @Override
    public boolean checkValidEtherAddress(String etherAddress) {
        return WalletUtils.isValidAddress(etherAddress);
    }

    //normal gas value
    private BigInteger getTransactionGasPrice(String gas) {
        BigInteger gasPrice = null;
        try {
            if (TextUtils.isEmpty(gas))
                gasPrice = web3j.ethGasPrice().send().getGasPrice();
            else
                gasPrice = Convert.toWei(String.valueOf(gas), Convert.Unit.ETHER).divide(new BigDecimal(BSC_GAS_LIMIT), MathContext.DECIMAL32).toBigInteger();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return gasPrice;
    }

    /**
     * check user has enough token balance
     *
     * @return
     */
    private boolean userHasEnoughTokenBalance(BigDecimal amount) {
        Wallet wallet = database.getWallet(currencyType);
        if (wallet != null) {
            BigDecimal walletAmount = new BigDecimal(wallet.getAmount());
            return amount.compareTo(walletAmount) <= 0;
        } else
            return false;
    }

    /**
     * check network coin balance for pay gas
     *
     * @param gas
     * @return
     */
    private boolean userHasEnoughNetworkCoinBalanceForGas(BigDecimal gas) {
//        Wallet wallet = database.getWallet(currencyType);
//        if (wallet != null) {
        BigDecimal bnbBalance = getNetworkCoinBalance();
        return gas.compareTo(bnbBalance) <= 0;
//        } else
//            return false;
    }

    /**
     * get network coin balance (bnb)
     *
     * @return
     */
    private BigDecimal getNetworkCoinBalance() {
        Credentials credentials = CryptoWalletUtils.getWalletCredential(context, database, currencyType);
        BigInteger valueInWei = new BigInteger("0");
        try {
            valueInWei = web3j.ethGetBalance(credentials.getAddress(), DefaultBlockParameterName.LATEST).send().getBalance();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Convert.fromWei(new BigDecimal(valueInWei), Convert.Unit.ETHER);
    }

    /**
     * check amount less than zero
     *
     * @return
     */
    private boolean checkAmountLessThanZero(BigDecimal tokenAmount) {
        return tokenAmount.compareTo(new BigDecimal("0")) == -1;
    }

    private String safeAddress(String toAddress) {
        if (!toAddress.startsWith("0x"))
            toAddress = "0x" + toAddress;
        return toAddress;
    }

    /**
     * @param
     * @param tokenAmount
     * @param gas         is in ether normal amount
     * @return
     */
    @NotNull
    @Override
    public Single<String> sendAmount(@NotNull String toAddress, @NotNull BigDecimal tokenAmount, @NotNull BigDecimal gas) {
        return Single.create(emitter -> {
            try {
                Credentials credentials = CryptoWalletUtils.getWalletCredential(context, database, currencyType);

                ////////////////// check wallet address //////////////////////////
                String finalDestinationAddress = safeAddress(toAddress);

                if (!checkValidEtherAddress(finalDestinationAddress)) {
                    emitter.onError(new Throwable(context.getString(R.string.repository_invalid_wallet_address)));
                    return;
                }

                //////////////////////////check wallet balance//////////////////////////////
                if (!userHasEnoughTokenBalance(tokenAmount)) {
                    emitter.onError(new Throwable(context.getString(R.string.repository_you_dont_have_enough_balance) + " " + tokenAmount + " " + currencyType.getSymbol()));
                    return;
                }


                if (!userHasEnoughNetworkCoinBalanceForGas(gas)) {
                    emitter.onError(new Throwable(context.getString(R.string.repository_you_dont_have_enough_balance) + " " + gas + " " + CryptoCurrencyType.BINANCE.getSymbol()));
                    return;
                }

                //////////////////////////check amount more than zero//////////////////////////////
                if (checkAmountLessThanZero(tokenAmount)) {
                    emitter.onError(new Throwable(context.getString(R.string.repository_send_amount_too_low)));
                    return;
                }

                // get gas price
                BigInteger gasWeiPrice = getTransactionGasPrice(gas.toString());


                TransactionManager transactionManager = new RawTransactionManager(web3j, credentials, getChainID());
                Erc20TokenWrapper contract = Erc20TokenWrapper.load(getContractAddress(), web3j, transactionManager, gasWeiPrice, BSC_GAS_LIMIT);

//                String tokenName = contract.name().getValue();
//                String tokenSymbol = contract.symbol().getValue();
                BigInteger decimalCount = contract.decimals().getValue();
                contract.requestCurrentGasPrice();

                BigDecimal formattedAmount = BalanceUtils.amountByDecimal(tokenAmount, new BigDecimal(decimalCount));

                TransactionReceipt mReceipt = contract.transfer(new Address(finalDestinationAddress), new Uint256(formattedAmount.toBigInteger()));

                if (!mReceipt.isStatusOK()) {
                    if (!emitter.isDisposed()) emitter.onError(new Exception("Transaction error!"));
                    return;
                }

                Log.e("TransactionReceipt", mReceipt.getTransactionHash());
                saveTransactionToDatabase(mReceipt, finalDestinationAddress, formattedAmount.toBigInteger());
                if (!emitter.isDisposed()) emitter.onSuccess(mReceipt.getTransactionHash());

            } catch (Exception e) {
                e.printStackTrace();
                if (!emitter.isDisposed()) emitter.onError(e);
            }
        });
    }

    private void saveTransactionToDatabase(TransactionReceipt mReceipt, String finalDestinationAddress, BigInteger amount) {
        WalletTransactionHistory history = new Mapper_TransactionReceipt_To_WalletTransactionHistory().mapperToWalletTransactionHistory(mReceipt, finalDestinationAddress, amount);
        history.setType(currencyType);
        database.saveWalletTransaction(history)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe();
    }

    @NotNull
    @Override
    public Single<Boolean> restoreWalletFromMnemonic(@NotNull String mnemonic) {
        return Single.create(emitter -> {
            if (!MnemonicUtils.validateMnemonic(mnemonic)) {
                emitter.onError(new Throwable(context.getString(R.string.repository_invalid_mnemonic_phrase)));
            } else {
                try {
                    String password = "";
                    String privateKey = CryptoWalletUtils.getPrivateKeyFromMnemonic(mnemonic);
                    Credentials credentials = Credentials.create(privateKey);
                    Bip39Wallet bip39Wallet = WalletUtils.generateBip39WalletFromMnemonic(password, mnemonic, CryptoWalletUtils.getWalletDestinationDirectory(context));
                    storeWallet(mnemonic, privateKey, bip39Wallet.getFilename(), credentials.getAddress());
                    Log.v("restoreWalletFromMnemon", credentials.getAddress());
                    emitter.onSuccess(true);
                } catch (Exception e) {
                    e.printStackTrace();
                    emitter.onError(new Throwable(context.getString(R.string.repository_error_occurred)));

                }
            }
        });
    }

    @SuppressLint("CheckResult")
    @NotNull
    @Override
    public Single<Boolean> restoreWalletFromPrivateKey(@NotNull String privateKey) {
        return Single.fromCallable(() -> {
            try {
                String password = "";

                ECKeyPair keys = getECKeyPair(privateKey);

                Credentials credentials = Credentials.create(keys);
                String walletFileName = WalletUtils.generateLightNewWalletFile(password, CryptoWalletUtils.getWalletDestinationDirectory(context));

                storeWallet("", privateKey, walletFileName, credentials.getAddress());
                storeBinanceWallet("", privateKey, walletFileName, credentials.getAddress());
                return true;
            } catch (CipherException | IOException e) {
                e.printStackTrace();
            }
            return false;
        });

    }

    private ECKeyPair getECKeyPair(String privateKey) {
        String key = privateKey;
        if (key.startsWith("0x")) {
            key = key.substring(2);
        }
        return ECKeyPair.create(Hex.decode(key));
    }

    @SuppressLint("CheckResult")
    @NotNull
    private Single<WalletTransactionHistory> getTransactionDetailRemote(@NotNull String transactionHashCode) {

        return Single.create(emitter -> {
            try {

                web3j.ethGetTransactionByHash(transactionHashCode).sendAsync().thenAccept(txDetails -> {
                    org.web3j.protocol.core.methods.response.Transaction fetchedTx = txDetails.getTransaction().orElseThrow();

                    BigInteger blockNumber;
                    try {
                        blockNumber = fetchedTx.getBlockNumber();
                    } catch (MessageDecodingException e) {
                        blockNumber = BigInteger.valueOf(-1);
                    }

                    if (blockNumber.compareTo(BigInteger.ZERO) > 0) {
                        web3j.ethGetTransactionReceipt(transactionHashCode).sendAsync().thenAccept(ethGetTransactionReceipt -> {
                            if (ethGetTransactionReceipt.getTransactionReceipt().isPresent()) {
                                database.updateWalletTransactionStatus(ethGetTransactionReceipt.getResult().getStatus(), ethGetTransactionReceipt.getResult().getBlockHash(), ethGetTransactionReceipt.getResult().getTransactionHash())
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread()).subscribe((walletTransactionHistory, throwable) -> {
                                    emitter.onSuccess(walletTransactionHistory);
                                });
//                                    WalletTransactionHistory transaction = new Mapper_TransactionReceipt_To_WalletTransactionHistory().mapperToWalletTransactionHistory(ethGetTransactionReceipt.getResult());
//                                    emitter.onSuccess(transaction);
                            } else {
                                emitter.onError(new Exception(context.getString(R.string.repository_transaction_not_present)));
                            }
                        }).exceptionally(throwable -> {
                            throwable.printStackTrace();
                            return null;
                        });
                    } else {
                        database.updateWalletTransactionStatus(TRANSACTION_SEEN, transactionHashCode).subscribe();
                        emitter.onError(new Exception(context.getString(R.string.repository_transaction_pending)));
                    }
                }).exceptionally(throwable -> {
                    String c1 = throwable.getMessage(); //java.util.NoSuchElementException: No value present
                    if (!TextUtils.isEmpty(c1) && c1.contains("NoSuchElementException")) { //we sighted this tx in the pool, now it's gone
                        database.getWalletTransaction(transactionHashCode).subscribe(new DisposableSingleObserver<WalletTransactionHistory>() {
                            @Override
                            public void onSuccess(@NonNull WalletTransactionHistory etherTransaction) {
                                //transaction is no longer in pool or on chain. Cause: dropped from mining pool
                                //mark transaction as dropped
                                if (etherTransaction.getTxreceiptStatus().equals(TRANSACTION_SEEN)) {
                                    database.updateWalletTransactionStatus(TRANSACTION_DROPPED, transactionHashCode).subscribe();
                                    emitter.onError(new Exception(context.getString(R.string.repository_transaction_dropped)));
                                }
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {

                            }
                        });
                    } else
                        emitter.onError(new Exception(context.getString(R.string.repository_transaction_unknown_error)));
                    return null;
                });
            } catch (Exception e) {
                e.printStackTrace();
                emitter.onError(e);
            }
        });
    }


    @NotNull
    private Single<WalletTransactionHistory> getTransactionDetailLocal(@NotNull String transactionHashCode) {
        return database.getWalletTransaction(transactionHashCode);
    }


    @NotNull
    @Override
    public Observable<WalletTransactionHistory> getTransactionDetail(@NotNull String transactionHashCode) {
        Single<WalletTransactionHistory> local = getTransactionDetailLocal(transactionHashCode);
        Single<WalletTransactionHistory> remote = getTransactionDetailRemote(transactionHashCode);
        return Observable.concatArray(local.toObservable(), remote.toObservable());
    }

    @NotNull
    @Override
    public Single<String> getMnemonic() {
        return database.getWalletSingle(currencyType).map(wallet -> {
            return SecurityUtils.getSecureString(wallet.getMnemonic(), wallet.getAddress(), context);
        });
    }


    @NotNull
    @Override
    public Single<String> getPrivateKey() {
        return database.getWalletSingle(currencyType).map(wallet -> {
            return SecurityUtils.getSecureString(wallet.getPrivateKey(), wallet.getAddress(), context);
        });
    }

    @Override
    public Observable<List<WalletTransactionHistory>> getTransactionHistory() {

        Credentials credentials = CryptoWalletUtils.getWalletCredential(context, database, currencyType);
        String walletAddress = "";
        if (credentials != null)
            walletAddress = credentials.getAddress();

        Single<List<WalletTransactionHistory>> apiRequest = getTransactionFromApi(walletAddress);

        Single<List<WalletTransactionHistory>> localRequest = database.getWalletTransactionList(currencyType);

        String finalWalletAddress = walletAddress;
        return Observable.concatArray(localRequest.toObservable(), apiRequest.toObservable()).map(history -> {
            if (history != null)
                for (WalletTransactionHistory transaction : history) {
                    transaction.setSend(transaction.getFrom().equalsIgnoreCase(finalWalletAddress));
                }

            return history;
        });
    }

    private Single<List<WalletTransactionHistory>> getTransactionFromApi(String walletAddress) {
        Single<List<WalletTransactionHistory>> apiRequest;
        if (!TextUtils.isEmpty(walletAddress)) {
            apiRequest = etherApiInterface.getBSCTransactions(walletAddress, getContractAddress(), "desc", 1, ApiConstants.ETHER_MAX_TRANSACTION_RECORDS)
                    .flatMap(etherTransactionResponse -> {
                        ArrayList<WalletTransactionHistory> transactions = etherTransactionResponse.getResult();
                        for (WalletTransactionHistory trans : transactions) {
                            trans.setType(currencyType);
                        }
                        return database.saveWalletTransaction(transactions);
                    }).flatMap(etherTransactions -> database.getWalletTransactionList(currencyType));
        } else {
            apiRequest = Single.just(new ArrayList<>());
        }

        return apiRequest;
    }

    @NotNull
    @Override
    public Single<TransactionGas> getNetworkFee() {
        return etherApiInterface.getBSCTransactionsGas().map(gas -> {
            TransactionGas transactionGas = new TransactionGas();
            transactionGas.setSafeGasPrice(convertFromWei(gas.getDecimalResult()));
            transactionGas.setProposeGasPrice(convertFromWei(gas.getDecimalResult()));
            transactionGas.setFastGasPrice(convertFromWei(gas.getDecimalResult()));

            FiatType currency = userSession.getCurrentCurrency();
            List<CoinExchangeRate> exchangeRate = userSession.getCoinExchangeRates();

            transactionGas.setCoin(CoinHelper.INSTANCE.getCoin(CryptoCurrencyType.BINANCE, currency, exchangeRate));
            transactionGas.setGasCryptoCurrencyType(CryptoCurrencyType.BINANCE);

            return transactionGas;
        });
    }


    @SuppressLint("CheckResult")
    private Wallet storeWallet(String mnemonic, String privateKey, String fileName, String address) {
        String uniqueKey = getContractAddress();
        Wallet wallet = WalletFactory.getWallet(currencyType, address, uniqueKey);
        try {
            database.deleteWallet(currencyType);

            if (!TextUtils.isEmpty(mnemonic))
                wallet.setMnemonic(SecurityUtils.saveSecureString(mnemonic, address, context));
            if (!TextUtils.isEmpty(privateKey))
                wallet.setPrivateKey(SecurityUtils.saveSecureString(privateKey, address, context));

            wallet.setFileName(fileName);

            database.saveOrReplaceWallet(wallet);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return wallet;
    }

    /**
     * create token network coin wallet
     *
     * @param mnemonic
     * @param privateKey
     * @param fileName
     * @param address
     * @return
     */
    @SuppressLint("CheckResult")
    private Wallet storeBinanceWallet(String mnemonic, String privateKey, String fileName, String address) {
        String uniqueKey = address;
        Wallet wallet = WalletFactory.getWallet(CryptoCurrencyType.BINANCE, address, uniqueKey);
        try {
            database.deleteWallet(CryptoCurrencyType.BINANCE);

            if (!TextUtils.isEmpty(mnemonic))
                wallet.setMnemonic(SecurityUtils.saveSecureString(mnemonic, address, context));
            if (!TextUtils.isEmpty(privateKey))
                wallet.setPrivateKey(SecurityUtils.saveSecureString(privateKey, address, context));

            wallet.setFileName(fileName);
            wallet.setUserVerifiedMnemonic(true);
            database.saveOrReplaceWallet(wallet);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return wallet;
    }

    private String convertFromWei(String value) {
        return Convert.fromWei(String.valueOf(new BigInteger(value).multiply(BSC_GAS_LIMIT)), Convert.Unit.GWEI).toString();
    }

    private int getChainID() {
        return isMainNet() ? (byte) 56 : (byte) 97;
    }

    private boolean isMainNet() {
        return true;
    }
}
