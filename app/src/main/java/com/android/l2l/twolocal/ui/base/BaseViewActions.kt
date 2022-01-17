package com.android.l2l.twolocal.ui.base;

import android.content.Context
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.android.l2l.twolocal.dataSourse.utils.error.GeneralError
import com.android.l2l.twolocal.utils.MessageUtils
import com.google.android.material.snackbar.Snackbar


class BaseViewActions private constructor() {

    private var mProgressDialog: SweetAlertDialog? = null
    private lateinit var context: Context

    companion object {
        fun getInstance(context: Context) = BaseViewActions().also {
            it.context = context
        }
    }


    fun showLoading() {
        mProgressDialog = MessageUtils.showLoadingDialog(context)
        mProgressDialog?.show()
    }

    fun hideLoading() {
        mProgressDialog?.let {
            if (it.isShowing) {
                it.cancel()
                it.dismissWithAnimation()
            }
        }
    }

    fun onMessageToast(error: GeneralError) {
        if (error.message != null)
            onMessageToast(error.message)
        else if (error.messageRes != null && error.messageRes != 0)
            onMessageToast(error.messageRes!!)
    }

    fun onMessageToast(message: Int) {
        onMessageToast(context.getString(message))
    }

    fun onErrorToast(@StringRes message: Int) {
        onErrorToast(context.getString(message))
    }

    fun onErrorToast(message: String?) {
        message?.let {
            val snack = Snackbar.make(View(context), it, Snackbar.LENGTH_LONG)
            snack.show()
        }
    }

    fun onMessageToast(message: String?) {
        MessageUtils.showMessageSnackbar(message, (context as AppCompatActivity).findViewById(android.R.id.content))
    }

    fun onErrorDialog(error: GeneralError) {
        if (mProgressDialog != null)
            if (mProgressDialog!!.isShowing) mProgressDialog!!.dismissWithAnimation()

        val message = if (error.message != null)
            error.message
        else if (error.messageRes != null && error.messageRes != 0)
            context.getString(error.messageRes!!)
        else ""

        mProgressDialog = MessageUtils.showErrorDialog(context, message)
        mProgressDialog!!.show()
    }

    fun onErrorDialog(message: String?) {
        if (mProgressDialog != null)
            if (mProgressDialog!!.isShowing) mProgressDialog!!.dismissWithAnimation()
        message?.let {
            mProgressDialog = MessageUtils.showErrorDialog(context, message)
            mProgressDialog!!.show()
        }
    }

    fun onSuccessDialog(message: String?) {
        if (mProgressDialog != null)
            if (mProgressDialog!!.isShowing) mProgressDialog!!.dismissWithAnimation()
        message?.let {
            mProgressDialog = MessageUtils.showSuccessDialog(context, message)
            mProgressDialog!!.show()
        }
    }

}
