package com.android.l2l.twolocal.ui.authentication.viewModel

import android.content.Context
import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.android.l2l.twolocal.dataSourse.local.prefs.UserSession
import com.android.l2l.twolocal.dataSourse.remote.auth.AuthenticationApiInterface
import com.android.l2l.twolocal.dataSourse.repository.auth.AuthenticationRepository
import com.android.l2l.twolocal.dataSourse.utils.ViewState
import com.android.l2l.twolocal.model.ProfileInfo
import com.android.l2l.twolocal.model.TwoFAVerify
import com.android.l2l.twolocal.model.response.base.ApiSingleResponse
import com.android.l2l.twolocal.ui.authentication.viewModel.formState.TwoFAFormState
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
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito
import java.util.concurrent.Callable


@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class TwoFactorViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var repository: AuthenticationRepository
    private lateinit var viewModel: TwoFactorViewModel

    //    private lateinit var authenticationRemoteDataSource: AuthenticationRemoteDataSource
//    private lateinit var authenticationLocalDataSource: AuthenticationLocalDataSource
    private lateinit var authenticationApi: AuthenticationApiInterface
    private lateinit var appPreferences: UserSession

    //    private lateinit var session: Session
    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var context: Context

    @Before
    fun setup() {
        appPreferences = Mockito.mock(UserSession::class.java)
        authenticationApi = Mockito.mock(AuthenticationApiInterface::class.java)
        repository = Mockito.mock(AuthenticationRepository::class.java)
        viewModel = TwoFactorViewModel(repository, appPreferences)


        sharedPrefs = Mockito.mock(SharedPreferences::class.java)
        context = Mockito.mock(Context::class.java)
        Mockito.`when`(context.getSharedPreferences(anyString(), anyInt())).thenReturn(sharedPrefs)

        RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler: Callable<Scheduler?>? -> Schedulers.trampoline() }
    }

    @Test
    fun `2fa invalid code`() {
        //GIVEN

        //WHEN
        viewModel.loginDataChanged("")

        //THEN
        viewModel.loginFormState.observeForever() {
            Truth.assertThat(it is TwoFAFormState.InvalidCode)
        }
    }

    @Test
    fun `2fa valid code`() {
        //GIVEN
        val code: String = "123456"
        //WHEN
        viewModel.loginDataChanged(code)

        //THEN
        viewModel.loginFormState.observeForever() {
            Truth.assertThat(it is TwoFAFormState.IsDataValid)
        }
    }

    @Test
    fun `verify 2fa code with success response`() {
        //GIVEN
        val res = ApiSingleResponse<TwoFAVerify>("", 200)
        val isValid = TwoFAVerify()
        isValid.isValid = true
        res.record = isValid

        val profileInfo = ProfileInfo()
        profileInfo.user_Id = 1L
        Mockito.`when`(appPreferences.profileInfo).thenReturn(profileInfo)

        //WHEN
        Mockito.`when`(repository.verify2Fa(anyString(), anyString())).thenReturn(Single.just(res))
        viewModel.verify("123456")

        //THEN
        viewModel.loginLiveData.observeForever() {
            Truth.assertThat(it is ViewState.Success)
        }
    }
}