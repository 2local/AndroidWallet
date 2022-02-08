package com.android.l2l.twolocal.ui.setting

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.android.l2l.twolocal.dataSourse.local.prefs.UserSession
import com.android.l2l.twolocal.dataSourse.remote.auth.AuthenticationApiInterface
import com.android.l2l.twolocal.dataSourse.repository.profile.ProfileRepositoryHelper
import com.android.l2l.twolocal.dataSourse.utils.ViewState
import com.android.l2l.twolocal.model.response.base.ApiBaseResponse
import com.android.l2l.twolocal.ui.setting.profile.viewmodel.AccountInfoViewModel
import com.google.common.truth.Truth
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import java.util.concurrent.Callable

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class SettingViewModelTest{

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var repository: ProfileRepositoryHelper
    private lateinit var viewModel: SettingViewModel

    //    private lateinit var authenticationRemoteDataSource: AuthenticationRemoteDataSource
//    private lateinit var authenticationLocalDataSource: AuthenticationLocalDataSource
    private lateinit var authenticationApi: AuthenticationApiInterface
    private lateinit var appPreferences: UserSession
    //    private lateinit var session: Session


    @Before
    fun setup() {
        appPreferences = Mockito.mock(UserSession::class.java)
        authenticationApi = Mockito.mock(AuthenticationApiInterface::class.java)
        repository = Mockito.mock(ProfileRepositoryHelper::class.java)
        viewModel = SettingViewModel(repository, appPreferences)


        RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler: Callable<Scheduler?>? -> Schedulers.trampoline() }
    }


    @Test
    fun `sign out app`() {
        //GIVEN
        val res = ApiBaseResponse(200, "")

        //WHEN
        Mockito.`when`(repository.resetWalletAndSignOut()).thenReturn(Single.just(true))
        viewModel.resetWalletAndSignOut()

        //THEN
        viewModel.logoutLiveData.observeForever() {
            Truth.assertThat(it is ViewState.Success)
        }
    }

}