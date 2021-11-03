package com.android.l2l.twolocal.ui.splash.di

import com.android.l2l.twolocal.dataSourse.remote.auth.AuthenticationRemoteDataSourceHelper
import com.android.l2l.twolocal.dataSourse.repository.profile.ProfileRepositoryHelper
import com.android.l2l.twolocal.di.component.AppComponent
import com.android.l2l.twolocal.ui.splash.SplashActivity
import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@SplashScope
@Component(dependencies = [AppComponent::class], modules = [ViewModelSplashModule::class, SplashModule::class])
interface SplashComponent {
    fun inject(splashActivity: SplashActivity)

    @Component.Factory
    interface Factory {

        fun create(appComponent: AppComponent): SplashComponent
    }
}