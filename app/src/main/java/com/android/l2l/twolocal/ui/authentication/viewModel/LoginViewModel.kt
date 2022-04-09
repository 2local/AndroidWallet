package com.android.l2l.twolocal.ui.authentication.viewModel;

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.l2l.twolocal.R
import com.android.l2l.twolocal.coin.TwoLocalCoin
import com.android.l2l.twolocal.common.withIO
import com.android.l2l.twolocal.dataSourse.local.prefs.UserSessionHelper
import com.android.l2l.twolocal.dataSourse.repository.auth.AuthenticationRepositoryHelper
import com.android.l2l.twolocal.dataSourse.repository.wallet.WalletRepositoryHelper
import com.android.l2l.twolocal.dataSourse.utils.ViewState
import com.android.l2l.twolocal.dataSourse.utils.error.GeneralError
import com.android.l2l.twolocal.dataSourse.utils.error.withError
import com.android.l2l.twolocal.model.ProfileInfo
import com.android.l2l.twolocal.model.enums.CryptoCurrencyType
import com.android.l2l.twolocal.model.request.LoginRequest
import com.android.l2l.twolocal.ui.authentication.viewModel.formState.LoginFormState
import com.android.l2l.twolocal.ui.base.BaseViewModel
import com.android.l2l.twolocal.utils.InputValidationRegex.isValidPassword
import com.android.l2l.twolocal.utils.InputValidationRegex.isValidateEmail
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import javax.inject.Inject

@ExperimentalCoroutinesApi
@SuppressLint("CheckResult")
class LoginViewModel
@Inject constructor(
    private val authenticationRepository: AuthenticationRepositoryHelper,
    private val appPreferencesHelper: UserSessionHelper,
    private val walletRepository: WalletRepositoryHelper,
) : BaseViewModel() {


    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState>
        get() = _loginForm

    private val _loginLiveData = MutableLiveData<ViewState<ProfileInfo>>()
    val loginLiveData: LiveData<ViewState<ProfileInfo>>
        get() = _loginLiveData

    fun loginBiometric() {
        val apiToken = appPreferencesHelper.apiToken
        val profileInfo = appPreferencesHelper.profileInfo

        if (apiToken.isNullOrBlank())
            _loginLiveData.value = ViewState.Error(GeneralError().withError(messageRes = R.string.fragment_login_using_email_password))
        else if (profileInfo != null) {
            authenticationRepository.saveUserLoggedIn()
            _loginLiveData.value = ViewState.Success(profileInfo)
        }
    }

    fun loginUser(user: LoginRequest) {
        val loginEnable = loginDataChanged(user.username, user.password)
        if (loginEnable is LoginFormState.IsDataValid) {
            if (loginEnable.isValid)
                loginUserApiRequest(user)
        }
    }

    fun loginUserApiRequest(user: LoginRequest) {
        authenticationRepository.login(user).withIO()
            .doOnSubscribe {
                addToDisposable(it)
                _loginLiveData.value = ViewState.Loading
            }
            .doOnError { _loginLiveData.value = ViewState.Error(GeneralError().withError(messageRes = R.string.unknownError)) }
            .subscribe({
                if (it.code == 200) {
                    _loginLiveData.value = ViewState.Success(it.record)
                    authenticationRepository.storeUserInfo(it.record)
                    walletRepository.createTemporaryWallet(CryptoCurrencyType.TwoLC, it.record.wallet, TwoLocalCoin.TWOlc_WALLET_CONTRACT)
                    if (it.record != null && !it.record.twoFaIsActive())
                        authenticationRepository.saveUserLoggedIn()
                } else {
                    _loginLiveData.value = ViewState.Error(GeneralError().withError(message = it.message))
                }
            }, {
                it.printStackTrace()
            })

    }

    fun loginDataChanged(username: String, password: String): LoginFormState {
        var formState: LoginFormState = LoginFormState.IsDataValid(true)
        if (username.isBlank()) {
            formState = LoginFormState.UsernameError(R.string.error_empty_input)
            _loginForm.value = formState
        } else if (!isValidateEmail(username)) {
            formState = LoginFormState.UsernameError(R.string.invalid_email)
            _loginForm.value = formState
        }

        if (password.isBlank()) {
            formState = LoginFormState.PasswordError(R.string.error_empty_input)
            _loginForm.value = formState
        } else if (!isValidPassword(password)) {
            formState = LoginFormState.PasswordError(R.string.invalid_password)
            _loginForm.value = formState
        }

        return formState
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}