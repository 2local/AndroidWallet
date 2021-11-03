package com.android.l2l.twolocal.dataSourse.repository.profile

import com.android.l2l.twolocal.dataSourse.local.prefs.UserSessionHelper
import com.android.l2l.twolocal.dataSourse.remote.profile.ProfileRemoteDataSourceHelper
import com.android.l2l.twolocal.dataSourse.repository.wallet.WalletRepositoryHelper
import com.android.l2l.twolocal.model.ProfileInfo
import com.android.l2l.twolocal.model.response.base.ApiBaseResponse
import com.android.l2l.twolocal.model.response.base.ApiSingleResponse
import io.reactivex.Single
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class ProfileRepository @Inject constructor(
    private val remoteDataSource: ProfileRemoteDataSourceHelper,
    private val walletRepository: WalletRepositoryHelper,
    private val userSession: UserSessionHelper
) :ProfileRepositoryHelper {


    override fun profile(): Single<ApiSingleResponse<ProfileInfo>> {
        return remoteDataSource.profile(userSession.userId).doOnSuccess {
            if(it.code == 200)
                userSession.saveProfileInfo(it.record)
        }
    }

    override fun updateProfile(profile: ProfileInfo): Single<ApiSingleResponse<ProfileInfo>> {
        return remoteDataSource.updateProfile(profile).doOnSuccess {
            if(it.code == 200){
                userSession.saveProfileInfo(profile)
            }

        }
    }

    override fun changePassword(profile: ProfileInfo): Single<ApiBaseResponse> {
        return remoteDataSource.changePassword(profile).doOnSuccess {
        }
    }

    override fun disable2Fa(profile: ProfileInfo): Single<ApiBaseResponse> {
        return remoteDataSource.disable2Fa(profile).doOnSuccess {
        }
    }

    override fun signOut(): Single<Boolean> {
        userSession.setUserLoggedOut()
        return Single.just(true)
    }

    override fun resetWalletAndSignOut(): Single<Boolean> {
        userSession.clearSession()
        return walletRepository.deleteAllTables()
    }

}