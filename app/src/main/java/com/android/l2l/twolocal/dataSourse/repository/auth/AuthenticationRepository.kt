package com.android.l2l.twolocal.dataSourse.repository.auth


import com.android.l2l.twolocal.dataSourse.local.AuthenticationLocalDataSource
import com.android.l2l.twolocal.dataSourse.remote.auth.AuthenticationRemoteDataSourceHelper
import com.android.l2l.twolocal.model.ProfileInfo
import com.android.l2l.twolocal.model.TwoFAVerify
import com.android.l2l.twolocal.model.request.LoginRequest
import com.android.l2l.twolocal.model.request.RegisterRequest
import com.android.l2l.twolocal.model.response.base.ApiSingleResponse
import io.reactivex.Single
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class AuthenticationRepository @Inject constructor(
    private val remoteDataSource: AuthenticationRemoteDataSourceHelper,
    private val localDataSource: AuthenticationLocalDataSource
) : AuthenticationRepositoryHelper {

    override fun login(login: LoginRequest): Single<ApiSingleResponse<ProfileInfo>> {
        return remoteDataSource.login(login)
    }

    override fun signUp(registerOutput: RegisterRequest): Single<ApiSingleResponse<ProfileInfo>> {
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