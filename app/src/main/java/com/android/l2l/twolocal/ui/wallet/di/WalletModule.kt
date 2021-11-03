package com.android.l2l.twolocal.ui.wallet.di

import android.content.Context
import com.android.l2l.twolocal.dataSourse.local.db.AppDatabase
import com.android.l2l.twolocal.dataSourse.local.prefs.UserSession
import com.android.l2l.twolocal.dataSourse.remote.api.ApiConstants.BSC_WEB3_MAINNET_URL
import com.android.l2l.twolocal.dataSourse.remote.currency.CryptoCurrencyApiInterface
import com.android.l2l.twolocal.dataSourse.remote.currency.CryptoCurrencyRemoteDataSource
import com.android.l2l.twolocal.dataSourse.remote.currency.CryptoCurrencyRemoteDataSourceHelper
import com.android.l2l.twolocal.dataSourse.remote.api.ApiConstants.ETHER_WEB3_MAINNET_INFURA_URL
import com.android.l2l.twolocal.dataSourse.remote.profile.ProfileApiInterface
import com.android.l2l.twolocal.dataSourse.remote.profile.ProfileRemoteDataSource
import com.android.l2l.twolocal.dataSourse.remote.profile.ProfileRemoteDataSourceHelper
import com.android.l2l.twolocal.dataSourse.repository.contact.ContactRepository
import com.android.l2l.twolocal.dataSourse.repository.contact.ContactRepositoryHelper
import com.android.l2l.twolocal.dataSourse.repository.crypto.CryptoCurrencyRepositoryHelper
import com.android.l2l.twolocal.dataSourse.repository.profile.ProfileRepository
import com.android.l2l.twolocal.dataSourse.repository.profile.ProfileRepositoryHelper
import com.android.l2l.twolocal.dataSourse.repository.crypto.ether.EtherRepository
import com.android.l2l.twolocal.dataSourse.repository.crypto.bsc.BSCRepository
import com.android.l2l.twolocal.dataSourse.repository.crypto.bsc.BinanceRepository
import com.android.l2l.twolocal.dataSourse.repository.wallet.WalletRepository
import com.android.l2l.twolocal.dataSourse.repository.wallet.WalletRepositoryHelper
import com.android.l2l.twolocal.model.enums.CryptoCurrencyType
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService
import retrofit2.Retrofit
import javax.inject.Named

@Module
@ExperimentalCoroutinesApi
class WalletModule {

    //    @WalletScope
    @Provides
    fun provideProfileRemoteDataSource(profileRemoteDataSource: ProfileRemoteDataSource): ProfileRemoteDataSourceHelper {
        return profileRemoteDataSource
    }

    //    @WalletScope
    @Provides
    fun provideEtherRemoteDataSource(etherRemoteDataSource: CryptoCurrencyRemoteDataSource): CryptoCurrencyRemoteDataSourceHelper {
        return etherRemoteDataSource
    }

    //////////////////////////////////////

    //    @WalletScope
    @Provides
    fun provideProfileApiInterface(retrofit: Retrofit): ProfileApiInterface = retrofit.create(ProfileApiInterface::class.java)


    //    @WalletScope
    @Provides
    fun provideEtherApiInterface(retrofit: Retrofit): CryptoCurrencyApiInterface = retrofit.create(CryptoCurrencyApiInterface::class.java)


    ////////////////////////////////////////////////

    //   @WalletScope
    @Provides
    fun provideCoinRepository(
        context: Context,
        appDatabase: AppDatabase,
        userSession: UserSession,
        retrofit: Retrofit,
        walletType: CryptoCurrencyType?,
    ): CryptoCurrencyRepositoryHelper {
        return walletRepositoryFactory(context, appDatabase, userSession, retrofit, walletType)
    }

    private fun walletRepositoryFactory(
        context: Context,
        appDatabase: AppDatabase,
        userSession: UserSession,
        retrofit: Retrofit,
        walletType: CryptoCurrencyType?,
    ): CryptoCurrencyRepositoryHelper =
        when (walletType) {
            CryptoCurrencyType.ETHEREUM -> EtherRepository(
                context,
                provideWeb3jETHER(),
                CryptoCurrencyRemoteDataSource(provideEtherApiInterface(retrofit)),
                appDatabase,
                userSession,
                walletType
            )
            CryptoCurrencyType.TwoLC -> BSCRepository(
                context,
                provideWeb3jBSC(),
                CryptoCurrencyRemoteDataSource(provideEtherApiInterface(retrofit)),
                appDatabase,
                userSession,
                walletType
            )
            CryptoCurrencyType.BINANCE -> BinanceRepository(
                context,
                provideWeb3jBSC(),
                CryptoCurrencyRemoteDataSource(provideEtherApiInterface(retrofit)),
                appDatabase,
                userSession,
                walletType
            )
            else -> BSCRepository(
                context,
                provideWeb3jBSC(),
                CryptoCurrencyRemoteDataSource(provideEtherApiInterface(retrofit)),
                appDatabase,
                userSession,
                walletType
            )
        }

    //    @WalletScope
    @Provides
    fun provideProfileRepository(profileRepository: ProfileRepository): ProfileRepositoryHelper {
        return profileRepository
    }


    //    @WalletScope
    @Provides
    fun provideWalletRepository(walletRepository: WalletRepository): WalletRepositoryHelper {
        return walletRepository
    }

    @Provides
    @Named("ethRepository")
    fun provideEtherRepository(
        context: Context,
        appDatabase: AppDatabase,
        userSession: UserSession,
        retrofit: Retrofit,
    ): CryptoCurrencyRepositoryHelper {
        return walletRepositoryFactory(context, appDatabase, userSession, retrofit, CryptoCurrencyType.ETHEREUM)
    }

    //
    @Provides
    @Named("bscRepository")
    fun provideBSCRepository(
        context: Context,
        appDatabase: AppDatabase,
        userSession: UserSession,
        retrofit: Retrofit,
    ): CryptoCurrencyRepositoryHelper {
        return walletRepositoryFactory(context, appDatabase, userSession, retrofit, CryptoCurrencyType.TwoLC)
    }

    @Provides
    @Named("bnbRepository")
    fun provideBNBRepository(
        context: Context,
        appDatabase: AppDatabase,
        userSession: UserSession,
        retrofit: Retrofit,
    ): CryptoCurrencyRepositoryHelper {
        return walletRepositoryFactory(context, appDatabase, userSession, retrofit, CryptoCurrencyType.BINANCE)
    }

    //    @WalletScope
    @Provides
    fun provideContactRepository(contactRepository: ContactRepository): ContactRepositoryHelper {
        return contactRepository
    }

    @WalletScope
    @Provides
    @Named("ETH")
    fun provideWeb3jETHER(): Web3j {
        return Web3j.build(HttpService(ETHER_WEB3_MAINNET_INFURA_URL))
        //        return  Web3j.build(new HttpService("https://main-light.eth.linkpool.io"));
//        return Web3j.build(HttpService("http://192.168.1.107:7545"));
    }

    @WalletScope
    @Provides
    @Named("BSC")
    fun provideWeb3jBSC(): Web3j {
        return Web3j.build(HttpService(BSC_WEB3_MAINNET_URL))
        //        return  Web3j.build(new HttpService("https://bsc-dataseed1.binance.org:443"));
//        return Web3j.build(HttpService("https://data-seed-prebsc-1-s1.binance.org:8545"));
    }
}