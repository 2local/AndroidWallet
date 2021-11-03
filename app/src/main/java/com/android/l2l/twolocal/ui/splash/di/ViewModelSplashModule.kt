package com.android.l2l.twolocal.ui.splash.di

import androidx.lifecycle.ViewModel
import com.android.l2l.twolocal.di.ViewModelKey
import com.android.l2l.twolocal.ui.authentication.viewModel.LoginViewModel
import com.android.l2l.twolocal.ui.authentication.viewModel.TwoFactorViewModel
import com.android.l2l.twolocal.ui.splash.viewModel.SplashViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import kotlinx.coroutines.ExperimentalCoroutinesApi


@Suppress("unused")
@Module
abstract class ViewModelSplashModule {
    @ExperimentalCoroutinesApi
    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    abstract fun bindSplashViewModel(loginViewModel: SplashViewModel): ViewModel


}