package com.android.l2l.twolocal.ui.setting.di

import com.android.l2l.twolocal.dataSourse.remote.profile.ProfileApiInterface
import com.android.l2l.twolocal.dataSourse.remote.profile.ProfileRemoteDataSource
import com.android.l2l.twolocal.dataSourse.remote.profile.ProfileRemoteDataSourceHelper
import com.android.l2l.twolocal.dataSourse.repository.contact.ContactRepository
import com.android.l2l.twolocal.dataSourse.repository.contact.ContactRepositoryHelper
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
class SettingModule {

//    @SettingScope
    @Provides
    fun provideAuthenticationRepository(profileRepository: ProfileRepository): ProfileRepositoryHelper {
        return profileRepository
    }

//    @SettingScope
    @Provides
    fun provideProfileRemoteDataSourceHelper(profileRemoteDataSource: ProfileRemoteDataSource): ProfileRemoteDataSourceHelper {
        return profileRemoteDataSource
    }

//    @SettingScope
    @Provides
    fun provideContactRepositoryHelper(contactRepository: ContactRepository): ContactRepositoryHelper {
        return contactRepository
    }

//    @SettingScope
    @Provides
    fun provideWalletRepositoryHelper(walletRepository: WalletRepository): WalletRepositoryHelper {
        return walletRepository
    }

//    @SettingScope
    @Provides
    fun provideApiService(retrofit: Retrofit): ProfileApiInterface = retrofit.create(ProfileApiInterface::class.java)


}