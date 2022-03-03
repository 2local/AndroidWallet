package com.android.l2l.twolocal.common

import android.app.Activity
import android.content.Context
import com.android.l2l.twolocal.App
import com.android.l2l.twolocal.dataSourse.utils.error.GeneralError
import com.android.l2l.twolocal.di.component.AppComponent
import com.android.l2l.twolocal.ui.base.BaseViewActions
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
fun Activity.findAppComponent(): AppComponent {
    return (application as App).getAppComponent()
}

fun Context.onMessageToast(message: String?) {
    if (message != null)
        BaseViewActions.onMessageToast(context = this, message)
}

fun Context.onMessageToast(message: Int?) {
    if (message != null)
        onMessageToast(getString(message))
}

fun Context.onMessageToast(error: GeneralError?) {
    if (error != null)
        BaseViewActions.onMessageToast(context = this, error)
}

fun Context.onMessageSnackbar(error: GeneralError?) {
    if (error != null)
        BaseViewActions.onMessageSnackbar(context = this, error)
}

fun Context.onMessageSnackbar(message: Int?) {
    if (message != null)
        BaseViewActions.onMessageSnackbar(context = this, message)
}

fun Context.onMessageSnackbar(message: String?) {
    if (message != null)
        BaseViewActions.onMessageSnackbar(context = this, message)
}

fun Context.onErrorDialog(error: GeneralError?) {
    if (error != null)
        BaseViewActions.onErrorDialog(context = this, error)
}

fun Context.onErrorDialog(message: String?) {
    if (message != null)
        BaseViewActions.onErrorDialog(context = this, message)
}

fun Context.onSuccessDialog(message: String?) {
    if (message != null)
        BaseViewActions.onSuccessDialog(context = this, message)
}