package com.android.l2l.twolocal.dataSourse.local.db;

import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.viewbinding.BuildConfig;

import com.android.l2l.twolocal.model.AddressBook;
import com.android.l2l.twolocal.model.AddressBookDao;
import com.android.l2l.twolocal.model.DaoSession;
import com.android.l2l.twolocal.model.Wallet;
import com.android.l2l.twolocal.model.WalletDao;
import com.android.l2l.twolocal.model.WalletTransactionHistory;
import com.android.l2l.twolocal.model.WalletTransactionHistoryDao;
import com.android.l2l.twolocal.model.enums.CryptoCurrencyType;

import org.greenrobot.greendao.query.DeleteQuery;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AppDatabase implements DatabaseHelper {

    private final DaoSession daoSession;

    @Inject
    public AppDatabase(DaoSession daoSession) {
        this.daoSession = daoSession;
        if (BuildConfig.DEBUG) {
            QueryBuilder.LOG_SQL = true;
            QueryBuilder.LOG_VALUES = true;
        }
    }

    @Override
    public Single<List<AddressBook>> getContactsFromDb() {
        daoSession.clear();
        return Single.fromCallable(() -> daoSession.getAddressBookDao().loadAll());
    }

    @Override
    public Single<List<AddressBook>> getContactsFromDb(CryptoCurrencyType walletType) {
        daoSession.clear();
        return Single.fromCallable(() -> daoSession.getAddressBookDao()
                .queryBuilder().where(AddressBookDao.Properties.Type.eq(walletType)).list());
    }

    @Override
    public Single<Long> addContactToDb(AddressBook addressBook) {
        daoSession.clear();
        return Single.fromCallable(() -> daoSession.getAddressBookDao().insert(addressBook));
    }

    @Override
    public Single<Boolean> deleteSingleContact(AddressBook addressBook) {
        daoSession.clear();
        return Single.fromCallable(() -> {
            daoSession.getAddressBookDao().delete(addressBook);
            return true;
        });
    }

    @Override
    public Single<Boolean> deleteAllTables() {
        daoSession.clear();
        return Single.fromCallable(() -> {
//            daoSession.getTransactionsDao().deleteAll();
            daoSession.getWalletTransactionHistoryDao().deleteAll();
            daoSession.getWalletDao().deleteAll();
            return true;
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<Boolean> deleteTransactions(CryptoCurrencyType walletType) {
        return Single.fromCallable(() -> {
            DeleteQuery<WalletTransactionHistory> tableDeleteQuery = daoSession.getWalletTransactionHistoryDao().queryBuilder()
                    .where(WalletTransactionHistoryDao.Properties.Type.eq(walletType)).buildDelete();
            tableDeleteQuery.executeDeleteWithoutDetachingEntities();
            daoSession.clear();
            return true;
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<Boolean> deleteWallet(String walletAddress) {
        return Single.fromCallable(() -> {
            daoSession.clear();
            daoSession.getWalletDao().deleteByKey(walletAddress);
            return true;
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Boolean deleteWallet(CryptoCurrencyType walletType) {

        try {
            daoSession.clear();
            DeleteQuery<Wallet> tableDeleteQuery = daoSession.getWalletDao().queryBuilder().where(WalletDao.Properties.Type.eq(walletType)).buildDelete();
            tableDeleteQuery.executeDeleteWithoutDetachingEntities();
            daoSession.clear();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Completable saveWalletTransaction(WalletTransactionHistory etherTransaction) {
        return Completable.create(emitter -> {
            try {
                daoSession.clear();
                daoSession.getWalletTransactionHistoryDao().insertOrReplace(etherTransaction);
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
            }

        });
    }

    @Override
    public Single<WalletTransactionHistory> updateWalletTransactionStatus(String status, String blockHash, String transactionHash) {
        return Single.create(emitter -> {
            try {
                daoSession.clear();
                List<WalletTransactionHistory> list = daoSession.getWalletTransactionHistoryDao().queryBuilder().where(WalletTransactionHistoryDao.Properties.Hash.eq(transactionHash)).list();
                if (list != null && !list.isEmpty()) {
                    list.get(0).setBlockHash(blockHash);
                    list.get(0).setTxreceiptStatus(status);
                    daoSession.getWalletTransactionHistoryDao().update(list.get(0));
                    emitter.onSuccess(list.get(0));
                }else
                    emitter.onError(new Exception("Transaction not found!"));
            } catch (Exception e) {
                emitter.onError(e);
            }

        });
    }

    @Override
    public Completable updateWalletTransactionStatus(String status, String transactionHash) {
        return Completable.create(emitter -> {
            try {
                daoSession.clear();
                List<WalletTransactionHistory> list = daoSession.getWalletTransactionHistoryDao().queryBuilder().where(WalletTransactionHistoryDao.Properties.Hash.eq(transactionHash)).list();
                if (list != null && !list.isEmpty()) {
                    list.get(0).setTxreceiptStatus(status);
                    daoSession.getWalletTransactionHistoryDao().update(list.get(0));
                }
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

    @Override
    public Single<List<WalletTransactionHistory>> saveWalletTransaction(List<WalletTransactionHistory> etherTransactionList) {
        return Single.create(emitter -> {
            try {
                daoSession.getWalletTransactionHistoryDao().insertOrReplaceInTx(etherTransactionList);
                emitter.onSuccess(etherTransactionList);
            } catch (Exception e) {
                emitter.onError(e);
            }

        });
    }

    @Override
    public boolean saveOrReplaceWallet(@Nullable Wallet wallet) {
        boolean success = false;
        if (wallet!=null && !TextUtils.isEmpty(wallet.getAddress()))
        try {
            daoSession.getWalletDao().insertOrReplace(wallet);
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }

    @Override
    public Boolean saveOrReplaceWallet(List<Wallet> walletList) {
        boolean success = false;
        try {
            daoSession.getWalletDao().insertOrReplaceInTx(walletList);
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }

    @Override
    public Single<List<Wallet>> getWalletList() {
        return Single.create(emitter -> {
            try {
                daoSession.clear();
                emitter.onSuccess(daoSession.getWalletDao().queryBuilder().where(WalletDao.Properties.UserVerifiedMnemonic.eq(true)).orderDesc(WalletDao.Properties.ShowingOrder).orderAsc(WalletDao.Properties.CreateWalletDate).list());
            } catch (Exception e) {
                e.printStackTrace();
                emitter.onError(e);
            }
        });
    }

    @Override
    public Single<Wallet> getWalletSingle(String uniqueID) {
        return Single.create(emitter -> {
            Wallet wallet = null;
            try {
                daoSession.clear();
                List<Wallet> list = daoSession.getWalletDao().queryBuilder().where(WalletDao.Properties.UniqueID.eq(uniqueID)).list();
                if (list != null && !list.isEmpty()) {
                    wallet = list.get(0);
                }
                emitter.onSuccess(wallet);
            } catch (Exception e) {
                e.printStackTrace();
                emitter.onError(e);
            }
        });
    }

    @Override
    public Single<Wallet> getWalletSingle(CryptoCurrencyType walletType) {
        return Single.create(emitter -> {
            Wallet wallet = null;
            try {
                daoSession.clear();
                List<Wallet> list = daoSession.getWalletDao().queryBuilder().where(WalletDao.Properties.Type.eq(walletType)).list();
                if (list != null && !list.isEmpty()) {
                    wallet = list.get(0);
                }
                if(wallet!=null) {
                    emitter.onSuccess(wallet);
                }else
                    emitter.onError(new Exception("Wallet not found"));
            } catch (Exception e) {
                e.printStackTrace();
                emitter.onError(e);
            }
        });
    }

    @Nullable
    @Override
    public Wallet getWallet(String uniqueID) {
        Wallet wallet = null;
        try {
            daoSession.clear();
            List<Wallet> list = daoSession.getWalletDao().queryBuilder().where(WalletDao.Properties.UniqueID.eq(uniqueID)).list();
            if (list != null && !list.isEmpty()) {
                wallet = list.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wallet;
    }

    @Nullable
    @Override
    public Wallet getWallet(CryptoCurrencyType walletType) {
        Wallet wallet = null;
        try {
            daoSession.clear();
            List<Wallet> list = daoSession.getWalletDao().queryBuilder().where(WalletDao.Properties.Type.eq(walletType)).list();
            if (list != null && !list.isEmpty()) {
                wallet = list.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wallet;
    }



    @Override
    public Single<List<WalletTransactionHistory>> getWalletTransactionList(CryptoCurrencyType walletType) {
        return Single.create(emitter -> {
            try {
                daoSession.clear();
                emitter.onSuccess(daoSession.getWalletTransactionHistoryDao().queryBuilder()
                        .where(WalletTransactionHistoryDao.Properties.Type.eq(walletType))
                        .orderDesc(WalletTransactionHistoryDao.Properties.TimeStamp).list());
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

    @Override
    public Single<List<WalletTransactionHistory>> getAllWalletTransactionList() {
        return Single.create(emitter -> {
            try {
                daoSession.clear();
                emitter.onSuccess(daoSession.getWalletTransactionHistoryDao().queryBuilder()
                        .orderDesc(WalletTransactionHistoryDao.Properties.TimeStamp).list());
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

    @Override
    public Single<WalletTransactionHistory> getWalletTransaction(String transactionHash) {
        return Single.create(emitter -> {
            try {
                daoSession.clear();
                List<WalletTransactionHistory> list = daoSession.getWalletTransactionHistoryDao().queryBuilder().where(WalletTransactionHistoryDao.Properties.Hash.eq(transactionHash)).list();
                if (list != null && !list.isEmpty()) {
                    emitter.onSuccess(list.get(0));
                }else
                    emitter.onError(new Exception("not found"));
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }
}
