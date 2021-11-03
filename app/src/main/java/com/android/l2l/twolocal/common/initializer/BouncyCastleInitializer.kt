package com.android.l2l.twolocal.common.initializer

import android.content.Context
import android.util.Log
import androidx.startup.Initializer
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.Security

class BouncyCastleInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        setupBouncyCastle()
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }

    private fun setupBouncyCastle() {
        val provider = Security.getProvider(BouncyCastleProvider.PROVIDER_NAME)
            ?: // Web3j will set up the provider lazily when it's first used.
            return
        if (provider.javaClass == BouncyCastleProvider::class.java) {
            // BC with same package name, shouldn't happen in real life.
            return
        }
        // Android registers its own BC provider. As it might be outdated and might not include
        // all needed ciphers, we substitute it with a known BC bundled in the app.
        // Android's BC has its package rewritten to "com.android.org.bouncycastle" and because
        // of that it's possible to have another BC implementation loaded in VM.
        Security.removeProvider(BouncyCastleProvider.PROVIDER_NAME)
        Security.insertProviderAt(BouncyCastleProvider(), 1)
        Log.v("setupBouncyCastle", "setupBouncyCastle")
    }
}