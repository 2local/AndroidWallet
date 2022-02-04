package com.android.l2l.twolocal.dataSourse.repository.profile

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.android.l2l.twolocal.dataSourse.remote.profile.ProfileApiInterface
import com.android.l2l.twolocal.dataSourse.repository.MockWebServerBaseTest
import com.android.l2l.twolocal.model.ProfileInfo
import com.android.l2l.twolocal.model.response.base.ApiBaseResponse
import com.android.l2l.twolocal.model.response.base.ApiSingleResponse
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import java.net.HttpURLConnection

class ProfileRepositoryTest : MockWebServerBaseTest() {

    override fun isMockServerEnabled() = true

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var context: Context

    private lateinit var authenticationApi: ProfileApiInterface


    @Before
    fun start() {
//        context = InstrumentationRegistry.getInstrumentation().targetContext
        authenticationApi = provideTestApiService(ProfileApiInterface::class.java)
    }


    @Test
    fun getProfileApiTest() {
        //GIVEN
        mockHttpResponse("json/profile_response.json", HttpURLConnection.HTTP_OK)

        //WHEN
        val testSingle = TestObserver<ApiSingleResponse<ProfileInfo>>()
        authenticationApi.getProfileData("55369").subscribe(testSingle)

        //THEN
        testSingle.await().assertValue {
            it.record != null
        }.assertValue {
            it.code == 200
        }.assertValue {
            it.record.user_Id == 55369L
        }
        testSingle.assertComplete()
        testSingle.assertNoErrors()
    }

    @Test
    fun changePasswordApiTest() {
        //GIVEN
        val updateProfile = ProfileInfo()
        updateProfile.user_Id = 55369
        updateProfile.name = "erfaneghterafi1"
        updateProfile.email = "erfan.eghterafi@gmail.com"
        updateProfile.mobile_number = "9138970886"

        mockHttpResponse("json/change_password_response.json", HttpURLConnection.HTTP_OK)

        //WHEN
        val testSingle = TestObserver<ApiBaseResponse>()
        authenticationApi.changePassword(updateProfile).subscribe(testSingle)

        //THEN
        testSingle.await().assertValue {
            it.code == 200
        }
        testSingle.assertComplete()
        testSingle.assertNoErrors()
    }

    @Test
    fun updateProfileApiTest() {
        //GIVEN
        val updateProfile = ProfileInfo()
        updateProfile.user_Id = 55369
        updateProfile.name = "erfaneghterafi1"
        updateProfile.email = "erfan.eghterafi@gmail.com"
        updateProfile.mobile_number = "9138970886"
        mockHttpResponse("json/update_profile_response.json", HttpURLConnection.HTTP_OK)

        //WHEN
        val testSingle = TestObserver<ApiBaseResponse>()
        authenticationApi.updateProfile(updateProfile).subscribe(testSingle)

        //THEN
        testSingle.await().assertValue {
            it.code == 200
        }
        testSingle.assertComplete()
        testSingle.assertNoErrors()
    }
}