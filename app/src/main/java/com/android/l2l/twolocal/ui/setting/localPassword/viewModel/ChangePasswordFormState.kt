package com.android.l2l.twolocal.ui.setting.localPassword.viewModel

/**
 * Data validation state of the change password form.
 */

sealed class ChangePasswordFormState {
    data class IsDataValid(val isValid: Boolean) : ChangePasswordFormState()
    data class CurrentPasswordError(val error: Int) : ChangePasswordFormState()
    data class NewPasswordError(val error: Int) : ChangePasswordFormState()
    data class ConfirmPasswordError(val error: Int) : ChangePasswordFormState()

}