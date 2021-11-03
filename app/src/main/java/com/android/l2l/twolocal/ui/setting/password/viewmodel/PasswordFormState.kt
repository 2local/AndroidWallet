package com.android.l2l.twolocal.ui.setting.password.viewmodel

/**
 * Data validation state of the login form.
 */

sealed class PasswordFormState {
    data class IsDataValid(val isValid: Boolean) : PasswordFormState()
    data class PasswordError(val error: Int) : PasswordFormState()
    data class RepeatPasswordError(val error: Int) : PasswordFormState()

}