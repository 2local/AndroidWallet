package com.android.l2l.twolocal.ui.market.di

import com.android.l2l.twolocal.di.component.AppComponent
import com.android.l2l.twolocal.ui.market.FragmentMarketList
import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@MarketScope
@Component(dependencies = [AppComponent::class], modules = [ViewModelMarketModule::class, MarketModule::class])
interface MarketComponent {
    fun inject(fragmentMarketList: FragmentMarketList)

    @Component.Factory
    interface Factory {

        fun create(appComponent: AppComponent): MarketComponent
    }
}