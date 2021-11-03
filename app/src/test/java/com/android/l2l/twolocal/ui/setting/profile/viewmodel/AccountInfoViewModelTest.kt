package com.android.l2l.twolocal.ui.setting.profile.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.android.l2l.twolocal.dataSourse.local.prefs.UserSession
import com.android.l2l.twolocal.dataSourse.remote.auth.AuthenticationApiInterface
import com.android.l2l.twolocal.dataSourse.repository.profile.ProfileRepositoryHelper
import com.android.l2l.twolocal.dataSourse.utils.ViewState
import com.android.l2l.twolocal.model.UpdateProfile
import com.android.l2l.twolocal.model.response.base.ApiBaseResponse
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
class AccountInfoViewModelTest{

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var repository: ProfileRepositoryHelper
    private lateinit var viewModel: AccountInfoViewModel

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
        viewModel = AccountInfoViewModel(repository, appPreferences)


        RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler: Callable<Scheduler?>? -> Schedulers.trampoline() }
    }

    @Test
    fun `update profile empty username inputs return error response`() {
        //GIVEN
        val updateProfile = UpdateProfile()
        //WHEN
        viewModel.updateProfile(updateProfile)

        //THEN
        viewModel.accountFormState.observeForever() {
            Truth.assertThat(it is AccountInfoFormState.UsernameError)
        }
    }

    @Test
    fun `update profile invalid username inputs return error response`() {
        //GIVEN
        val updateProfile = UpdateProfile()
        updateProfile.name = "1sampleusername"
        updateProfile.mobile_number = "9138970886"
        //WHEN
        viewModel.updateProfile(updateProfile)

        //THEN
        viewModel.accountFormState.observeForever() {
            Truth.assertThat(it is AccountInfoFormState.UsernameError)
        }
    }

    @Test
    fun `update profile empty mobile inputs return error response`() {
        //GIVEN
        val updateProfile = UpdateProfile()
        updateProfile.name = "sampleusername"
        //WHEN
        viewModel.updateProfile(updateProfile)

        //THEN
        viewModel.accountFormState.observeForever() {
            Truth.assertThat(it is AccountInfoFormState.UsernameError)
        }
    }

    @Test
    fun `update profile valid inputs return valid response`() {
        //GIVEN
        val updateProfile = UpdateProfile()
        updateProfile.name = "sampleusername"
        updateProfile.mobile_number = "9138970886"
        //WHEN
        viewModel.registerDataChanged(updateProfile)

        //THEN
        viewModel.accountFormState.observeForever() {
            Truth.assertThat(it is AccountInfoFormState.IsDataValid)
        }
    }

    @Test
    fun `update profile with valid inputs return success`() {
        //GIVEN
        val res = ApiBaseResponse(200, "")
        val updateProfile = UpdateProfile()
        updateProfile.name = "sampleusername"
        updateProfile.mobile_number = "9138970886"

        //WHEN
        Mockito.`when`(repository.updateProfile(updateProfile)).thenReturn(Single.just(res))
        viewModel.updateProfile(updateProfile)

        //THEN
        viewModel.updateInfoLiveData.observeForever() {
            Truth.assertThat(it is ViewState.Success)
        }
    }

    @Test
    fun `update profile with valid inputs return error response`() {
        //GIVEN
        val res = ApiBaseResponse(500, "")
        val updateProfile = UpdateProfile()
        updateProfile.name = "sampleusername"
        updateProfile.mobile_number = "9138970886"

        //WHEN
        Mockito.`when`(repository.updateProfile(updateProfile)).thenReturn(Single.just(res))
        viewModel.updateProfile(updateProfile)

        //THEN
        viewModel.updateInfoLiveData.observeForever() {
            Truth.assertThat(it is ViewState.Error)
            if(it is ViewState.Error)
                Truth.assertThat((it).error).isNotNull()
        }
    }

}