package com.android.l2l.twolocal.ui.authentication.viewModel.formState


sealed class TwoFAFormState {
    data class IsDataValid(val isValid: Boolean) : TwoFAFormState()
    data class InvalidCode(val error: Int) : TwoFAFormState()

}