package com.android.l2l.twolocal.ui.setting.contacts


sealed class ContactFormState {
    data class IsDataValid(val isValid: Boolean) : ContactFormState()
    data class NameError(val error: Int) : ContactFormState()
    data class AddressError(val error: Int) : ContactFormState()
    data class CurrencyError(val error: Int) : ContactFormState()

}