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
import com.android.l2l.twolocal.model.mapper.Mapper_EthSendTransaction_To_WalletTransactionHistory;
import com.android.l2l.twolocal.model.CoinExchangeRate;
import com.android.l2l.twolocal.sdk.binancesmartchainsdk.util.CryptoWalletUtils;
import com.android.l2l.twolocal.utils.CommonUtils;
import com.android.l2l.twolocal.utils.SecurityUtils;
import com.android.l2l.twolocal.utils.WalletFactory;

import org.bouncycastle.util.encoders.Hex;
import org.jetbrains.annotations.NotNull;
import org.web3j.crypto.Bip39Wallet;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.MnemonicUtils;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.crypto.WalletUtils;
import org.web3j.exceptions.MessageDecodingException;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

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

public class BinanceRepository implements CryptoCurrencyRepositoryHelper {

    private final Context context;
    private final AppDatabase database;
    private final UserSession userSession;
    private final CryptoCurrencyRemoteDataSourceHelper etherApiInterface;
    private final Web3j web3j;
    private final CryptoCurrencyType currencyType;

    @Inject
    public BinanceRepository(Context context,
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
            if (credentials != null) {
                BigInteger valueInWei = web3j
                        .ethGetBalance(credentials.getAddress(), DefaultBlockParameterName.LATEST)
                        .send()
                        .getBalance();
                String balance = Convert.fromWei(new BigDecimal(valueInWei), Convert.Unit.ETHER).toString();
                balance = CommonUtils.removeCharactersPriceIfExists(CommonUtils.formatToDecimalPriceSixDigitsOptional(CommonUtils.stringToBigDecimal(balance)));
                return new WalletBalance(balance, currencyType.getMyName());
            }else
                return new WalletBalance("0", currencyType.getMyName());
        });
    }


    @Override
    public boolean checkValidEtherAddress(String walletAddress) {
        return WalletUtils.isValidAddress(walletAddress);
    }

    /**
     * get transaction gas price
     *
     * @return
     */
    private BigInteger getTransactionGasPrice(String gas) {
        BigInteger gasPrice = null;
        try {
            if (TextUtils.isEmpty(gas))
                gasPrice = web3j.ethGasPrice().send().getGasPrice();
            else
                gasPrice = Convert.toWei(gas, Convert.Unit.ETHER).divide(new BigDecimal(Transfer.GAS_LIMIT), MathContext.DECIMAL32).toBigInteger();

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
    private boolean userHasEnoughBalance(BigDecimal amount, BigDecimal gas) {
        Wallet wallet = database.getWallet(currencyType);
        if (wallet != null) {
            BigDecimal totalNeededAmount = gas.add(amount);
            BigDecimal walletAmount = new BigDecimal(wallet.getAmount());
            return totalNeededAmount.compareTo(walletAmount) <= 0;
        } else
            return false;
    }


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
                if (!userHasEnoughBalance(tokenAmount, gas)) {
                    emitter.onError(new Throwable(context.getString(R.string.repository_you_dont_have_enough_balance) + gas.add(tokenAmount).toString() + " " + currencyType.getSymbol()));
                    return;
                }

                //////////////////////////check amount more than zero//////////////////////////////
                if (checkAmountLessThanZero(tokenAmount)) {
                    emitter.onError(new Throwable(context.getString(R.string.repository_send_amount_too_low)));
                    return;
                }

                // get gas price
                BigInteger gasWeiPrice = getTransactionGasPrice(gas.toString());


                BigInteger nonce = getNonce(credentials.getAddress());
                BigInteger tokenAmountWei = Convert.toWei(tokenAmount, Convert.Unit.ETHER).toBigIntegerExact();

                RawTransaction rawTransaction = RawTransaction.createEtherTransaction(nonce, gasWeiPrice, Transfer.GAS_LIMIT, finalDestinationAddress, tokenAmountWei);
                byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
                String hexValue = Numeric.toHexString(signedMessage);

                EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).sendAsync().get();

                if (ethSendTransaction.hasError()) {
                    if (!emitter.isDisposed()) emitter.onError(new Exception(ethSendTransaction.getError().getMessage()));
                    return;
                }
                Log.e("TransactionReceipt", ethSendTransaction.getTransactionHash());

                saveTransactionToDatabase(ethSendTransaction, tokenAmountWei, credentials.getAddress(), finalDestinationAddress);
                if (!emitter.isDisposed()) emitter.onSuccess(ethSendTransaction.getTransactionHash());

            } catch (Exception e) {
                e.printStackTrace();
                if (!emitter.isDisposed()) emitter.onError(e);
            }
        });
    }

    private void saveTransactionToDatabase(EthSendTransaction ethSendTransaction, BigInteger tokenAmount, String address, String finalDestinationAddress) {
        WalletTransactionHistory history = new Mapper_EthSendTransaction_To_WalletTransactionHistory().mapperToWalletTransactionHistory(ethSendTransaction, tokenAmount, address, finalDestinationAddress, currencyType);
        database.saveWalletTransaction(history)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe();
    }

    private BigInteger getNonce(String address) throws IOException {
        EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(address, DefaultBlockParameterName.LATEST).send();
        return ethGetTransactionCount.getTransactionCount();
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
//                    Log.v("restoreWalletFromMnemon", credentials.getAddress());
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
            String password = "";
            ECKeyPair keys = getECKeyPair(privateKey);

            try {
                Credentials credentials = Credentials.create(keys);
                String walletFileName = WalletUtils.generateLightNewWalletFile(password, CryptoWalletUtils.getWalletDestinationDirectory(context));
                storeWallet("", privateKey, walletFileName, credentials.getAddress());
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
        if (credentials != null) {
            String walletAddress = credentials.getAddress();
            Single<List<WalletTransactionHistory>> apiRequest = getTransactionFromApi(walletAddress);

            Single<List<WalletTransactionHistory>> localRequest = database.getWalletTransactionList(currencyType);

            return Observable.concatArray(localRequest.toObservable(), apiRequest.toObservable()).map(history -> {
                if (history != null)
                    for (WalletTransactionHistory transaction : history) {
                        transaction.setSend(transaction.getFrom().equalsIgnoreCase(walletAddress));
                    }

                return history;
            });
        } else
            return Observable.just(new ArrayList<WalletTransactionHistory>());
    }

    private Single<List<WalletTransactionHistory>> getTransactionFromApi(String walletAddress) {
        return etherApiInterface.getBinanceTransactions(walletAddress, "desc", 1, ApiConstants.ETHER_MAX_TRANSACTION_RECORDS)
                .flatMap(etherTransactionResponse -> {
                    ArrayList<WalletTransactionHistory> transactions = etherTransactionResponse.getResult();
                    for (int i = transactions.size() - 1; i >= 0; i--) {
                        transactions.get(i).setType(currencyType);
                        BigInteger val = new BigInteger(transactions.get(i).getValue());
                        if (val.compareTo(new BigInteger("0")) == 0)
                            transactions.remove(i);
                    }
                    return database.saveWalletTransaction(transactions);
                }).flatMap(etherTransactions -> database.getWalletTransactionList(currencyType));
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
        String uniqueKey = address;
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

    private String convertFromWei(String value) {
        return Convert.fromWei(String.valueOf(new BigInteger(value).multiply(Transfer.GAS_LIMIT)), Convert.Unit.GWEI).toString();
    }


    private int getChainID() {
        return isMainNet() ? (byte) 56 : (byte) 97;
    }

    private boolean isMainNet() {
        return true;
    }
}
