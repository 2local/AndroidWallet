package com.android.l2l.twolocal.ui.base;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.LayoutRes


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.*
import androidx.viewbinding.ViewBinding
import cn.pedant.SweetAlert.SweetAlertDialog
import com.android.l2l.twolocal.dataSourse.utils.error.GeneralError
import com.android.l2l.twolocal.di.viewModel.AppViewModelFactory
import com.android.l2l.twolocal.model.event.RefreshWalletEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


abstract class BaseActivity(@LayoutRes contentLayoutId : Int = 0) : AppCompatActivity(contentLayoutId) {

    private var viewActions: SweetAlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this)
    }

    fun showLoading() {
        viewActions = BaseViewActions.getSweetAlertDialog(this)
        BaseViewActions.showLoading(viewActions!!)
    }

    fun hideLoading() {
        if(viewActions!=null)
            BaseViewActions.hideLoading(viewActions!!)
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy();
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    override fun onBackPressed() {
        super.onBackPressed();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: Any) {
    }
}
