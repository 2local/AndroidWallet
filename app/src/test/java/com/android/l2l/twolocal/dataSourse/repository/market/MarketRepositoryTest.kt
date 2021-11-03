package com.android.l2l.twolocal.dataSourse.repository.market

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.android.l2l.twolocal.dataSourse.remote.market.MarketApiInterface
import com.android.l2l.twolocal.dataSourse.repository.MockWebServerBaseTest
import com.android.l2l.twolocal.model.Company
import com.android.l2l.twolocal.model.response.base.ApiSingleResponse
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import java.net.HttpURLConnection

class MarketRepositoryTest : MockWebServerBaseTest() {

    override fun isMockServerEnabled() = true

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var context: Context

    private lateinit var authenticationApi: MarketApiInterface


    @Before
    fun start() {
//        context = InstrumentationRegistry.getInstrumentation().targetContext
        authenticationApi = provideTestApiService(MarketApiInterface::class.java)
    }


    @Test
    fun loginApiTest() {
        //GIVEN
        mockHttpResponse("json/market_List_response.json", HttpURLConnection.HTTP_OK)

        //WHEN
        val testSingle = TestObserver<ApiSingleResponse<Company>>()
        authenticationApi.getMarketPlaces().subscribe(testSingle)

        //THEN
        testSingle.await().assertValue {
            it.record.companies.size == 1
        }.assertValue {
            it.code == 200
        }.assertValue {
            it.record.companies.get(0).id == 2
        }
        testSingle.assertComplete()
        testSingle.assertNoErrors()
    }
}