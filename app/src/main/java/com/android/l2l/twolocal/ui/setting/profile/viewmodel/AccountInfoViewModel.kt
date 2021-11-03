package com.android.l2l.twolocal.ui.setting.profile.viewmodel;

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
import com.android.l2l.twolocal.utils.InputValidationRegex.isValidateMobile
import com.android.l2l.twolocal.utils.InputValidationRegex.isValidateUsername
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import javax.inject.Inject

@ExperimentalCoroutinesApi
@SuppressLint("CheckResult")
class AccountInfoViewModel
@Inject constructor(
    private val profileRepository: ProfileRepositoryHelper,
) : BaseViewModel() {


    private val _accountForm = MutableLiveData<AccountInfoFormState>()
    val accountFormState: LiveData<AccountInfoFormState>
        get() = _accountForm

    private val _updateInfoLiveData = MutableLiveData<ViewState<ApiBaseResponse>>()
    val updateInfoLiveData: LiveData<ViewState<ApiBaseResponse>>
        get() = _updateInfoLiveData

    private val _profileInfoLiveData = MutableLiveData<ViewState<ProfileInfo>>()
    val profileInfoLiveData: LiveData<ViewState<ProfileInfo>>
        get() = _profileInfoLiveData


    fun updateProfile(profileInfo: ProfileInfo) {
        val loginEnable = registerDataChanged(profileInfo)
        if (loginEnable is AccountInfoFormState.IsDataValid) {
            if (loginEnable.isValid)
                updateProfileApiRequest(profileInfo)
        }
    }

    private fun updateProfileApiRequest(profileInfo: ProfileInfo) {
        profileRepository.updateProfile(profileInfo).withIO()
            .doOnSubscribe {
                addToDisposable(it)
                _updateInfoLiveData.value = ViewState.Loading
            }
            .doOnError { _updateInfoLiveData.value = ViewState.Error(GeneralError().withError(messageRes = R.string.unknownError)) }
            .subscribe({
                if(it.code == 200)
                _updateInfoLiveData.value = ViewState.Success(it)
                else
                _updateInfoLiveData.value = ViewState.Error(GeneralError().withError(message = it.message))
            }, { })

    }

    fun getProfile() {
//        _profileInfoLiveData.value = ViewState.Success(userSession.profileInfo)
        profileRepository.profile().withIO()
            .doOnSubscribe {
                addToDisposable(it)
                _profileInfoLiveData.value = ViewState.Loading
            }
            .doOnError { _profileInfoLiveData.value = ViewState.Error(GeneralError().withError(it)) }
            .subscribe({
                _profileInfoLiveData.value = ViewState.Success(it.record)
            }, { })

    }


    fun registerDataChanged(profileInfo: ProfileInfo): AccountInfoFormState {
        var formState: AccountInfoFormState = AccountInfoFormState.IsDataValid(true)
        if (profileInfo.name.isNullOrBlank()) {
            formState = AccountInfoFormState.UsernameError(R.string.error_empty_input)
            _accountForm.value = formState
        } else if (!isValidateUsername(profileInfo.name)) {
            formState = AccountInfoFormState.UsernameError(R.string.invalid_username)
            _accountForm.value = formState
        }

        if (profileInfo.mobile_number.isNullOrBlank()) {
            formState = AccountInfoFormState.PhoneError(R.string.error_empty_input)
            _accountForm.value = formState
        } else if (!isValidateMobile(profileInfo.mobile_number)) {
            formState = AccountInfoFormState.PhoneError(R.string.invalid_mobile)
            _accountForm.value = formState
        }

        if(_accountForm.value == null){
            _accountForm.value = formState
        }
        return formState
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}