package com.android.l2l.twolocal.dataSourse.repository.auth


import com.android.l2l.twolocal.dataSourse.local.AuthenticationLocalDataSource
import com.android.l2l.twolocal.dataSourse.remote.auth.AuthenticationRemoteDataSourceHelper
import com.android.l2l.twolocal.model.ProfileInfo
import com.android.l2l.twolocal.model.TwoFAVerify
import com.android.l2l.twolocal.model.output.LoginOutput
import com.android.l2l.twolocal.model.output.RegisterOutput
import com.android.l2l.twolocal.model.response.RegisterResponse
import com.android.l2l.twolocal.model.response.base.ApiSingleResponse
import io.reactivex.Single
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
open class AuthenticationRepository @Inject constructor(
    private val remoteDataSource: AuthenticationRemoteDataSourceHelper,
    private val localDataSource: AuthenticationLocalDataSource
) : AuthenticationRepositoryHelper {

    override fun login(login: LoginOutput): Single<ApiSingleResponse<ProfileInfo>> {
        return remoteDataSource.login(login)
    }

    override fun signUp(registerOutput: RegisterOutput): Single<ApiSingleResponse<ProfileInfo>> {
        return remoteDataSource.signUp(registerOutput)
    }

    override fun storeUserInfo(profile: ProfileInfo) {
        localDataSource.saveProfileInfo(profile)
        localDataSource.saveApiToken(String.format("%s %s", profile.token_type, profile.access_token))
    }

    override fun saveUserLoggedIn() {
        localDataSource.saveUserLoggedIn()
    }

    override fun verify2Fa(code: String, user_id: String): Single<ApiSingleResponse<TwoFAVerify>> {
        return remoteDataSource.verify2Fa(code, user_id)
    }
}