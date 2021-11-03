package com.android.l2l.twolocal.ui.market

import android.content.Context
import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.android.l2l.twolocal.dataSourse.local.prefs.UserSession
import com.android.l2l.twolocal.dataSourse.remote.auth.AuthenticationApiInterface
import com.android.l2l.twolocal.dataSourse.repository.auth.AuthenticationRepository
import com.android.l2l.twolocal.dataSourse.repository.market.MarketRepositoryHelper
import com.android.l2l.twolocal.dataSourse.utils.ViewState
import com.android.l2l.twolocal.model.Company
import com.android.l2l.twolocal.model.MarketPlace
import com.android.l2l.twolocal.model.ProfileInfo
import com.android.l2l.twolocal.model.TwoFAVerify
import com.android.l2l.twolocal.model.response.base.ApiSingleResponse
import com.android.l2l.twolocal.ui.authentication.viewModel.TwoFactorViewModel
import com.google.common.truth.Truth
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import java.util.concurrent.Callable

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class MarketListViewModelTest{

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var repository: MarketRepositoryHelper
    private lateinit var viewModel: MarketListViewModel

    //    private lateinit var authenticationRemoteDataSource: AuthenticationRemoteDataSource
//    private lateinit var authenticationLocalDataSource: AuthenticationLocalDataSource
    private lateinit var authenticationApi: AuthenticationApiInterface
    private lateinit var appPreferences: UserSession
    //    private lateinit var session: Session

    @Before
    fun setup() {
        appPreferences = Mockito.mock(UserSession::class.java)
        authenticationApi = Mockito.mock(AuthenticationApiInterface::class.java)
        repository = Mockito.mock(MarketRepositoryHelper::class.java)
        viewModel = MarketListViewModel(repository)


        RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler: Callable<Scheduler?>? -> Schedulers.trampoline() }
    }

    @Test
    fun `get list of marketplace return success response`() {
        //GIVEN
        val res = ApiSingleResponse<Company>("", 200)
        val companyList = mutableListOf<MarketPlace>()
        val marketPlace = MarketPlace()
        companyList.add(marketPlace)
        val company = Company(companyList)
        res.record = company

        //WHEN
        Mockito.`when`(repository.getMarketPlaces()).thenReturn(Single.just(res))
        viewModel.getMarketPlaces()

        //THEN
        viewModel.marketPlaceListLiveData.observeForever() {
            Truth.assertThat(it is ViewState.Success)
        }
    }

    @Test
    fun `get list of marketplace return success response with response size zero`() {
        //GIVEN
        val res = ApiSingleResponse<Company>("", 200)
        val companyList = mutableListOf<MarketPlace>()
        val company = Company(companyList)
        res.record = company

        //WHEN
        Mockito.`when`(repository.getMarketPlaces()).thenReturn(Single.just(res))
        viewModel.getMarketPlaces()

        //THEN
        viewModel.marketPlaceListLiveData.observeForever() {
            Truth.assertThat(it is ViewState.Success)
        }
    }

    @Test
    fun `get list of marketplace return error response`() {
        //GIVEN
        val res = ApiSingleResponse<Company>("", 500)
        val companyList = mutableListOf<MarketPlace>()
        val marketPlace = MarketPlace()
        companyList.add(marketPlace)
        val company = Company(companyList)
        res.record = company

        //WHEN
        Mockito.`when`(repository.getMarketPlaces()).thenReturn(Single.just(res))
        viewModel.getMarketPlaces()

        //THEN
        viewModel.marketPlaceListLiveData.observeForever() {
            Truth.assertThat(it is ViewState.Error)
        }
    }
}