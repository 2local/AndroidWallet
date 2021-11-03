package com.android.l2l.twolocal.ui.wallet.send.viewmodel

/**
 * Data validation state of the login form.
 */

sealed class SendFormState {
    data class IsDataValid(val isValid: Boolean) : SendFormState()
    data class AddressError(val error: Int) : SendFormState()
    data class AmountError(val error: Int) : SendFormState()

}