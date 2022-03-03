package com.android.l2l.twolocal

import android.app.Application
import com.android.l2l.twolocal.di.component.AppComponent
import com.android.l2l.twolocal.di.component.DaggerAppComponent
import io.branch.referral.Branch
import io.reactivex.plugins.RxJavaPlugins
import org.bouncycastle.jce.provider.BouncyCastleProvider
import timber.log.Timber
import java.security.Security


class App : Application() {

    lateinit var component: AppComponent


    override fun onCreate() {
        super.onCreate()

        createComponent()
        component.inject(this)

        RxJavaPlugins.setErrorHandler { throwable: Throwable ->
            Timber.i("MyApplication MyApplication ${throwable.message}")
            throwable.printStackTrace()
        }

//        setupBouncyCastle()

    }

    private fun createComponent() {
        component = DaggerAppComponent.factory().create(this);
    }

    fun getAppComponent(): AppComponent {
        return component
    }

//    private fun setupBouncyCastle() {
//        val provider = Security.getProvider(BouncyCastleProvider.PROVIDER_NAME)
//            ?: // Web3j will set up the provider lazily when it's first used.
//            return
//        if (provider.javaClass == BouncyCastleProvider::class.java) {
//            // BC with same package name, shouldn't happen in real life.
//            return
//        }
//        // Android registers its own BC provider. As it might be outdated and might not include
//        // all needed ciphers, we substitute it with a known BC bundled in the app.
//        // Android's BC has its package rewritten to "com.android.org.bouncycastle" and because
//        // of that it's possible to have another BC implementation loaded in VM.
//        Security.removeProvider(BouncyCastleProvider.PROVIDER_NAME)
//        Security.insertProviderAt(BouncyCastleProvider(), 1)
//    }
}