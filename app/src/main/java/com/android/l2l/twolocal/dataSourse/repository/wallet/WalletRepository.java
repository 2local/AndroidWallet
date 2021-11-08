package com.android.l2l.twolocal.dataSourse.repository.wallet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;

import com.android.l2l.twolocal.coin.CoinHelper;
import com.android.l2l.twolocal.dataSourse.local.db.AppDatabase;
import com.android.l2l.twolocal.dataSourse.local.prefs.UserSession;
import com.android.l2l.twolocal.model.Wallet;
import com.android.l2l.twolocal.model.WalletTransactionHistory;
import com.android.l2l.twolocal.model.enums.CryptoCurrencyType;
import com.android.l2l.twolocal.model.enums.FiatType;
import com.android.l2l.twolocal.model.CoinExchangeRate;
import com.android.l2l.twolocal.utils.SecurityUtils;
import com.android.l2l.twolocal.utils.WalletFactory;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;


public class WalletRepository implements WalletRepositoryHelper {

    private final AppDatabase database;
    private final Context context;
    private final UserSession userSession;

    @Inject
    public WalletRepository(AppDatabase database, Context context, UserSession userSession) {
        this.database = database;
        this.context = context;
        this.userSession = userSession;
    }

    @Override
    public Single<Wallet> getWalletSingle(CryptoCurrencyType WalletType) {
        return database.getWalletSingle(WalletType).map(wallet -> {
            FiatType currency = userSession.getCurrentCurrency();
            List<CoinExchangeRate> exchangeRate = userSession.getCoinExchangeRates();
            if (wallet != null) {
                wallet.setCurrency(currency);
                wallet.setCoin(CoinHelper.INSTANCE.getCoin(wallet.getType(), currency, exchangeRate));
            }
            return wallet;
        });
    }

    @Override
    public Single<List<Wallet>> getWalletList() {
        return database.getWalletList().map(wallets -> {
            FiatType currency = userSession.getCurrentCurrency();
            List<CoinExchangeRate> exchangeRate = userSession.getCoinExchangeRates();
            for (Wallet wallet : wallets) {
                wallet.setCurrency(currency);
                wallet.setCoin(CoinHelper.INSTANCE.getCoin(wallet.getType(), currency, exchangeRate));
            }
            return wallets;
        });
    }

    @Nullable
    @Override
    public Wallet getWallet(@NotNull CryptoCurrencyType walletType) {
        Wallet wallet = database.getWallet(walletType);
        FiatType currency = userSession.getCurrentCurrency();
        List<CoinExchangeRate> exchangeRate = userSession.getCoinExchangeRates();
        if (wallet != null) {
            wallet.setCurrency(currency);
            wallet.setCoin(CoinHelper.INSTANCE.getCoin(wallet.getType(), currency, exchangeRate));
        }
        return wallet;
    }

    @Override
    public boolean saveWallet(@Nullable Wallet wallet) {
        return database.saveOrReplaceWallet(wallet);
    }


    @NotNull
    @Override
    public Single<Wallet> saveWalletSingle(@NotNull Wallet wallet) {
        return Single.fromCallable(() -> {
            database.saveOrReplaceWallet(wallet);
            return wallet;
        });
    }

    @Override
    public Single<Boolean> activeWallet(@NotNull CryptoCurrencyType walletType) {
        return Single.fromCallable(() -> {
            Wallet wallet = getWallet(walletType);
            wallet.setUserVerifiedMnemonic(true);
            return database.saveOrReplaceWallet(wallet);
        });
    }

    @SuppressLint("CheckResult")
    @Override
    public Single<Boolean> removeWallet(@NotNull CryptoCurrencyType walletType) {
        return database.getWalletSingle(walletType)
                .flatMap((Function<Wallet, SingleSource<Boolean>>) wallet -> Single.create(emitter -> {
                    try {

                        File path = new File(context.getApplicationInfo().dataDir);
                        File[] files = path.listFiles();
                        if (files != null && files.length != 0) {
                            for (File file : files) {
                                if (file.getName().equals(wallet.getEtherFileName())) { // prefsHelper.getEtherWalletFileName()
                                    file.delete();
                                    break;
                                }
                            }
                        }
                        boolean removeWallet = database.deleteWallet(walletType);
                        emitter.onSuccess(removeWallet);
                    } catch (Exception e) {
                        e.printStackTrace();
                        emitter.onError(e);
                    }
                }))
                .flatMap((Function<Boolean, SingleSource<Boolean>>) isSuccess -> {
                    return database.deleteTransactions(walletType);
                });
    }

    @NotNull
    @Override
    public Single<Boolean> deleteAllTables() {
        return database.deleteAllTables();
    }

    @NotNull
    @Override
    public Single<List<WalletTransactionHistory>> getAllWalletTransactionList() {
        return Single.zip(database.getAllWalletTransactionList(), database.getWalletList(), (walletTransactionHistories, walletList) -> {
                    for (WalletTransactionHistory transactionHistory : walletTransactionHistories) {
                        for (Wallet wallet : walletList) {
                            if (transactionHistory.getType() == wallet.getType())
                                transactionHistory.setSend(transactionHistory.getFrom().equalsIgnoreCase(wallet.getAddress()));
                        }
                    }
                    return walletTransactionHistories;
                }
        );
    }

    @NotNull
    @Override
    public Single<List<WalletTransactionHistory>> getWalletTransactionList(@NotNull CryptoCurrencyType walletType) {
        return database.getWalletTransactionList(walletType);
    }

    @NotNull
    @Override
    public boolean createTemporaryWallet(@NotNull CryptoCurrencyType currencyType, String address, String uniqueKey) {
        Wallet wallet = WalletFactory.getWallet(currencyType, address, uniqueKey);
        try {
            database.saveOrReplaceWallet(wallet);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return  false;
        }
    }
}
