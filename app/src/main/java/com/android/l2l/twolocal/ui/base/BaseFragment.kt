package com.android.l2l.twolocal.ui.base;


import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import com.android.l2l.twolocal.dataSourse.utils.error.GeneralError

/**
 * All Fragments must extends from this Base class
 */

abstract class BaseFragment<VM : BaseViewModel>(@LayoutRes contentLayoutId: Int = 0) : Fragment(contentLayoutId), BaseView {


    protected abstract val viewModel: VM
    lateinit var viewActions: BaseViewActions


    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewActions = BaseViewActions.getInstance(requireContext())
    }


    override fun showLoading(message: String?) {
    }

    override fun showLoading() {
        viewActions.showLoading()
    }

    override fun hideLoading() {
        viewActions.hideLoading()
    }

    override fun unauthorizedUser(response: String?) {
    }

    override fun onTimeout(throwable: Throwable?) {
    }

    override fun onNetworkError(throwable: Throwable?) {
    }

    override fun onErrorToast(error: GeneralError?) {
        viewActions.onErrorToast(error?.message)
    }

    fun onMessageToast(message: Int) {
        viewActions.showMessageSnackbar(getString(message))
    }

    override fun onMessageToast(message: String?) {
        viewActions.showMessageSnackbar(message)
    }

    fun onMessageToast(error: GeneralError) {
        viewActions.showMessageSnackbar(error)
    }

    fun onErrorDialog(message: String?) {
        viewActions.onErrorDialog(message)
    }

    fun onSuccessDialog(message: String?) {
        viewActions.onSuccessDialog(message)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}
