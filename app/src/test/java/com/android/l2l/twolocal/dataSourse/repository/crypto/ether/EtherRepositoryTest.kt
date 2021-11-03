package com.android.l2l.twolocal.dataSourse.repository.crypto.ether

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.android.l2l.twolocal.dataSourse.local.db.AppDatabase
import com.android.l2l.twolocal.dataSourse.local.prefs.UserSession
import com.android.l2l.twolocal.dataSourse.remote.currency.CryptoCurrencyApiInterface
import com.android.l2l.twolocal.dataSourse.remote.currency.CryptoCurrencyRemoteDataSourceHelper
import com.android.l2l.twolocal.dataSourse.repository.MockWebServerBaseTest
import com.android.l2l.twolocal.model.Wallet
import com.android.l2l.twolocal.model.enums.CryptoCurrencyType
import com.android.l2l.twolocal.model.response.EtherTransactionGasResponse
import com.android.l2l.twolocal.model.response.EtherTransactionResponse
import com.google.common.truth.Truth
import io.mockk.coEvery
import io.mockk.mockk
import io.reactivex.observers.TestObserver
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.web3j.protocol.Web3j
import java.lang.reflect.Method
import java.math.BigDecimal
import java.net.HttpURLConnection

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class EtherRepositoryTest : MockWebServerBaseTest() {

    override fun isMockServerEnabled() = true

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var context: Context

//    private lateinit var etherApiInterface: CryptoCurrencyRemoteDataSourceHelper
    private lateinit var database: AppDatabase
    lateinit var repository: EtherRepository

    @Before
    fun start() {
        context = mockk<Context>()
        database = mockk<AppDatabase>()
        val web3j = mockk<Web3j>()
        val userSession = mockk<UserSession>()
        val etherApiInterface = mockk<CryptoCurrencyRemoteDataSourceHelper>()
        repository = EtherRepository(context, web3j, etherApiInterface, database, userSession, CryptoCurrencyType.TwoLC)
    }


    @Test
    fun getEtherTransactionsApiTest() {
        mockHttpResponse("json/ether_history_response.json", HttpURLConnection.HTTP_OK)

        val testSingle = TestObserver<EtherTransactionResponse>()
        val etherApiInterface = provideTestApiService(CryptoCurrencyApiInterface::class.java)
        etherApiInterface.getEtherTransactions("0xbd19c48c8177d51a94621db57d818b94882498c4", "desc", 1, 2).subscribe(testSingle)
        testSingle.await().assertValue {
            it.result.size == 2
        }.assertValue {
            it.status == "1"
        }
        testSingle.assertComplete()
        testSingle.assertNoErrors()
    }

    @Test
    fun etherGasTest() {
        mockHttpResponse("json/ether_gas_response.json", HttpURLConnection.HTTP_OK)
        val etherApiInterface = provideTestApiService(CryptoCurrencyApiInterface::class.java)
        val testSingle = TestObserver<EtherTransactionGasResponse>()
        etherApiInterface.getEtherTransactionsGas().subscribe(testSingle)
        testSingle.await().assertValue {
            it.result.fastGasPrice.isNotBlank()
            it.result.safeGasPrice.isNotBlank()
            it.result.proposeGasPrice.isNotBlank()
        }.assertValue {
            it.status == "1"
        }
        testSingle.assertComplete()
        testSingle.assertNoErrors()
    }

    @Test
    fun walletHasEnoughBalanceTest() {
        //Given
        val wallet = Wallet()
        wallet.amount = "1"

        //When
        coEvery {
            database.getWallet(CryptoCurrencyType.TwoLC)
        } returns wallet

        val privateStringMethod: Method =
            EtherRepository::class.java.getDeclaredMethod("userHasEnoughBalance", BigDecimal::class.java, BigDecimal::class.java)
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

}