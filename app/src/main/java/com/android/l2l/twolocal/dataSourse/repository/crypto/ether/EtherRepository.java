package com.android.l2l.twolocal.dataSourse.repository.crypto.ether;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;

import com.android.l2l.twolocal.R;
import com.android.l2l.twolocal.coin.CoinHelper;
import com.android.l2l.twolocal.dataSourse.local.db.AppDatabase;
import com.android.l2l.twolocal.dataSourse.local.prefs.UserSession;
import com.android.l2l.twolocal.dataSourse.remote.currency.CryptoCurrencyRemoteDataSourceHelper;
import com.android.l2l.twolocal.dataSourse.repository.crypto.CryptoCurrencyRepositoryHelper;
import com.android.l2l.twolocal.model.TransactionGas;
import com.android.l2l.twolocal.model.WalletTransactionHistory;
import com.android.l2l.twolocal.model.Wallet;
import com.android.l2l.twolocal.model.WalletBalance;
import com.android.l2l.twolocal.model.enums.CryptoCurrencyType;
import com.android.l2l.twolocal.model.mapper.Mapper_EthSendTransaction_To_WalletTransactionHistory;
import com.android.l2l.twolocal.model.enums.FiatType;
import com.android.l2l.twolocal.model.CoinExchangeRate;
import com.android.l2l.twolocal.dataSourse.repository.crypto.utils.CryptoWalletUtils;
import com.android.l2l.twolocal.utils.PriceFormatUtils;
import com.android.l2l.twolocal.utils.WalletFactory;
import com.android.l2l.twolocal.utils.SecurityUtils;

import org.bouncycastle.util.encoders.Hex;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
import org.web3j.protocol.core.methods.response.EthGetBalance;
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
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import static com.android.l2l.twolocal.dataSourse.remote.api.ApiConstants.ETHER_MAX_TRANSACTION_RECORDS;
import static com.android.l2l.twolocal.utils.constants.AppConstants.TRANSACTION_DROPPED;
import static com.android.l2l.twolocal.utils.constants.AppConstants.TRANSACTION_SEEN;


public class EtherRepository implements CryptoCurrencyRepositoryHelper {

    private final Context context;
    private final Web3j web3j;
    private final AppDatabase database;
    private final CryptoCurrencyRemoteDataSourceHelper etherApiInterface;
    private final UserSession userSession;
    private final CryptoCurrencyType currencyType;

    @Inject
    public EtherRepository(Context context, @Named("ETH") Web3j web3j, CryptoCurrencyRemoteDataSourceHelper etherApiInterface, AppDatabase database, UserSession userSession, CryptoCurrencyType currencyType) {
        this.context = context;
        this.web3j = web3j;
        this.database = database;
        this.etherApiInterface = etherApiInterface;
        this.userSession = userSession;
        this.currencyType = currencyType;
    }


    @NotNull
    @Override
    public Single<WalletBalance> getWalletBalance() {
        return Single.create(emitter -> {
            try {
                Credentials credentials = CryptoWalletUtils.getWalletCredential(context, database, currencyType);
                if(credentials!=null) {
                    EthGetBalance etherBalance = web3j.ethGetBalance(credentials.getAddress(), DefaultBlockParameterName.LATEST).sendAsync().get();
                    if (etherBalance.hasError())
                        emitter.onError(new Throwable(etherBalance.getError().getMessage()));
                    else {
                        String amount = Convert.fromWei(etherBalance.getBalance().toString(), Convert.Unit.ETHER).toString();
                        // there is a special condition when balance return 110, we solve it using below convert
                        amount = PriceFormatUtils.removeCharactersPriceIfExists(PriceFormatUtils.formatToDecimalPriceSixDigitsOptional(PriceFormatUtils.stringToBigDecimal(amount)));
                        emitter.onSuccess(new WalletBalance(amount, currencyType.name()));
                    }
                }else
                    emitter.onError(new Throwable("Credential error!"));
            } catch (InterruptedException | ExecutionException interruptedException) {
                interruptedException.printStackTrace();
                if (!emitter.isDisposed())
                    emitter.onError(interruptedException);
            }
        });
    }

    @Override
    public boolean checkValidEtherAddress(String etherAddress) {
        return WalletUtils.isValidAddress(etherAddress);
    }


    private BigInteger getTransactionGasPrice(String gas) throws Exception {
        BigInteger gasPrice;
        if (TextUtils.isEmpty(gas))
            gasPrice = web3j.ethGasPrice().send().getGasPrice();
        else
            gasPrice = Convert.toWei(String.valueOf(gas), Convert.Unit.ETHER).divide(new BigDecimal(Transfer.GAS_LIMIT), MathContext.DECIMAL32).toBigInteger();
//            gasPrice = Convert.toWei(String.valueOf(gas), Convert.Unit.ETHER).divide(new BigDecimal("10000"), MathContext.DECIMAL32).toBigInteger();
//            gasPrice = new BigInteger(BalanceUtils.ethToWei(gas));

        return gasPrice;
    }

    //user wallet balance should be greater than transaction amount + gas
    private boolean userHasEnoughBalance(BigDecimal amount, BigDecimal gas) {
        Wallet wallet = database.getWallet(currencyType);
        if (wallet != null) {
            BigDecimal totalNeededAmount = amount.add(gas);
            BigDecimal walletAmount = new BigDecimal(wallet.getAmount());
            return totalNeededAmount.compareTo(walletAmount) <= 0;/////////check this///////////////////////////////////////////////////////
        } else
            return false;
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
    @SuppressLint("CheckResult")
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
                    emitter.onError(new Throwable(context.getString(R.string.repository_you_dont_have_enough_balance) + tokenAmount.add(gas).toString() + " " + currencyType.getSymbol()));
                    return;
                }

                //////////////////////////check amount more than zero//////////////////////////////
                if (checkAmountLessThanZero(tokenAmount)) {
                    emitter.onError(new Throwable(context.getString(R.string.repository_send_amount_too_low)));
                    return;
                }

                // get gas price
                BigInteger gasWeiPrice = getTransactionGasPrice(gas.toString());

                // Get the latest nonce of current account
                BigInteger nonce = getNonce(credentials.getAddress());

                // Create the Transaction
                BigInteger tokenAmountWei = Convert.toWei(tokenAmount, Convert.Unit.ETHER).toBigInteger();
                RawTransaction rawTransaction = RawTransaction.createEtherTransaction(
                        nonce,
                        gasWeiPrice,
                        Transfer.GAS_LIMIT,
                        finalDestinationAddress,
                        tokenAmountWei
                );
                byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
                String hexValue = Numeric.toHexString(signedMessage);

                // Send transaction
                EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).send();
                if (ethSendTransaction.hasError()) {
                    if (!emitter.isDisposed()) emitter.onError(new Exception(ethSendTransaction.getError().getMessage()));
                    return;
                }

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
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe((walletTransactionHistory, throwable) -> {
                                            emitter.onSuccess(walletTransactionHistory);
                                        });
//                                WalletTransactionHistory transaction = new Mapper_TransactionReceipt_To_WalletTransactionHistory().mapperToWalletTransactionHistory(ethGetTransactionReceipt.getResult());
//                                emitter.onSuccess(transaction);
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
    public Single<Boolean> createEtherWallet() {
        return Single.create(emitter -> {
            String password = "";
            try {
//                File path = new File(context.getApplicationInfo().dataDir);
//                if (!path.exists()) {
//                    path.mkdir();
//                }

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
                    emitter.onSuccess(true);
                } catch (Exception e) {
                    e.printStackTrace();
                    emitter.onError(new Throwable(context.getString(R.string.repository_error_occurred)));
                }
            }
        });
    }


    @NotNull
    @Override
    public Single<Boolean> restoreWalletFromPrivateKey(@NotNull String privateKey) {
        return Single.fromCallable(() -> {
            try {
                String password = "";
                ECKeyPair keys = ECKeyPair.create(Hex.decode(privateKey));

                Credentials credentials = Credentials.create(privateKey);
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

    @Nullable
    @Override
    public Single<String> getPrivateKey() {
        return null;
    }

    @Override
    public Observable<List<WalletTransactionHistory>> getTransactionHistory() {

        Credentials credentials = CryptoWalletUtils.getWalletCredential(context, database, currencyType);
        if(credentials!=null) {
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
        }else{
            return Observable.just(new ArrayList<WalletTransactionHistory>());
        }
    }

    private Single<List<WalletTransactionHistory>> getTransactionFromApi(String walletAddress) {
        return etherApiInterface.getEtherTransactions(walletAddress, "desc", 1, ETHER_MAX_TRANSACTION_RECORDS)
                .flatMap(etherTransactionResponse -> {
                    ArrayList<WalletTransactionHistory> transactions = etherTransactionResponse.getResult();
                    for (WalletTransactionHistory trans : transactions) {
                        trans.setType(currencyType);
                    }
                    return database.saveWalletTransaction(transactions);
                }).flatMap(etherTransactions -> database.getWalletTransactionList(currencyType));
    }

    @SuppressLint("CheckResult")
    @Override
    public Single<TransactionGas> getNetworkFee() {
        return etherApiInterface.getEtherTransactionsGas().map(gas -> {
            TransactionGas transactionGas = new TransactionGas();

            transactionGas.setSafeGasPrice(convertFromWei(gas.getResult().getSafeGasPrice()));
            transactionGas.setProposeGasPrice(convertFromWei(gas.getResult().getProposeGasPrice()));
            transactionGas.setFastGasPrice(convertFromWei(gas.getResult().getFastGasPrice()));

            FiatType currency = userSession.getCurrentCurrency();
            List<CoinExchangeRate> exchangeRate = userSession.getCoinExchangeRates();

            transactionGas.setCoin(CoinHelper.INSTANCE.getCoin(CryptoCurrencyType.ETHEREUM, currency, exchangeRate));
            transactionGas.setGasCryptoCurrencyType(CryptoCurrencyType.ETHEREUM);

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


    @Override
    public Single<String> getMnemonic() {
        return database.getWalletSingle(currencyType).map(wallet -> {
            return SecurityUtils.getSecureString(wallet.getMnemonic(), wallet.getAddress(), context);
        });
    }

    private String convertFromWei(String value) {
        return Convert.fromWei(String.valueOf(new BigInteger(value).multiply(Transfer.GAS_LIMIT)), Convert.Unit.GWEI).toString();
    }
}
