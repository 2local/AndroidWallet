package com.android.l2l.twolocal.dataSourse.repository.crypto

import com.android.l2l.twolocal.model.TransactionGas
import com.android.l2l.twolocal.model.WalletTransactionHistory
import com.android.l2l.twolocal.model.WalletBalance
import io.reactivex.Observable
import io.reactivex.Single
import java.math.BigDecimal

interface CryptoCurrencyRepositoryHelper {
    fun createEtherWallet(): Single<Boolean>
    fun getWalletBalance(): Single<WalletBalance>
    fun sendAmount(to: String, tokenAmount: BigDecimal, gasLimit: BigDecimal): Single<String>
    fun restoreWalletFromMnemonic(mnemonic: String): Single<Boolean>
    fun restoreWalletFromPrivateKey(privateKey: String): Single<Boolean>
    fun getTransactionDetail(transactionHashCode: String): Observable<WalletTransactionHistory>
    fun getMnemonic(): Single<String>
    fun getPrivateKey(): Single<String>
    fun getTransactionHistory(): Observable<List<WalletTransactionHistory>>
    fun getNetworkFee(): Single<TransactionGas>
    fun checkValidEtherAddress(to: String): Boolean
}