package com.android.l2l.twolocal.ui.base;

import android.os.Bundle;
import android.view.LayoutInflater
import android.view.MenuItem;
import android.view.ViewGroup
import androidx.annotation.LayoutRes


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.*
import androidx.viewbinding.ViewBinding
import com.android.l2l.twolocal.dataSourse.utils.error.GeneralError
import com.android.l2l.twolocal.di.viewModel.AppViewModelFactory
import com.android.l2l.twolocal.model.event.RefreshWalletEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


abstract class BaseActivity(@LayoutRes contentLayoutId : Int = 0) : AppCompatActivity(contentLayoutId), BaseView {

    lateinit var viewActions: BaseViewActions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        viewActions = BaseViewActions.getInstance(this)
        EventBus.getDefault().register(this)
    }


    override fun showLoading(message: String?) {
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
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
        viewActions.onMessageToast(getString(message))
    }

    override fun onMessageToast(message: String?) {
        viewActions.onMessageToast(message)
    }

    fun onErrorDialog(message: String?) {
        viewActions.onErrorDialog(message)
    }

    fun onSuccessDialog(message: String?) {
        viewActions.onSuccessDialog(message)
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
