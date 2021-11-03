package com.android.l2l.twolocal.common.initializer

import android.content.Context
import android.util.Log
import androidx.startup.Initializer
import io.branch.referral.Branch
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.Security

class BranchInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        // Enable branch
        // Branch logging for debugging
        Branch.enableLogging()

        // Branch object initialization
        Branch.getAutoInstance(context)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}