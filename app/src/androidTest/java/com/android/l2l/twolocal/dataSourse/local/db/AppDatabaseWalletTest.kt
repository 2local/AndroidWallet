package com.android.l2l.twolocal.dataSourse.local.db

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.android.l2l.twolocal.common.withIO
import com.android.l2l.twolocal.model.DaoMaster
import com.android.l2l.twolocal.model.DaoSession
import com.android.l2l.twolocal.model.enums.CryptoCurrencyType
import com.android.l2l.twolocal.utils.WalletFactory
import com.google.common.truth.Truth.assertThat
import io.reactivex.disposables.CompositeDisposable
import org.greenrobot.greendao.database.Database
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.io.IOException

@RunWith(MockitoJUnitRunner::class)
class AppDatabaseWalletTest {



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
    fun saveOrReplaceWalletTest() {
        val wallet = WalletFactory.getWallet(CryptoCurrencyType.ETHEREUM, "0xC4a43BB218b623b451ddbe5e2d568DD1595865Aa", "0xC4a43BB218b623b451ddbe5e2d568DD1595865Aa")

        val saveWalletObserver = appDatabase.saveOrReplaceWallet(wallet)
        assertThat(saveWalletObserver).isTrue()

//        val result:Wallet = appDatabase.getWalletSingle(WalletType.ETHEREUM).blockingGet()
//        assertThat(result.type).isEqualTo(wallet.type)

    }

    @Test
    fun saveOrReplaceWalletAndReadUsingSingleTest() {
        //GIVEN
        val wallet = WalletFactory.getWallet(CryptoCurrencyType.ETHEREUM, "0xC4a43BB218b623b451ddbe5e2d568DD1595865Aa", "0xC4a43BB218b623b451ddbe5e2d568DD1595865Aa")
        val saveWalletObserver = appDatabase.saveOrReplaceWallet(wallet)

        //WHEN
        val result = appDatabase.getWalletSingle(CryptoCurrencyType.ETHEREUM).withIO().test()
        result.awaitTerminalEvent()
        compositeDisposable.add(result)

        //THEN
        result.assertValue {
            it.type == wallet.type
        }
    }

    @Test
    fun saveOrReplaceWalletAndReadByTypeAndAddressTest() {
        //GIVEN
        val wallet = WalletFactory.getWallet(CryptoCurrencyType.ETHEREUM, "0xC4a43BB218b623b451ddbe5e2d568DD1595865Aa", "0xC4a43BB218b623b451ddbe5e2d568DD1595865Aa")
        val saveWalletObserver = appDatabase.saveOrReplaceWallet(wallet)
        assertThat(saveWalletObserver).isTrue()


        //WHEN
        //wallet by type
        val resultWallet = appDatabase.getWallet(CryptoCurrencyType.ETHEREUM)
        //THEN
        assertThat(resultWallet).isNotNull()
        assertThat(resultWallet!!.type).isEqualTo(wallet.type)

        //WHEN
        //wallet by address
        val resultWallet2 = appDatabase.getWallet(wallet.address)
        //THEN
        assertThat(resultWallet2).isNotNull()
        assertThat(resultWallet2!!.type).isEqualTo(wallet.type)
    }

    @Test
    fun readAnUpdateWalletTest() {
        //GIVEN
        val wallet = WalletFactory.getWallet(CryptoCurrencyType.ETHEREUM, "0xC4a43BB218b623b451ddbe5e2d568DD1595865Aa", "0xC4a43BB218b623b451ddbe5e2d568DD1595865Aa")
        // save wallet
        appDatabase.saveOrReplaceWallet(wallet)

        // update wallet
        wallet.walletName = "new name"
        val updateWalletObserver = appDatabase.saveOrReplaceWallet(wallet)
        assertThat(updateWalletObserver).isTrue()

        //WHEN
        val result2 = appDatabase.getWalletSingle(CryptoCurrencyType.ETHEREUM).withIO().test()
        result2.awaitTerminalEvent()
        compositeDisposable.add(result2)

        //THEN
        result2.assertValue {
            it.walletName == "new name"
        }
    }


    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
        compositeDisposable.dispose()
    }
}