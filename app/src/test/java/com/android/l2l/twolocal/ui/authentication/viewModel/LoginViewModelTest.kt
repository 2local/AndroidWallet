package com.android.l2l.twolocal.ui.authentication.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.android.l2l.twolocal.dataSourse.local.prefs.UserSession
import com.android.l2l.twolocal.dataSourse.remote.auth.AuthenticationApiInterface
import com.android.l2l.twolocal.dataSourse.repository.auth.AuthenticationRepository
import com.android.l2l.twolocal.dataSourse.utils.ViewState
import com.android.l2l.twolocal.model.ProfileInfo
import com.android.l2l.twolocal.model.output.LoginOutput
import com.android.l2l.twolocal.model.response.base.ApiSingleResponse
import com.android.l2l.twolocal.ui.authentication.viewModel.formState.LoginFormState
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
import org.mockito.Mockito
import java.util.concurrent.Callable

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class LoginViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var repository: AuthenticationRepository
    private lateinit var viewModel: LoginViewModel

    //    private lateinit var authenticationRemoteDataSource: AuthenticationRemoteDataSource
//    private lateinit var authenticationLocalDataSource: AuthenticationLocalDataSource
    private lateinit var authenticationApi: AuthenticationApiInterface
    private lateinit var appPreferences: UserSession
//    private lateinit var session: Session

    @Before
    fun setup() {
        appPreferences = Mockito.mock(UserSession::class.java)
        authenticationApi = Mockito.mock(AuthenticationApiInterface::class.java)
        repository = Mockito.mock(AuthenticationRepository::class.java)
        viewModel = LoginViewModel(repository, appPreferences)

        RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler: Callable<Scheduler?>? -> Schedulers.trampoline() }
    }

    @Test
    fun loginInvalidPassword() {
        //GIVEN
        val loginOutput = LoginOutput("sample@gmail.com", "1234567")

        //WHEN
        viewModel.loginUser(loginOutput)

        //THEN
        viewModel.loginFormState.observeForever() {
            Truth.assertThat(it is LoginFormState.PasswordError)
        }
    }

    @Test
    fun loginValidAllInputs() {
        //GIVEN
        val username = "sample@gmail.com"
        val password = "12345678"

        //WHEN
        viewModel.loginDataChanged(username, password)

        //THEN
        viewModel.loginFormState.observeForever {
            Truth.assertThat(it is LoginFormState.IsDataValid)
        }

//        val result= viewModel.loginFormState.getOrAwaitValueTest()
//        assertThat(result).isInstanceOf(LoginFormState.PasswordError::class.java)

    }

    @Test
    fun loginInvalidEmail() {
        //GIVEN
        val username = "samplegmail.com"
        val password = "12345678"

        //WHEN
        viewModel.loginDataChanged(username, password)

        //THEN
        viewModel.loginFormState.observeForever {
            Truth.assertThat(it is LoginFormState.UsernameError)
        }

    }

    @Test
    fun loginWithUnSuccessResponseCode() {
        //GIVEN
        val loginOutput = LoginOutput("sample@gmail.com", "1234567")
        val res = ApiSingleResponse<ProfileInfo>("", 500)

        //WHEN
        Mockito.`when`(repository.login(loginOutput)).thenReturn(Single.just(res))
        viewModel.loginUserApiRequest(loginOutput)

        //THEN
        viewModel.loginLiveData.observeForever() {
            Truth.assertThat(it is ViewState.Error)
            if(it is ViewState.Error)
                Truth.assertThat((it).error).isNotNull()
        }
    }


    @Test
    fun loginWithSuccessResponseCode() {
        //GIVEN
        val loginOutput = LoginOutput("sample@gmail.com", "1234567")
        val profileInfo = Mockito.mock(ProfileInfo::class.java)
        val res = ApiSingleResponse<ProfileInfo>("", 200)
        res.record = profileInfo

        //WHEN
        Mockito.`when`(repository.login(loginOutput)).thenReturn(Single.just(res))
        viewModel.loginUserApiRequest(loginOutput)

        //THEN
        viewModel.loginLiveData.observeForever() {
            Truth.assertThat(it is ViewState.Success)
        }

    }

    @Test
    fun loginWithSuccessResponseCode2() {
        //GIVEN

        //WHEN
        viewModel.loginBiometric()

        //THEN
        viewModel.loginLiveData.observeForever() {
            Truth.assertThat(it is ViewState.Error)
        }

    }


}