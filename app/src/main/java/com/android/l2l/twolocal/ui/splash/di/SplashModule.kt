package com.android.l2l.twolocal.ui.splash.di

import com.android.l2l.twolocal.dataSourse.local.ExchangeRateLocalDataSource
import com.android.l2l.twolocal.dataSourse.remote.exchangeRate.ExchangeRateApiInterface
import com.android.l2l.twolocal.dataSourse.remote.exchangeRate.ExchangeRateRemoteDataSource
import com.android.l2l.twolocal.dataSourse.remote.exchangeRate.ExchangeRateRemoteDataSourceHelper
import com.android.l2l.twolocal.dataSourse.remote.profile.ProfileApiInterface
import com.android.l2l.twolocal.dataSourse.remote.profile.ProfileRemoteDataSource
import com.android.l2l.twolocal.dataSourse.remote.profile.ProfileRemoteDataSourceHelper
import com.android.l2l.twolocal.dataSourse.repository.exchangeRate.ExchangeRateRepository
import com.android.l2l.twolocal.dataSourse.repository.exchangeRate.ExchangeRateRepositoryHelper
import com.android.l2l.twolocal.dataSourse.repository.profile.ProfileRepository
import com.android.l2l.twolocal.dataSourse.repository.profile.ProfileRepositoryHelper
import com.android.l2l.twolocal.dataSourse.repository.wallet.WalletRepository
import com.android.l2l.twolocal.dataSourse.repository.wallet.WalletRepositoryHelper
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import retrofit2.Retrofit

@Module
@ExperimentalCoroutinesApi
class SplashModule {

//    @SplashScope
    @Provides
    fun provideProfileRemoteDataSource(profileRemoteDataSource: ProfileRemoteDataSource): ProfileRemoteDataSourceHelper {
        return profileRemoteDataSource
    }
    @Provides
    fun provideExchangeRateRemoteDataSource(exchangeRateRemoteDataSource: ExchangeRateRemoteDataSource): ExchangeRateRemoteDataSourceHelper {
        return exchangeRateRemoteDataSource
    }
//    @SplashScope
    @Provides
    fun provideProfileRepository(profileRepository: ProfileRepository): ProfileRepositoryHelper {
        return profileRepository
    }

//    @SplashScope
    @Provides
    fun provideWalletRepository(walletRepository: WalletRepository): WalletRepositoryHelper {
        return walletRepository
    }

//    @SplashScope
    @Provides
    fun provideExchangeRateRepository(exchangeRateRepository: ExchangeRateRepository): ExchangeRateRepositoryHelper {
        return exchangeRateRepository
    }


//    @SplashScope
    @Provides
    fun provideProfileApiInterface(retrofit: Retrofit): ProfileApiInterface = retrofit.create(ProfileApiInterface::class.java)

//    @SplashScope
    @Provides
    fun provideExchangeRateApiInterface(retrofit: Retrofit): ExchangeRateApiInterface = retrofit.create(ExchangeRateApiInterface::class.java)


}