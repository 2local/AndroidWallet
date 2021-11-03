package com.android.l2l.twolocal.ui.authentication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.android.l2l.twolocal.R
import com.android.l2l.twolocal.common.binding.viewBinding
import com.android.l2l.twolocal.common.findAppComponent
import com.android.l2l.twolocal.databinding.ActivityAuthenticationBinding
import com.android.l2l.twolocal.databinding.ActivityMainBinding
import com.android.l2l.twolocal.ui.authentication.di.DaggerAuthenticationComponent
import com.android.l2l.twolocal.ui.base.BaseActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class AuthenticationActivity : BaseActivity(R.layout.activity_authentication){

    companion object {

        fun start(context: Context) {
            Intent(context, AuthenticationActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                context.startActivity(this)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        DaggerAuthenticationComponent.factory().create(findAppComponent()).inject(this)
        super.onCreate(savedInstanceState)


    }

}