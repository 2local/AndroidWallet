package com.android.l2l.twolocal.ui.wallet.detail.rename

/**
 * Data validation state of the login form.
 */

sealed class RenameFormState {
    data class IsDataValid(val isValid: Boolean) : RenameFormState()
    data class NameError(val error: Int) : RenameFormState()

}