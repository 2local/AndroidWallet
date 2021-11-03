package com.android.l2l.twolocal.ui.authentication.securityPassword.viewmodel;

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.l2l.twolocal.R
import com.android.l2l.twolocal.common.withIO
import com.android.l2l.twolocal.dataSourse.local.prefs.UserSessionHelper
import com.android.l2l.twolocal.dataSourse.repository.profile.ProfileRepositoryHelper
import com.android.l2l.twolocal.dataSourse.utils.ViewState
import com.android.l2l.twolocal.dataSourse.utils.error.GeneralError
import com.android.l2l.twolocal.dataSourse.utils.error.withError
import com.android.l2l.twolocal.ui.base.BaseViewModel
import com.android.l2l.twolocal.utils.InputValidationRegex
import com.android.l2l.twolocal.utils.SecurityUtils
import com.android.l2l.twolocal.utils.constants.AppConstants
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
@SuppressLint("CheckResult")
class UnlockViewModel
@Inject constructor(
    private val profileRepository: ProfileRepositoryHelper,
    private val appPreferencesHelper: UserSessionHelper
) : BaseViewModel() {


    private val _unlockAppForm = MutableLiveData<SecurityFormState>()
    val unlockAppState: LiveData<SecurityFormState>
        get() = _unlockAppForm

    private val _resetWalletLiveData = MutableLiveData<ViewState<Boolean>>()
    val resetWalletLiveData: LiveData<ViewState<Boolean>>
        get() = _resetWalletLiveData


    fun isTouchIDActive(): Boolean {
        return appPreferencesHelper.isActiveLoginTouchID
    }

    fun shouldCreatePassword(): Boolean {
        val savedPassword = appPreferencesHelper.userLocalPassword
        return savedPassword.isNullOrEmpty()
    }


    fun isValidForm(password: String, context: Context) {
         createPasswordValidForm(password, context)
    }

    private fun validPassword(password: String, context: Context): Boolean {
        val securePassword = appPreferencesHelper.userLocalPassword
        val savedPassword = SecurityUtils.getSecureString(securePassword, AppConstants.LOCAL_PASS_ALICE, context)

        return password == savedPassword
    }


    fun resetWallet() {
        profileRepository.resetWalletAndSignOut().withIO()
            .doOnSubscribe {
                addToDisposable(it)
                _resetWalletLiveData.value = ViewState.Loading
            }
            .doOnSuccess { _resetWalletLiveData.value = ViewState.Success(it) }
            .doOnError { _resetWalletLiveData.value = ViewState.Error(GeneralError().withError(it)) }
            .subscribe({

            }, { })
    }

    private fun createPasswordValidForm(password: String, context: Context): SecurityFormState {
        var formState: SecurityFormState = SecurityFormState.IsDataValid(true)
        if (password.isBlank()) {
            formState = SecurityFormState.PasswordError(R.string.error_empty_input)
        } else if (!InputValidationRegex.isValidPassword(password)) {
            formState = SecurityFormState.PasswordError(R.string.invalid_password)
        } else if (!validPassword(password, context)) {
            formState = SecurityFormState.PasswordError(R.string.invalid_local_password)
        }

        _unlockAppForm.value = formState
        return formState
    }

    override fun onCleared() {
        super.onCleared()
    }

}