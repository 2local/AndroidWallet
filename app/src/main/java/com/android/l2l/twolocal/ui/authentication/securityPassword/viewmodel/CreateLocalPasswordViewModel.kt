package com.android.l2l.twolocal.ui.authentication.securityPassword.viewmodel;

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.l2l.twolocal.R
import com.android.l2l.twolocal.dataSourse.local.prefs.UserSessionHelper
import com.android.l2l.twolocal.ui.base.BaseViewModel
import com.android.l2l.twolocal.utils.InputValidationRegex
import com.android.l2l.twolocal.utils.SecurityUtils
import com.android.l2l.twolocal.utils.constants.AppConstants
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
@SuppressLint("CheckResult")
class CreateLocalPasswordViewModel
@Inject constructor(
    private val appPreferencesHelper: UserSessionHelper
) : BaseViewModel() {


    private val _createPasswordForm = MutableLiveData<SecurityFormState>()
    val createPasswordFormState: LiveData<SecurityFormState>
        get() = _createPasswordForm


    fun setTouchIDActive(isActive: Boolean) {
        appPreferencesHelper.isActiveLoginTouchID = isActive
    }

    private fun savePassword(password: String, context: Context) {
        val securePassword = SecurityUtils.saveSecureString(password, AppConstants.LOCAL_PASS_ALICE, context)
        appPreferencesHelper.userLocalPassword = securePassword

    }

    fun setLocalPassword(password: String, confirmPassword: String, context: Context) {
        val loginEnable = createPasswordValidForm(password, confirmPassword)
        if (loginEnable is SecurityFormState.IsDataValid) {
            if (loginEnable.isValid)
                savePassword(password, context)
        }
    }


    private fun createPasswordValidForm(password: String, confirmPassword: String): SecurityFormState {
        var formState: SecurityFormState = SecurityFormState.IsDataValid(true)
        if (password.isBlank()) {
            formState = SecurityFormState.PasswordError(R.string.error_empty_input)
        } else if (!InputValidationRegex.isValidPassword(password)) {
            formState = SecurityFormState.PasswordError(R.string.invalid_password)
        } else if (confirmPassword != password) {
            formState = SecurityFormState.ConfirmPasswordError(R.string.invalid_password_repeat)
        }

        _createPasswordForm.value = formState
        return formState
    }

    override fun onCleared() {
        super.onCleared()
    }

}