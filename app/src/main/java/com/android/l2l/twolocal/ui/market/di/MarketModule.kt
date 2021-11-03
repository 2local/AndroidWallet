package com.android.l2l.twolocal.ui.market.di

import com.android.l2l.twolocal.dataSourse.remote.market.MarketApiInterface
import com.android.l2l.twolocal.dataSourse.remote.market.MarketRemoteDataSource
import com.android.l2l.twolocal.dataSourse.remote.market.MarketRemoteDataSourceHelper
import com.android.l2l.twolocal.dataSourse.repository.market.MarketRepository
import com.android.l2l.twolocal.dataSourse.repository.market.MarketRepositoryHelper
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import retrofit2.Retrofit

@Module
@ExperimentalCoroutinesApi
class MarketModule {

//    @MarketScope
    @Provides
    fun provideMarketRepository(marketRepository: MarketRepository): MarketRepositoryHelper {
        return marketRepository
    }

//    @MarketScope
    @Provides
    fun provideMarketRemoteDataSource(marketRemoteDataSource: MarketRemoteDataSource): MarketRemoteDataSourceHelper {
        return marketRemoteDataSource
    }

//    @MarketScope
    @Provides
    fun provideMarketApiInterface(retrofit: Retrofit): MarketApiInterface = retrofit.create(MarketApiInterface::class.java)

}