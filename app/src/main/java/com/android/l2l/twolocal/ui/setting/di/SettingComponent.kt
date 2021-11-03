package com.android.l2l.twolocal.ui.setting.di

import com.android.l2l.twolocal.di.component.AppComponent
import com.android.l2l.twolocal.ui.setting.profile.AccountInfoFragment
import com.android.l2l.twolocal.ui.setting.SettingActivity
import com.android.l2l.twolocal.ui.setting.SettingFragment
import com.android.l2l.twolocal.ui.setting.about.AboutFragment
import com.android.l2l.twolocal.ui.setting.affiliate.AffiliateFragment
import com.android.l2l.twolocal.ui.setting.contacts.ContactAddFragment
import com.android.l2l.twolocal.ui.setting.contacts.ContactListFragment
import com.android.l2l.twolocal.ui.setting.currency.CurrencyFragment
import com.android.l2l.twolocal.ui.setting.localPassword.FragmentChangeLocalPassword
import com.android.l2l.twolocal.ui.setting.password.PasswordFragment
import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@SettingScope
@Component(dependencies = [AppComponent::class], modules = [ViewModelSettingModule::class, SettingModule::class]) //, modules = [ViewModelAuthenticationModule::class]
interface SettingComponent {

    fun inject(settingActivity: SettingActivity)
    fun inject(accountInfoRegister: AccountInfoFragment)
    fun inject(settingFragment: SettingFragment)
    fun inject(contactFragment: ContactListFragment)
    fun inject(contactAddFragment: ContactAddFragment)
    fun inject(currencyFragment: CurrencyFragment)
    fun inject(affiliateFragment: AffiliateFragment)
    fun inject(passwordFragment: PasswordFragment)
    fun inject(aboutFragment: AboutFragment)
    fun inject(fragmentChangeLocalPassword: FragmentChangeLocalPassword)


    @Component.Factory
    interface Factory {

        fun create(appComponent: AppComponent): SettingComponent
    }
}