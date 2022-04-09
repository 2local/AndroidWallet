package com.android.l2l.twolocal.dataSourse.repository.auth

import com.android.l2l.twolocal.model.ProfileInfo
import com.android.l2l.twolocal.model.TwoFAVerify
import com.android.l2l.twolocal.model.request.LoginRequest
import com.android.l2l.twolocal.model.request.RegisterRequest
import com.android.l2l.twolocal.model.response.base.ApiSingleResponse
import io.reactivex.Single

interface AuthenticationRepositoryHelper {

    fun login(login: LoginRequest): Single<ApiSingleResponse<ProfileInfo>>
    fun signUp(registerOutput: RegisterRequest): Single<ApiSingleResponse<ProfileInfo>>
    fun storeUserInfo(profile: ProfileInfo)
    fun verify2Fa(code: String, user_id: String): Single<ApiSingleResponse<TwoFAVerify>>
    fun saveUserLoggedIn()
}