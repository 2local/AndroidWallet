package com.android.l2l.twolocal.common.initializer

import android.content.Context
import androidx.startup.Initializer
import com.android.l2l.twolocal.utils.RemoteConfigUtils

class RemoteConfigInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        RemoteConfigUtils.init()
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}