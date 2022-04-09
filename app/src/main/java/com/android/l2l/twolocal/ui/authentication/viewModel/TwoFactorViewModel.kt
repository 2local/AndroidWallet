package com.android.l2l.twolocal.ui.authentication.viewModel;

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.l2l.twolocal.R
import com.android.l2l.twolocal.common.withIO
import com.android.l2l.twolocal.dataSourse.local.prefs.UserSessionHelper
import com.android.l2l.twolocal.dataSourse.repository.auth.AuthenticationRepositoryHelper
import com.android.l2l.twolocal.dataSourse.utils.ViewState
import com.android.l2l.twolocal.dataSourse.utils.error.GeneralError
import com.android.l2l.twolocal.dataSourse.utils.error.withError
import com.android.l2l.twolocal.model.TwoFAVerify
import com.android.l2l.twolocal.ui.authentication.viewModel.formState.TwoFAFormState
import com.android.l2l.twolocal.ui.base.BaseViewModel
import com.android.l2l.twolocal.utils.InputValidationRegex.isValidateVerification
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import javax.inject.Inject

@ExperimentalCoroutinesApi
@SuppressLint("CheckResult")
class TwoFactorViewModel
@Inject constructor(
    private val authenticationRepository: AuthenticationRepositoryHelper,
    private val appPreferencesHelper: UserSessionHelper
) : BaseViewModel() {


    private val _loginForm = MutableLiveData<TwoFAFormState>()
    val loginFormState: LiveData<TwoFAFormState>
        get() = _loginForm

    private val _loginLiveData = MutableLiveData<ViewState<TwoFAVerify>>()
    val loginLiveData: LiveData<ViewState<TwoFAVerify>>
        get() = _loginLiveData


    fun verifyUser(code: String) {
        val loginEnable = loginDataChanged(code)
        if (loginEnable is TwoFAFormState.IsDataValid) {
            if (loginEnable.isValid)
                verify(code)
        }
    }

    fun verify(code: String) {
        val profileInfo = appPreferencesHelper.profileInfo
//        val codeBase64 = CommonUtils.encodeBase64ToString(code)
//        val userIDBase64 = CommonUtils.encodeBase64ToString(profileInfo.user_Id.toString())
        authenticationRepository.verify2Fa(code, profileInfo.user_Id.toString()).withIO()
            .doOnSubscribe {
                addToDisposable(it)
                _loginLiveData.value = ViewState.Loading
            }
            .doOnError { _loginLiveData.value = ViewState.Error(GeneralError().withError(messageRes = R.string.unknownError)) }
            .subscribe({
                if (it.record.isValid) {
                    authenticationRepository.saveUserLoggedIn()
                    _loginLiveData.value = ViewState.Success(it.record)
                }else
                    _loginLiveData.value = ViewState.Error(GeneralError().withError(messageRes = R.string.two_fa_invalid_code))
            }, {
                it.printStackTrace()
            })

    }

    fun loginDataChanged(code: String): TwoFAFormState {
        var formState: TwoFAFormState = TwoFAFormState.IsDataValid(true)
        if (code.isBlank()) {
            formState = TwoFAFormState.InvalidCode(R.string.error_empty_input)
            _loginForm.value = formState
        } else if (!isValidateVerification(code)) {
            formState = TwoFAFormState.InvalidCode(R.string.invalid_code)
            _loginForm.value = formState
        }

        if( _loginForm.value ==null)
            _loginForm.value = formState

        return formState
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}