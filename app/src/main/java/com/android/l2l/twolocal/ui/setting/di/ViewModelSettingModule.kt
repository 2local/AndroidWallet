package com.android.l2l.twolocal.ui.setting.di

import androidx.lifecycle.ViewModel
import com.android.l2l.twolocal.di.ViewModelKey
import com.android.l2l.twolocal.ui.setting.affiliate.AffiliateViewModel
import com.android.l2l.twolocal.ui.setting.contacts.AddContactViewModel
import com.android.l2l.twolocal.ui.setting.contacts.ContactViewModel
import com.android.l2l.twolocal.ui.setting.currency.CurrencyViewModel
import com.android.l2l.twolocal.ui.setting.profile.viewmodel.AccountInfoViewModel
import com.android.l2l.twolocal.ui.setting.SettingViewModel
import com.android.l2l.twolocal.ui.setting.localPassword.viewModel.ChangeLocalPasswordViewModel
import com.android.l2l.twolocal.ui.setting.password.viewmodel.PasswordViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import kotlinx.coroutines.ExperimentalCoroutinesApi


@Suppress("unused")
@Module

//internal
abstract class ViewModelSettingModule {
    //
    @ExperimentalCoroutinesApi
    @Binds
    @IntoMap
    @ViewModelKey(SettingViewModel::class)
    abstract fun bindSettingViewModel(settingViewModel: SettingViewModel): ViewModel

    @ExperimentalCoroutinesApi
    @Binds
    @IntoMap
    @ViewModelKey(AccountInfoViewModel::class)
    abstract fun bindAccountInfoViewModel(accountInfoViewModel: AccountInfoViewModel): ViewModel


    @ExperimentalCoroutinesApi
    @Binds
    @IntoMap
    @ViewModelKey(ContactViewModel::class)
    abstract fun bindContactViewModel(contactViewModel: ContactViewModel): ViewModel

    @ExperimentalCoroutinesApi
    @Binds
    @IntoMap
    @ViewModelKey(AddContactViewModel::class)
    abstract fun bindAddContactViewModel(addContactViewModel: AddContactViewModel): ViewModel

    @ExperimentalCoroutinesApi
    @Binds
    @IntoMap
    @ViewModelKey(CurrencyViewModel::class)
    abstract fun bindCurrencyViewModel(currencyViewModel: CurrencyViewModel): ViewModel

    @ExperimentalCoroutinesApi
    @Binds
    @IntoMap
    @ViewModelKey(AffiliateViewModel::class)
    abstract fun bindAffiliateViewModel(affiliateViewModel: AffiliateViewModel): ViewModel

    @ExperimentalCoroutinesApi
    @Binds
    @IntoMap
    @ViewModelKey(PasswordViewModel::class)
    abstract fun bindPasswordViewModel(passwordViewModel: PasswordViewModel): ViewModel

    @ExperimentalCoroutinesApi
    @Binds
    @IntoMap
    @ViewModelKey(ChangeLocalPasswordViewModel::class)
    abstract fun bindChangeLocalPasswordViewModel(changeLocalPasswordViewModel: ChangeLocalPasswordViewModel): ViewModel
}