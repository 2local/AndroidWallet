package com.android.l2l.twolocal.ui.authentication.di

import androidx.lifecycle.ViewModel
import com.android.l2l.twolocal.di.ViewModelKey
import com.android.l2l.twolocal.ui.authentication.securityPassword.viewmodel.CreateLocalPasswordViewModel
import com.android.l2l.twolocal.ui.authentication.securityPassword.viewmodel.UnlockViewModel
import com.android.l2l.twolocal.ui.authentication.viewModel.LoginViewModel
import com.android.l2l.twolocal.ui.authentication.viewModel.RegisterViewModel
import com.android.l2l.twolocal.ui.authentication.viewModel.TwoFactorViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import kotlinx.coroutines.ExperimentalCoroutinesApi


@Suppress("unused")
@Module

//internal
abstract class ViewModelAuthenticationModule {
//
    @ExperimentalCoroutinesApi
    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLoginViewModel(loginViewModel: LoginViewModel): ViewModel

    @ExperimentalCoroutinesApi
    @Binds
    @IntoMap
    @ViewModelKey(RegisterViewModel::class)
    abstract fun bindRegisterViewModel(registerViewModel: RegisterViewModel): ViewModel

    @ExperimentalCoroutinesApi
    @Binds
    @IntoMap
    @ViewModelKey(TwoFactorViewModel::class)
    abstract fun bindTwoFactorViewModel(registerViewModel: TwoFactorViewModel): ViewModel

    @ExperimentalCoroutinesApi
    @Binds
    @IntoMap
    @ViewModelKey(UnlockViewModel::class)
    abstract fun bindUnlockViewModel(unlockViewModel: UnlockViewModel): ViewModel

    @ExperimentalCoroutinesApi
    @Binds
    @IntoMap
    @ViewModelKey(CreateLocalPasswordViewModel::class)
    abstract fun bindCreatePasswordViewModel(createPasswordViewModel: CreateLocalPasswordViewModel): ViewModel

}