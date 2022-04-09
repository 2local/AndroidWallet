package com.android.l2l.twolocal.ui.authentication.viewModel

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.android.l2l.twolocal.dataSourse.local.AuthenticationLocalDataSource
import com.android.l2l.twolocal.dataSourse.local.prefs.Session
import com.android.l2l.twolocal.dataSourse.local.prefs.UserSession
import com.android.l2l.twolocal.dataSourse.remote.auth.AuthenticationApiInterface
import com.android.l2l.twolocal.dataSourse.remote.auth.AuthenticationRemoteDataSource
import com.android.l2l.twolocal.dataSourse.repository.auth.AuthenticationRepository
import com.android.l2l.twolocal.model.request.RegisterRequest
import com.android.l2l.twolocal.ui.authentication.viewModel.formState.LoginFormState
import com.android.l2l.twolocal.utils.getOrAwaitValueTest
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class RegisterViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    var context: Context? = null


    @Mock
    lateinit var repository: AuthenticationRepository

    @Mock
    private lateinit var viewModel: RegisterViewModel
    private lateinit var authenticationRemoteDataSource: AuthenticationRemoteDataSource
    private lateinit var authenticationLocalDataSource: AuthenticationLocalDataSource
    private lateinit var authenticationApi: AuthenticationApiInterface
    private lateinit var appPreferences: UserSession
    private lateinit var session: Session

    @Before
    fun setup() {
//        context = InstrumentationRegistry.getInstrumentation().targetContext
//        session = Session(context)
//        appPreferences = UserSession(session)
//        authenticationLocalDataSource = AuthenticationLocalDataSource(appPreferences)
//        authenticationApi = provideTestApiService(AuthenticationApiInterface::class.java)
//        authenticationRemoteDataSource = AuthenticationRemoteDataSource(authenticationApi)
//        repository = AuthenticationRepository(authenticationRemoteDataSource, authenticationLocalDataSource)
//        viewModel = RegisterViewModel(repository)

        appPreferences = Mockito.mock(UserSession::class.java)
        authenticationApi = Mockito.mock(AuthenticationApiInterface::class.java)
        repository = Mockito.mock(AuthenticationRepository::class.java)
        viewModel = RegisterViewModel(repository)
    }

    @Test
    fun loginInvalidPassword() {
        //GIVEN
        val loginOutput =
            RegisterRequest("zxcvbnm1", "sample@gmail.com", "1234567")

        //WHEN
        viewModel.registerUser(loginOutput)

        //THEN
        viewModel.registerFormState.observeForever() {
            Assert.assertTrue(it is LoginFormState.PasswordError)
        }
    }

    @Test
    fun loginValidAllInputs() {
        //GIVEN
        viewModel.registerDataChanged("zxcvbnm1", "sample@gmail.com", "12345678")

        //WHEN
        val result = viewModel.registerFormState.getOrAwaitValueTest()

        //THEN
        assertThat(result).isInstanceOf(LoginFormState.IsDataValid::class.java)

//        viewModel.loginFormState.observeForever {
//            Assert.assertTrue(it is LoginFormState.IsDataValid)
//        }
    }

    @Test
    fun loginInvalidEmail() {
        //GIVEN
        val loginOutput =
            RegisterRequest("zxcvbnm1", "samplegmail.com", "12345678")

        //WHEN
        viewModel.registerUser(loginOutput)
        val result = viewModel.registerFormState.getOrAwaitValueTest()

        //THEN
        assertThat(result).isInstanceOf(LoginFormState.EmailError::class.java)

//        viewModel.loginFormState.observeForever {
//            Assert.assertTrue(it is LoginFormState.EmailError)
//        }

    }

    @Test
    fun loginInvalidUsername() {
        //GIVEN
        val loginOutput =
            RegisterRequest("zxcvbnm", "sample@gmail.com", "12345678")

        //WHEN
        viewModel.registerUser(loginOutput)
        val result = viewModel.registerFormState.getOrAwaitValueTest()

        //THEN
        assertThat(result).isInstanceOf(LoginFormState.UsernameError::class.java)

//        viewModel.loginFormState.observeForever {
//            Assert.assertTrue(it is LoginFormState.UsernameError)
//        }
    }
}