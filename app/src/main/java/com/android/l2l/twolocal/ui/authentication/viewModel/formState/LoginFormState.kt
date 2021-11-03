package com.android.l2l.twolocal.ui.authentication.viewModel.formState

/**
 * Data validation state of the login form.
 */

sealed class LoginFormState {
    data class IsDataValid(val isValid: Boolean) : LoginFormState()
    data class UsernameError(val error: Int) : LoginFormState()
    data class EmailError(val error: Int) : LoginFormState()
    data class PasswordError(val error: Int) : LoginFormState()

}