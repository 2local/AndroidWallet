package com.android.l2l.twolocal.ui.wallet.create

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import com.android.l2l.twolocal.R
import com.android.l2l.twolocal.common.findAppComponent
import com.android.l2l.twolocal.ui.base.BaseActivity
import com.android.l2l.twolocal.ui.wallet.di.DaggerWalletComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class CreateWalletActivity : BaseActivity(R.layout.activity_create_wallet){

    companion object {

        fun start(context: Context) {
            Intent(context, CreateWalletActivity::class.java).apply {
                context.startActivity(this)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)
    }

}