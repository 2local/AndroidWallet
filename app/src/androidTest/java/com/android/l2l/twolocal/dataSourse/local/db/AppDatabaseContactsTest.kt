package com.android.l2l.twolocal.dataSourse.local.db

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.android.l2l.twolocal.common.withIO
import com.android.l2l.twolocal.model.AddressBook
import com.android.l2l.twolocal.model.DaoMaster
import com.android.l2l.twolocal.model.DaoSession
import com.android.l2l.twolocal.model.enums.CryptoCurrencyType
import io.reactivex.disposables.CompositeDisposable
import org.greenrobot.greendao.database.Database
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.io.IOException

@RunWith(MockitoJUnitRunner::class)
class AppDatabaseContactsTest {



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
    fun readAndWriteContactTest() {
        val contactEther = AddressBook(
            "name ether", "0x93455463546", CryptoCurrencyType.ETHEREUM
        )

        val contact2LC = AddressBook(
            "name 2lc", "0x93455163596", CryptoCurrencyType.TwoLC
        )

        // add contact for ether wallet
        val saveContactObserver1 = appDatabase.addContactToDb(contactEther).withIO().test()
        saveContactObserver1.awaitTerminalEvent()
        compositeDisposable.add(saveContactObserver1)

        // add contact for 2lc wallet
        val saveContactObserver2 = appDatabase.addContactToDb(contact2LC).withIO().test()
        saveContactObserver2.awaitTerminalEvent()
        compositeDisposable.add(saveContactObserver2)


//        val result:Wallet = appDatabase.getWalletSingle(WalletType.ETHEREUM).blockingGet()
//        assertThat(result.type).isEqualTo(wallet.type)

        // get contact by wallet type
        //WHEN
        val result = appDatabase.getContactsFromDb(contactEther.type).withIO().test()
        result.awaitTerminalEvent()
        compositeDisposable.add(result)

        //THEN
        result.assertValue {
            it.size == 1 && it[0].name == contactEther.name && it[0].type == contactEther.type
        }

        ////////////////////////////////

        //WHEN
        val resultAll = appDatabase.getContactsFromDb().withIO().test()
        resultAll.awaitTerminalEvent()
        compositeDisposable.add(resultAll)

        //THEN
        resultAll.assertValue {
            it.size == 2
        }
    }

    @Test
    fun deleteContactTest() {
        //GIVEN
        val contactEther = AddressBook(1,"name", "0x93455463546", CryptoCurrencyType.ETHEREUM)

        // add contact
        val saveContactObserver = appDatabase.addContactToDb(contactEther).withIO().test()
        saveContactObserver.awaitTerminalEvent()
        compositeDisposable.add(saveContactObserver)

        //WHEN
        val result = appDatabase.deleteSingleContact(contactEther).withIO().test()
        result.awaitTerminalEvent()
        compositeDisposable.add(result)

        //THEN
        result.assertValue {
            it
        }
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
        compositeDisposable.dispose()
    }
}