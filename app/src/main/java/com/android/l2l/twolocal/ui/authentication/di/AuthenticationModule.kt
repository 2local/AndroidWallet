package com.android.l2l.twolocal.ui.authentication.di


import com.android.l2l.twolocal.dataSourse.remote.auth.AuthenticationApiInterface
import com.android.l2l.twolocal.dataSourse.remote.auth.AuthenticationRemoteDataSource
import com.android.l2l.twolocal.dataSourse.remote.auth.AuthenticationRemoteDataSourceHelper
import com.android.l2l.twolocal.dataSourse.remote.profile.ProfileApiInterface
import com.android.l2l.twolocal.dataSourse.remote.profile.ProfileRemoteDataSource
import com.android.l2l.twolocal.dataSourse.remote.profile.ProfileRemoteDataSourceHelper
import com.android.l2l.twolocal.dataSourse.repository.auth.AuthenticationRepository
import com.android.l2l.twolocal.dataSourse.repository.auth.AuthenticationRepositoryHelper
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
class AuthenticationModule {

//    @AuthenticationScope
    @Provides
    fun provideAuthenticationRepository(authenticationRepository: AuthenticationRepository): AuthenticationRepositoryHelper {
        return authenticationRepository
    }
//    @AuthenticationScope
    @Provides
    fun provideAuthenticationRemoteDataSource(authenticationRemoteDataSource: AuthenticationRemoteDataSource): AuthenticationRemoteDataSourceHelper {
        return authenticationRemoteDataSource
    }
//    @AuthenticationScope
    @Provides
    fun provideApiService(retrofit: Retrofit): AuthenticationApiInterface = retrofit.create(AuthenticationApiInterface::class.java)


    @Provides
    fun provideProfileRepository(profileRepository: ProfileRepository): ProfileRepositoryHelper {
        return profileRepository
    }

    //    @SettingScope
    @Provides
    fun provideProfileRemoteDataSourceHelper(profileRemoteDataSource: ProfileRemoteDataSource): ProfileRemoteDataSourceHelper {
        return profileRemoteDataSource
    }

    //    @SettingScope
    @Provides
    fun provideWalletRepositoryHelper(walletRepository: WalletRepository): WalletRepositoryHelper {
        return walletRepository
    }

    //    @SettingScope
    @Provides
    fun provideProfileApiService(retrofit: Retrofit): ProfileApiInterface = retrofit.create(ProfileApiInterface::class.java)


}