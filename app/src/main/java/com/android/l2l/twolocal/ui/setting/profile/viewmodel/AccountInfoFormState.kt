package com.android.l2l.twolocal.ui.setting.profile.viewmodel

/**
 * Data validation state of the login form.
 */

sealed class AccountInfoFormState {
    data class IsDataValid(val isValid: Boolean) : AccountInfoFormState()
    data class UsernameError(val error: Int) : AccountInfoFormState()
    data class PhoneError(val error: Int) : AccountInfoFormState()

}