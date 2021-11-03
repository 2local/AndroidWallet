package com.android.l2l.twolocal.ui.market.di

import androidx.lifecycle.ViewModel
import com.android.l2l.twolocal.di.ViewModelKey
import com.android.l2l.twolocal.ui.market.MarketListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import kotlinx.coroutines.ExperimentalCoroutinesApi


@Suppress("unused")
@Module
abstract class ViewModelMarketModule {
    @ExperimentalCoroutinesApi
    @Binds
    @IntoMap
    @ViewModelKey(MarketListViewModel::class)
    abstract fun bindMarketListViewModel(homeViewModel: MarketListViewModel): ViewModel

}