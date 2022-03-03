package com.android.l2l.twolocal.common

import androidx.fragment.app.Fragment
import com.android.l2l.twolocal.dataSourse.utils.error.GeneralError

fun Fragment.onMessageToast(message: String?) {
    requireContext().onMessageToast(message)
}

fun Fragment.onMessageToast(message: Int?) {
    requireContext().onMessageToast(message)
}

fun Fragment.onMessageToast(error: GeneralError?) {
    requireContext().onMessageToast(error)
}

fun Fragment.onMessageSnackbar(message: Int?) {
    requireContext().onMessageSnackbar(message)
}

fun Fragment.onMessageSnackbar(error: GeneralError?) {
    requireContext().onMessageSnackbar(error)
}

fun Fragment.onMessageSnackbar(message: String?) {
    requireContext().onMessageSnackbar(message)
}

fun Fragment.onErrorDialog(error: GeneralError?) {
    requireContext().onErrorDialog(error)
}

fun Fragment.onErrorDialog(message: String?) {
    requireContext().onErrorDialog(message)
}

fun Fragment.onSuccessDialog(message: String?) {
    requireContext().onSuccessDialog(message)
}