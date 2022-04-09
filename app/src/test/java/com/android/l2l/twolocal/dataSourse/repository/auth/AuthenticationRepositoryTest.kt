package com.android.l2l.twolocal.dataSourse.repository.auth

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.android.l2l.twolocal.dataSourse.remote.auth.AuthenticationApiInterface
import com.android.l2l.twolocal.dataSourse.repository.MockWebServerBaseTest
import com.android.l2l.twolocal.model.ProfileInfo
import com.android.l2l.twolocal.model.TwoFAVerify
import com.android.l2l.twolocal.model.request.LoginRequest
import com.android.l2l.twolocal.model.request.RegisterRequest
import com.android.l2l.twolocal.model.response.base.ApiSingleResponse
import io.reactivex.observers.TestObserver
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import java.net.HttpURLConnection

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class AuthenticationRepositoryTest : MockWebServerBaseTest() {

    override fun isMockServerEnabled() = true

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var context: Context

//    lateinit var repository: AuthenticationRepository
//    private lateinit var authenticationRemoteDataSource: AuthenticationRemoteDataSource
//    private lateinit var authenticationLocalDataSource: AuthenticationLocalDataSource
    private lateinit var authenticationApi: AuthenticationApiInterface
//    private lateinit var appPreferences: UserSession
//    private lateinit var session: Session


    @Before
    fun start() {
//        context = InstrumentationRegistry.getInstrumentation().targetContext
        authenticationApi = provideTestApiService(AuthenticationApiInterface::class.java)
    }


    @Test
    fun loginApiTest() {
        //Given
        val loginOutput = LoginRequest("username", "password")
        mockHttpResponse("json/login_response.json", HttpURLConnection.HTTP_OK)

        val testSingle = TestObserver<ApiSingleResponse<ProfileInfo>>()
        authenticationApi.login(loginOutput).subscribe(testSingle)
        testSingle.await().assertValue {
            it.record.email == "erfan.eghterafi@gmail.com"
        }.assertValue {
            it.code == 200
        }
        testSingle.assertComplete()
        testSingle.assertNoErrors()
    }

    @Test
    fun sendSignUpTest() {
        val registerOutput =
            RegisterRequest("username1234", "erfan@gmail.com", "password")
        mockHttpResponse("json/register_response.json", HttpURLConnection.HTTP_OK)

        val testSingle = TestObserver<ApiSingleResponse<ProfileInfo>>()
        authenticationApi.signUp(registerOutput).subscribe(testSingle)
        testSingle.await().assertValue {
            it.record.email == "erfan.eg@gmail.com"
        }.assertValue {
            it.code == 200
        }
        testSingle.assertComplete()
        testSingle.assertNoErrors()
    }

    @Test
    fun verify2FaTest() {
        val body: HashMap<String, String> =  hashMapOf()
        body["code"] = "MjU3ODU3"
        body["user_id"] = "OTIzMDk\u003d"

        mockHttpResponse("json/verify_auth_response.json", HttpURLConnection.HTTP_OK)

        val testSingle = TestObserver<ApiSingleResponse<TwoFAVerify>>()

        authenticationApi.verify2Fa(body).subscribe(testSingle)
        testSingle.await().assertValue {
            it.record.isValid
        }.assertValue {
            it.code == 200
        }
        testSingle.assertComplete()
        testSingle.assertNoErrors()
    }

}