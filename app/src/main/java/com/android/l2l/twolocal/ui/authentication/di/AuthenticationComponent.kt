package com.android.l2l.twolocal.ui.authentication.di

import com.android.l2l.twolocal.di.component.AppComponent
import com.android.l2l.twolocal.ui.authentication.AuthenticationActivity
import com.android.l2l.twolocal.ui.authentication.DialogTowFactorVerification
import com.android.l2l.twolocal.ui.authentication.FragmentLogin
import com.android.l2l.twolocal.ui.authentication.FragmentRegister
import com.android.l2l.twolocal.ui.authentication.securityPassword.FragmentCreateLocalPassword
import com.android.l2l.twolocal.ui.authentication.securityPassword.FragmentUnlockApp
import com.android.l2l.twolocal.ui.authentication.securityPassword.SecurityPasswordActivity
import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AuthenticationScope
@Component(dependencies = [AppComponent::class], modules = [ViewModelAuthenticationModule::class, AuthenticationModule::class]) //, modules = [ViewModelAuthenticationModule::class]
interface AuthenticationComponent {

    fun inject(loginFragment: FragmentLogin)
    fun inject(fragmentRegister: FragmentRegister)
    fun inject(authenticationActivity: AuthenticationActivity)
    fun inject(dialogTowFactorVerification: DialogTowFactorVerification)
    fun inject(securityActivity: SecurityPasswordActivity)
    fun inject(fragmentUnlockApp: FragmentUnlockApp)
    fun inject(fragmentCreatePassword: FragmentCreateLocalPassword)


    @Component.Factory
    interface Factory {

        fun create(appComponent: AppComponent): AuthenticationComponent
    }
}