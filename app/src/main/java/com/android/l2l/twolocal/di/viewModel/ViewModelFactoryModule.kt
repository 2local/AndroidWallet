package com.android.l2l.twolocal.di.viewModel

import androidx.lifecycle.ViewModelProvider
import com.android.l2l.twolocal.di.viewModel.AppViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelFactoryModule {


    @Binds
    abstract fun bindViewModelFactory(appViewModelFactory: AppViewModelFactory): ViewModelProvider.Factory
}