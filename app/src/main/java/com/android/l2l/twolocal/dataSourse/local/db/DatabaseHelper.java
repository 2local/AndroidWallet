package com.android.l2l.twolocal.dataSourse.local.db;

import com.android.l2l.twolocal.model.AddressBook;
import com.android.l2l.twolocal.model.WalletTransactionHistory;
import com.android.l2l.twolocal.model.Wallet;
import com.android.l2l.twolocal.model.enums.CryptoCurrencyType;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;


public interface DatabaseHelper {


    Single<List<AddressBook>> getContactsFromDb();
    Single<List<AddressBook>> getContactsFromDb(CryptoCurrencyType walletType);
    Single<Long> addContactToDb(AddressBook addressBook);
    Single<Boolean> deleteSingleContact(AddressBook addressBook);
    Single<Boolean> deleteAllTables();
    Completable saveWalletTransaction(WalletTransactionHistory etherTransaction);
    Single<WalletTransactionHistory> updateWalletTransactionStatus(String status, String blockHash, String transactionHash);
    Completable updateWalletTransactionStatus(String status, String transactionHash);
    Single<List<WalletTransactionHistory>> saveWalletTransaction(List<WalletTransactionHistory> etherTransaction);
    Single<Boolean> deleteTransactions(CryptoCurrencyType walletType);
    Single<Boolean> deleteWallet(String walletAddress);
    boolean saveOrReplaceWallet(Wallet wallet);
    Boolean saveOrReplaceWallet(List<Wallet> walletList);
    Single<List<Wallet>> getWalletList();

    Single<Wallet> getWalletSingle(String uniqueID);

    Single<Wallet> getWalletSingle(CryptoCurrencyType walletType);

    Wallet getWallet(String uniqueID);

    Wallet getWallet(CryptoCurrencyType walletType);

    Boolean deleteWallet(CryptoCurrencyType walletType);

    Single<List<WalletTransactionHistory>> getWalletTransactionList(CryptoCurrencyType walletType);


    Single<List<WalletTransactionHistory>> getAllWalletTransactionList();

    Single<WalletTransactionHistory> getWalletTransaction(String transactionHash);
}
