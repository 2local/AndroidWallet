package com.android.l2l.twolocal.ui.base;


import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import cn.pedant.SweetAlert.SweetAlertDialog
import com.android.l2l.twolocal.dataSourse.utils.error.GeneralError

/**
 * All Fragments must extends from this Base class
 */

abstract class BaseFragment<VM : BaseViewModel>(@LayoutRes contentLayoutId: Int = 0) : Fragment(contentLayoutId) {


    protected abstract val viewModel: VM
    private var viewActions: SweetAlertDialog? = null


    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    fun showLoading() {
        viewActions = BaseViewActions.getSweetAlertDialog(requireContext())
        BaseViewActions.showLoading(viewActions!!)
    }

    fun hideLoading() {
        if (viewActions != null)
            BaseViewActions.hideLoading(viewActions!!)
    }


    override fun onDestroy() {
        super.onDestroy()
    }

}
