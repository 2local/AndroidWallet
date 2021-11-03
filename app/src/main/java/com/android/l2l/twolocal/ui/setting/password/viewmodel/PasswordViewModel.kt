package com.android.l2l.twolocal.ui.setting.password.viewmodel;

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.l2l.twolocal.R
import com.android.l2l.twolocal.common.withIO
import com.android.l2l.twolocal.dataSourse.local.prefs.UserSession
import com.android.l2l.twolocal.dataSourse.repository.profile.ProfileRepositoryHelper
import com.android.l2l.twolocal.dataSourse.utils.ViewState
import com.android.l2l.twolocal.dataSourse.utils.error.GeneralError
import com.android.l2l.twolocal.dataSourse.utils.error.withError
import com.android.l2l.twolocal.model.ProfileInfo
import com.android.l2l.twolocal.model.response.base.ApiBaseResponse
import com.android.l2l.twolocal.ui.base.BaseViewModel
import com.android.l2l.twolocal.utils.InputValidationRegex.isValidatePassword
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import javax.inject.Inject

@ExperimentalCoroutinesApi
@SuppressLint("CheckResult")
class PasswordViewModel
@Inject constructor(
    private val profileRepository: ProfileRepositoryHelper,
    private val userSession: UserSession,
) : BaseViewModel() {


    private val _accountForm = MutableLiveData<PasswordFormState>()
    val accountFormState: LiveData<PasswordFormState>
        get() = _accountForm

    private val _updateInfoLiveData = MutableLiveData<ViewState<ApiBaseResponse>>()
    val updateInfoLiveData: LiveData<ViewState<ApiBaseResponse>>
        get() = _updateInfoLiveData


    fun updatePassword(password: String, repeatPassword: String) {
        val loginEnable = registerDataChanged(password, repeatPassword)
        if (loginEnable is PasswordFormState.IsDataValid) {
            if (loginEnable.isValid)
                updatePasswordApiRequest(password)
        }
    }

    private fun updatePasswordApiRequest(password: String) {
        val updateProfile = makeProfileUpdateObject(password)
        profileRepository.changePassword(updateProfile).withIO()
            .doOnSubscribe {
                addToDisposable(it)
                _updateInfoLiveData.value = ViewState.Loading
            }
            .doOnError { _updateInfoLiveData.value = ViewState.Error(GeneralError().withError(it)) }
            .subscribe({
                _updateInfoLiveData.value = ViewState.Success(it)
            }, { })

    }

    private fun makeProfileUpdateObject(password: String): ProfileInfo{
        val profileInfo = userSession.profileInfo
        val updateProfile = ProfileInfo()
        updateProfile.user_Id = profileInfo.user_Id
        updateProfile.name = profileInfo.name
        updateProfile.email = profileInfo.email
        updateProfile.mobile_number = profileInfo.mobile_number
//        profileInfo.email = profileInfo.email
//        updateProfile.password = password
        return updateProfile
    }

    private fun registerDataChanged(password: String, repeatPassword: String): PasswordFormState {
        var formState: PasswordFormState = PasswordFormState.IsDataValid(true)
        if (password.isBlank()) {
            formState = PasswordFormState.PasswordError(R.string.error_empty_input)
            _accountForm.value = formState
        } else if (!isValidatePassword(password)) {
            formState = PasswordFormState.PasswordError(R.string.invalid_password)
            _accountForm.value = formState
        }

        if (password.isBlank()) {
            formState = PasswordFormState.RepeatPasswordError(R.string.error_empty_input)
            _accountForm.value = formState
        } else if (password != repeatPassword) {
            formState = PasswordFormState.RepeatPasswordError(R.string.invalid_password_repeat)
            _accountForm.value = formState
        }

        return formState
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}