package com.android.l2l.twolocal.ui.authentication.securityPassword

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.android.l2l.twolocal.R
import com.android.l2l.twolocal.common.findAppComponent
import com.android.l2l.twolocal.ui.authentication.di.DaggerAuthenticationComponent
import com.android.l2l.twolocal.ui.base.BaseActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class SecurityPasswordActivity : BaseActivity(R.layout.activity_security_password){

    companion object {

        fun start(context: Context) {
            Intent(context, SecurityPasswordActivity::class.java).apply {
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