package com.android.l2l.twolocal.dataSourse.repository.wallet

import com.android.l2l.twolocal.model.Wallet
import com.android.l2l.twolocal.model.WalletTransactionHistory
import com.android.l2l.twolocal.model.enums.CryptoCurrencyType
import io.reactivex.Observable
import io.reactivex.Single

interface WalletRepositoryHelper {
    fun getWalletSingle(WalletType: CryptoCurrencyType): Single<Wallet?>
    fun getWallet(walletType: CryptoCurrencyType): Wallet?
    fun saveWallet(wallet: Wallet): Boolean
    fun saveWalletSingle(wallet: Wallet): Single<Wallet>
    fun getWalletList(): Single<List<Wallet>>
    fun activeWallet(walletType: CryptoCurrencyType): Single<Boolean>
    fun removeWallet(walletType: CryptoCurrencyType): Single<Boolean>
    fun deleteAllTables(): Single<Boolean>
    fun getAllWalletTransactionList(): Single<List<WalletTransactionHistory>>
    fun getWalletTransactionList(walletType: CryptoCurrencyType): Single<List<WalletTransactionHistory>>
}