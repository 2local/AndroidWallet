package com.android.l2l.twolocal.ui.authentication.securityPassword.viewmodel

/**
 * Data validation state of the login form.
 */

sealed class SecurityFormState {
    data class IsDataValid(val isValid: Boolean) : SecurityFormState()
    data class PasswordError(val error: Int) : SecurityFormState()
    data class ConfirmPasswordError(val error: Int) : SecurityFormState()

}