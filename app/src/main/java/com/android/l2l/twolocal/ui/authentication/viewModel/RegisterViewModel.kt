package com.android.l2l.twolocal.ui.authentication.viewModel;

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.l2l.twolocal.R
import com.android.l2l.twolocal.common.withIO
import com.android.l2l.twolocal.dataSourse.repository.auth.AuthenticationRepositoryHelper
import com.android.l2l.twolocal.dataSourse.utils.ViewState
import com.android.l2l.twolocal.dataSourse.utils.error.GeneralError
import com.android.l2l.twolocal.dataSourse.utils.error.withError
import com.android.l2l.twolocal.model.ProfileInfo
import com.android.l2l.twolocal.model.request.RegisterRequest
import com.android.l2l.twolocal.ui.authentication.viewModel.formState.LoginFormState
import com.android.l2l.twolocal.ui.base.BaseViewModel
import com.android.l2l.twolocal.utils.CommonUtils
import com.android.l2l.twolocal.utils.PriceFormatUtils
import com.android.l2l.twolocal.utils.InputValidationRegex.isValidPassword
import com.android.l2l.twolocal.utils.InputValidationRegex.isValidateEmail
import com.android.l2l.twolocal.utils.InputValidationRegex.isValidateUsername
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import javax.inject.Inject

@ExperimentalCoroutinesApi
@SuppressLint("CheckResult")
class RegisterViewModel
@Inject constructor(
    private val authenticationRepository: AuthenticationRepositoryHelper,

    ) : BaseViewModel() {


    private val _registerForm = MutableLiveData<LoginFormState>()
    val registerFormState: LiveData<LoginFormState>
        get() = _registerForm

    private val _registerLiveData = MutableLiveData<ViewState<ProfileInfo>>()
    val registerLiveData: LiveData<ViewState<ProfileInfo>>
        get() = _registerLiveData


    fun registerUser(user: RegisterRequest) {
        val loginEnable = registerDataChanged(user.name, user.username, user.password)
        if (loginEnable is LoginFormState.IsDataValid && loginEnable.isValid) {
            registerUserApiRequest(user)
        }
    }

    private fun registerUserApiRequest(user: RegisterRequest) {
        user.name = CommonUtils.encodeBase64ToString(user.name)
        user.username = CommonUtils.encodeBase64ToString(user.username)
        user.password = CommonUtils.encodeBase64ToString(user.password)
        authenticationRepository.signUp(user).withIO()
            .doOnSubscribe {
                addToDisposable(it)
                _registerLiveData.value = ViewState.Loading
            }
            .doOnSuccess { _registerLiveData.value = ViewState.Success(it.record) }
            .doOnError { _registerLiveData.value = ViewState.Error(GeneralError().withError(it)) }
            .subscribe({

            }, { })

    }

    fun registerDataChanged(username: String, mail: String, password: String): LoginFormState {
        var formState: LoginFormState = LoginFormState.IsDataValid(true)
        if (username.isBlank()) {
            formState = LoginFormState.UsernameError(R.string.error_empty_input)
            _registerForm.value = formState
        } else if (!isValidateUsername(username)) {
            formState = LoginFormState.UsernameError(R.string.invalid_username)
            _registerForm.value = formState
        }

        if (mail.isBlank()) {
            formState = LoginFormState.EmailError(R.string.error_empty_input)
            _registerForm.value = formState
        } else if (!isValidateEmail(mail)) {
            formState = LoginFormState.EmailError(R.string.invalid_email)
            _registerForm.value = formState
        }

        if (password.isBlank()) {
            formState = LoginFormState.PasswordError(R.string.error_empty_input)
            _registerForm.value = formState
        } else if (!isValidPassword(password)) {
            formState = LoginFormState.PasswordError(R.string.invalid_password)
            _registerForm.value = formState
        }
        if (_registerForm.value == null)
            _registerForm.value = formState
        return formState
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}