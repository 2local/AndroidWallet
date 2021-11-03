package com.android.l2l.twolocal.dataSourse.local.db

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.android.l2l.twolocal.model.DaoMaster
import com.android.l2l.twolocal.model.DaoSession
import com.android.l2l.twolocal.model.WalletTransactionHistory
import com.android.l2l.twolocal.model.enums.CryptoCurrencyType
import com.android.l2l.twolocal.utils.constants.AppConstants.TRANSACTION_PENDING
import com.android.l2l.twolocal.utils.constants.AppConstants.TRANSACTION_SUCCESS
import io.reactivex.disposables.CompositeDisposable
import org.greenrobot.greendao.database.Database
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.io.IOException

@RunWith(MockitoJUnitRunner::class)
class AppDatabaseTtransactionTest {



    private lateinit var daoSession: DaoSession
    private lateinit var appDatabase: AppDatabase
    private val compositeDisposable = CompositeDisposable()
    private lateinit var db: Database

    @Before
    fun sutUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val openHelper = DaoMaster.DevOpenHelper(context, null)
        db = openHelper.writableDb
        daoSession = DaoMaster(db).newSession()
        appDatabase = AppDatabase(daoSession)
    }

    @Test
    fun readAndWriteTransactionTest() {
        ///GIVEN
        val etherTransaction = WalletTransactionHistory(
            "blockHash", "hash", " confirmations", "gasUsed", "cumulativeGasUsed",
            "contractAddress", "input", "txreceiptStatus", "isError", "gasPrice", "gas",
            "value", "to", "from", "transactionIndex", "nonce", "timeStamp",
            "blockNumber", "transactionHash","2local","2lc","18",  CryptoCurrencyType.ETHEREUM
        )

        ///WHEN
        val saveTransactionObserver = appDatabase.saveWalletTransaction(etherTransaction).test()
        compositeDisposable.add(saveTransactionObserver)
        saveTransactionObserver.assertComplete()
        saveTransactionObserver.assertNoErrors()

        val getAllTransactions = appDatabase.getWalletTransactionList(CryptoCurrencyType.ETHEREUM).test()
        compositeDisposable.add(getAllTransactions)
        getAllTransactions.assertComplete()
        getAllTransactions.assertNoErrors()

        //THEN
        getAllTransactions.assertValue {
            it.size == 1 && it[0].hash == etherTransaction.hash
        }
    }

    @Test
    fun updateWalletTransaction_Status_Hash_bloc() {
        ///GIVEN
        val hash = "transactionHash"
        val block = "234234"
        val etherTransaction = WalletTransactionHistory(
            "blockHash", hash, " confirmations", "gasUsed", "cumulativeGasUsed",
            "contractAddress", "input", TRANSACTION_PENDING, "isError", "gasPrice", "gas",
            "value", "to", "from", "transactionIndex", "nonce", "timeStamp",
            block, hash,"2local","2lc","18", CryptoCurrencyType.ETHEREUM
        )

        // save transaction
        val saveTransactionObserver = appDatabase.saveWalletTransaction(etherTransaction).test()
        compositeDisposable.add(saveTransactionObserver)
        saveTransactionObserver.assertComplete()
        saveTransactionObserver.assertNoErrors()

        ///WHEN
        // update transaction status
        val updateTransactions = appDatabase.updateWalletTransactionStatus(TRANSACTION_SUCCESS, block, hash).test()
        compositeDisposable.add(updateTransactions)
        updateTransactions.assertComplete()
        updateTransactions.assertNoErrors()

        // check ether transaction status
        val getAllTransactions = appDatabase.getWalletTransaction(hash).test()
        getAllTransactions.awaitTerminalEvent()
        compositeDisposable.add(getAllTransactions)

        ///THEN
        getAllTransactions.assertValue {
           it.txreceiptStatus == TRANSACTION_SUCCESS
        }

    }

     @Test
    fun updateWalletTransaction_Status_Hash() {
         //GIVEN
        val hash = "transactionHash"
        val block = "234234"
        val etherTransaction = WalletTransactionHistory(
            "blockHash", hash, " confirmations", "gasUsed", "cumulativeGasUsed",
            "contractAddress", "input", TRANSACTION_PENDING, "isError", "gasPrice", "gas",
            "value", "to", "from", "transactionIndex", "nonce", "timeStamp",
            block, hash,"2local","2lc","18", CryptoCurrencyType.ETHEREUM
        )

        // save transaction
        val saveTransactionObserver = appDatabase.saveWalletTransaction(etherTransaction).test()
        compositeDisposable.add(saveTransactionObserver)
        saveTransactionObserver.assertComplete()
        saveTransactionObserver.assertNoErrors()

         ///WHEN
        // update transaction status
        val updateTransactions = appDatabase.updateWalletTransactionStatus(TRANSACTION_SUCCESS, hash).test()
        compositeDisposable.add(updateTransactions)
        updateTransactions.assertComplete()
        updateTransactions.assertNoErrors()


         // check ether transaction status
        val getAllTransactions = appDatabase.getWalletTransaction(hash).test()
        getAllTransactions.awaitTerminalEvent()
        compositeDisposable.add(getAllTransactions)

         ///THEN
        getAllTransactions.assertValue {
           it.txreceiptStatus == TRANSACTION_SUCCESS
        }

    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
        compositeDisposable.dispose()
    }
}