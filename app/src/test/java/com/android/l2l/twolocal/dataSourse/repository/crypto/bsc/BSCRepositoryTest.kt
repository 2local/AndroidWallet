package com.android.l2l.twolocal.dataSourse.repository.crypto.bsc;

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.android.l2l.twolocal.dataSourse.local.db.AppDatabase
import com.android.l2l.twolocal.dataSourse.local.prefs.UserSession
import com.android.l2l.twolocal.dataSourse.remote.currency.CryptoCurrencyApiInterface
import com.android.l2l.twolocal.dataSourse.remote.currency.CryptoCurrencyRemoteDataSourceHelper
import com.android.l2l.twolocal.dataSourse.repository.MockWebServerBaseTest
import com.android.l2l.twolocal.dataSourse.repository.crypto.ether.EtherRepository
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
import org.web3j.protocol.core.DefaultBlockParameterName
import java.lang.reflect.Method
import java.math.BigDecimal
import java.math.BigInteger
import java.net.HttpURLConnection

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class BSCRepositoryTest: MockWebServerBaseTest() {

    override fun isMockServerEnabled() = true

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var context: Context

    private lateinit var etherApiInterface: CryptoCurrencyApiInterface
    private lateinit var database: AppDatabase
    lateinit var repository: BSCRepository
    lateinit var web3j: Web3j

    @Before
    fun start() {
        context = mockk<Context>()
        database = mockk<AppDatabase>()
        web3j = mockk<Web3j>()
        val userSession = mockk<UserSession>()
        val etherApiInterface = mockk<CryptoCurrencyRemoteDataSourceHelper>()
        repository = BSCRepository(context, web3j, etherApiInterface, database, userSession, CryptoCurrencyType.TwoLC)
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
            BSCRepository::class.java.getDeclaredMethod("userHasEnoughTokenBalance",  BigDecimal::class.java)
        privateStringMethod.isAccessible = true

        //Then
        val case1 = privateStringMethod.invoke(repository, BigDecimal("0.5"), ) as Boolean
        Truth.assertThat(case1).isTrue()

        val case2 = privateStringMethod.invoke(repository, BigDecimal("1.5"),) as Boolean
        Truth.assertThat(case2).isFalse()

        val case3 = privateStringMethod.invoke(repository, BigDecimal("1")) as Boolean
        Truth.assertThat(case3).isTrue()
    }

    @Test
    fun userHasEnoughNetworkCoinBalanceForGasTest() {
        //Given
        val etherAmount = BigInteger("1000000000000000000") // 1 BSC
        coEvery {
            web3j.ethGetBalance(any(), DefaultBlockParameterName.LATEST).send().getBalance()
        } returns etherAmount

        val privateStringMethod: Method =
            BSCRepository::class.java.getDeclaredMethod("userHasEnoughNetworkCoinBalanceForGas",  BigDecimal::class.java)
        privateStringMethod.isAccessible = true

        //Then
        val case1 = privateStringMethod.invoke(repository, BigDecimal("0.5")) as Boolean
        Truth.assertThat(case1).isTrue()

        val case2 = privateStringMethod.invoke(repository, BigDecimal("1.5")) as Boolean
        Truth.assertThat(case2).isFalse()

        val case3 = privateStringMethod.invoke(repository, BigDecimal("1")) as Boolean
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