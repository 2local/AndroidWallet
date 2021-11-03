package com.android.l2l.twolocal.ui.setting.localPassword.viewModel;

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
class ChangeLocalPasswordViewModel
@Inject constructor(
    private val appPreferencesHelper: UserSessionHelper
) : BaseViewModel() {


    private val _createPasswordForm = MutableLiveData<ChangePasswordFormState>()
    val createPasswordFormState: LiveData<ChangePasswordFormState>
        get() = _createPasswordForm


    fun setLocalPassword(currentPassword: String, newPassword: String, confirmPassword: String, context: Context) {
        val loginEnable = createPasswordValidForm(currentPassword,newPassword, confirmPassword, context)
        if (loginEnable is ChangePasswordFormState.IsDataValid) {
            if (loginEnable.isValid)
                savePassword(newPassword, context)
        }
    }

    private fun savePassword(password: String, context: Context) {
        val securePassword = SecurityUtils.saveSecureString(password, AppConstants.LOCAL_PASS_ALICE, context)
        appPreferencesHelper.userLocalPassword = securePassword
    }

    private fun validCurrentPassword(password: String, context: Context): Boolean {
        val securePassword = appPreferencesHelper.userLocalPassword
        val savedPassword = SecurityUtils.getSecureString(securePassword, AppConstants.LOCAL_PASS_ALICE, context)

        return password == savedPassword
    }

    private fun createPasswordValidForm(currentPassword: String, newPassword: String, confirmPassword: String, context: Context): ChangePasswordFormState {
        var formState: ChangePasswordFormState = ChangePasswordFormState.IsDataValid(true)
        if (!validCurrentPassword(currentPassword, context)) {
            formState = ChangePasswordFormState.CurrentPasswordError(R.string.fragment_change_local_password_current_pass_incorrect_error)
        }else if (newPassword.isBlank()) {
            formState = ChangePasswordFormState.NewPasswordError(R.string.error_empty_input)
        } else if (!InputValidationRegex.isValidPassword(newPassword)) {
            formState = ChangePasswordFormState.NewPasswordError(R.string.invalid_password)
        } else if (confirmPassword != newPassword) {
            formState = ChangePasswordFormState.ConfirmPasswordError(R.string.invalid_password_repeat)
        }

        _createPasswordForm.value = formState
        return formState
    }

    override fun onCleared() {
        super.onCleared()
    }

}