package com.android.l2l.twolocal.di.viewModel

import androidx.lifecycle.ViewModel
import com.android.l2l.twolocal.di.ViewModelKey
import com.android.l2l.twolocal.ui.authentication.viewModel.LoginViewModel
import com.android.l2l.twolocal.ui.base.BaseViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import kotlinx.coroutines.ExperimentalCoroutinesApi


@Suppress("unused")
@Module
abstract class ViewModelModule {

    @ExperimentalCoroutinesApi
    @Binds
    @IntoMap
    @ViewModelKey(BaseViewModel::class)
    abstract fun bindBaseViewModel(baseViewModel: BaseViewModel): ViewModel

}