//package com.android.l2l.twolocal.dataSourse.repository.auth
//
//import androidx.arch.core.executor.testing.InstantTaskExecutorRule
//import com.android.l2l.twolocal.dataSourse.local.prefs.UserSession
//import com.android.l2l.twolocal.dataSourse.remote.auth.AuthenticationApiInterface
//import com.android.l2l.twolocal.dataSourse.utils.ViewState
//import com.android.l2l.twolocal.model.ProfileInfo
//import com.android.l2l.twolocal.model.output.LoginOutput
//import com.android.l2l.twolocal.model.response.base.ApiSingleResponse
//import com.android.l2l.twolocal.ui.authentication.viewModel.formState.LoginFormState
//import com.android.l2l.twolocal.ui.authentication.viewModel.LoginViewModel
//import io.reactivex.Single
//import junit.framework.Assert.assertTrue
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//import org.junit.runner.RunWith
//import org.junit.runners.JUnit4
//import org.mockito.Mockito
//import org.mockito.Mockito.mock
//
//
//@ExperimentalCoroutinesApi
//@RunWith(JUnit4::class)
//class LoginViewModelTest  {
//    @get:Rule
//    var instantTaskExecutorRule = InstantTaskExecutorRule()
//
//    lateinit var repository: AuthenticationRepository
//    private lateinit var viewModel: LoginViewModel
////    private lateinit var authenticationRemoteDataSource: AuthenticationRemoteDataSource
////    private lateinit var authenticationLocalDataSource: AuthenticationLocalDataSource
//    private lateinit var authenticationApi: AuthenticationApiInterface
//    private lateinit var appPreferences: UserSession
////    private lateinit var session: Session
//
//    @Before
//    fun setup() {
//        appPreferences = mock(UserSession::class.java)
//        authenticationApi = mock(AuthenticationApiInterface::class.java)
//        repository =  mock(AuthenticationRepository::class.java)
//        viewModel = LoginViewModel(repository, appPreferences)
//
//    }
//
//    @Test
//    fun loginInvalidPassword() {
//        val loginOutput = LoginOutput("sample@gmail.com", "1234567")
//
//        viewModel.loginUser(loginOutput)
//        viewModel.loginFormState.observeForever() {
//            assertTrue(it is LoginFormState.PasswordError)
//        }
//    }
//
//    @Test
//    fun loginValidAll() {
//
//        viewModel.loginDataChanged("sample@gmail.com", "12345678")
//        viewModel.loginFormState.observeForever {
//            assertTrue(it is LoginFormState.IsDataValid)
//        }
//
////        val result= viewModel.loginFormState.getOrAwaitValueTest()
////        assertThat(result).isInstanceOf(LoginFormState.PasswordError::class.java)
//
//    }
//
//    @Test
//    fun loginInvalidEmail() {
//
//        viewModel.loginDataChanged("samplegmail.com", "12345678")
//        viewModel.loginFormState.observeForever {
//            assertTrue(it is LoginFormState.UsernameError)
//        }
//
//        viewModel.loginDataChanged("sample@gmail", "12345678")
//        viewModel.loginFormState.observeForever {
//            assertTrue(it is LoginFormState.UsernameError)
//        }
//
//    }
//
//    @Test
//    fun login() {
//        val loginOutput = LoginOutput("sample@gmail.com", "1234567")
//
//        Mockito.`when`(repository.login(loginOutput)).thenReturn(Single.just(ApiSingleResponse<ProfileInfo>("")))
//        viewModel.login(loginOutput)
//        viewModel.loginLiveData.observeForever() {
//            assertTrue(it is ViewState.Error)
//        }
//
//    }
//
//}