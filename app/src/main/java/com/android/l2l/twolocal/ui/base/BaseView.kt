package com.android.l2l.twolocal.ui.base;

import com.android.l2l.twolocal.dataSourse.utils.error.GeneralError


interface BaseView {

    fun showLoading(message: String?)

    fun showLoading()

    fun hideLoading()

    fun unauthorizedUser(response: String?)

    fun onTimeout(throwable: Throwable?)

    fun onNetworkError(throwable: Throwable?)

    fun onErrorToast(error: GeneralError?)

    fun onMessageToast(message: String?)

}