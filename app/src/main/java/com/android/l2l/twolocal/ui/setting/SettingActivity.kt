package com.android.l2l.twolocal.ui.setting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.android.l2l.twolocal.R
import com.android.l2l.twolocal.common.binding.viewBinding
import com.android.l2l.twolocal.common.findAppComponent
import com.android.l2l.twolocal.databinding.ActivitySettingBinding
import com.android.l2l.twolocal.ui.base.BaseActivity
import com.android.l2l.twolocal.ui.setting.di.DaggerSettingComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class SettingActivity : BaseActivity(R.layout.activity_setting){

//    private val binding: ActivitySettingBinding by viewBinding()

    companion object {

        fun start(context: Context) {
            Intent(context, SettingActivity::class.java).apply {
                context.startActivity(this)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        DaggerSettingComponent.factory().create(findAppComponent()).inject(this)
        super.onCreate(savedInstanceState)

    }

}