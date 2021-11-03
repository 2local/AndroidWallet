package com.android.l2l.twolocal.dataSourse.repository.crypto.bsc;

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.android.l2l.twolocal.dataSourse.local.db.AppDatabase
import com.android.l2l.twolocal.dataSourse.local.prefs.UserSession
import com.android.l2l.twolocal.dataSourse.remote.currency.CryptoCurrencyRemoteDataSourceHelper
import com.android.l2l.twolocal.dataSourse.repository.MockWebServerBaseTest
import com.android.l2l.twolocal.model.Wallet
import com.android.l2l.twolocal.model.enums.CryptoCurrencyType
import com.google.common.truth.Truth
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.web3j.protocol.Web3j
import java.lang.reflect.Method
import java.math.BigDecimal

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class BinanceRepositoryTest: MockWebServerBaseTest() {

    override fun isMockServerEnabled() = true

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var context: Context

//    private lateinit var etherApiInterface: CryptoCurrencyApiInterface
    private lateinit var database: AppDatabase
    lateinit var repository: BinanceRepository
    lateinit var web3j: Web3j

    @Before
    fun start() {
        context = mockk<Context>()
        database = mockk<AppDatabase>()
        web3j = mockk<Web3j>()
        val userSession = mockk<UserSession>()
        val etherApiInterface = mockk<CryptoCurrencyRemoteDataSourceHelper>()
        repository = BinanceRepository(context, web3j, etherApiInterface, database, userSession, CryptoCurrencyType.TwoLC)
    }


    @Test
    fun userHasEnoughTokenBalanceTest() {
        //Given
        val wallet = Wallet()
        wallet.amount = "1"

        //When
        coEvery {
            database.getWallet(CryptoCurrencyType.TwoLC)
        } returns wallet

        val privateStringMethod: Method =
            BinanceRepository::class.java.getDeclaredMethod("userHasEnoughBalance", BigDecimal::class.java, BigDecimal::class.java)
        privateStringMethod.isAccessible = true

        //Then
        val case1 = privateStringMethod.invoke(repository, BigDecimal("0.5"), BigDecimal("0.2")) as Boolean
        Truth.assertThat(case1).isTrue()

        val case2 = privateStringMethod.invoke(repository, BigDecimal("1.5"), BigDecimal("0.2")) as Boolean
        Truth.assertThat(case2).isFalse()

        val case3 = privateStringMethod.invoke(repository, BigDecimal("0.8"), BigDecimal("0.2")) as Boolean
        Truth.assertThat(case3).isTrue()
    }


    @Test
    fun checkValidEtherAddressTest() {
        //When

        //Then
        val case1 = repository.checkValidEtherAddress("0xC4a43BB218b623b451ddbe5e2d568DD1595865Aa")
        Truth.assertThat(case1).isTrue()

        val case2 = repository.checkValidEtherAddress("C4a43BB218b623b451ddbe5e2d568DD1595865Aa")
        Truth.assertThat(case2).isTrue()

        //wallet address with invalid length
        val case3 = repository.checkValidEtherAddress("0xC4a43BB218b623b451ddbe5e2d568DD15958")
        Truth.assertThat(case3).isFalse()

        //wrong wallet address
        val case4 = repository.checkValidEtherAddress("0x11113BB2rt6723b451ddbe5e2d5yyDD1595865Aa")
        Truth.assertThat(case4).isFalse()
    }

//    @Test
//    fun restoreWalletFromMnemonicTest() {
//        //Given
//        val ageField: Field = BinanceRepository::class.java
//            .getDeclaredField("context")
//        ageField.setAccessible(true)
//
//        ageField.set(repository, context)
//
//        coEvery {
//            context.getFilesDir()
//        } returns File("")
//
//        val testSingle = TestObserver<Boolean>()
//        repository.restoreWalletFromMnemonic("front front expect moon brown  attract congress advice potato moon region moon").subscribe(
//            testSingle
//        )
//        testSingle.await().assertValue {
//            it == true
//        }
//        testSingle.assertComplete()
//        testSingle.assertNoErrors()
//    }

//    @Test
//    fun restoreWalletFromPrivateKeyTest() {
//        //Given
//
//        coEvery {
//            context.getFilesDir()
//        } returns getInstrumentation().getContext().getDir("tmp1", Context.MODE_PRIVATE);
//
//        val testSingle = TestObserver<Boolean>()
//        repository.restoreWalletFromPrivateKey("0x02be51640adb86933608b53e4ce522c930d39cf6004b17f5905c4062f21e5ab2").subscribe(
//            testSingle
//        )
//        testSingle.await().assertValue {
//            it == true
//        }
//        testSingle.assertComplete()
//        testSingle.assertNoErrors()
//    }
}