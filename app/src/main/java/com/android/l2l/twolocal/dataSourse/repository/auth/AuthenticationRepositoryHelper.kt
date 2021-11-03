package com.android.l2l.twolocal.dataSourse.repository.auth

import com.android.l2l.twolocal.model.ProfileInfo
import com.android.l2l.twolocal.model.TwoFAVerify
import com.android.l2l.twolocal.model.output.LoginOutput
import com.android.l2l.twolocal.model.output.RegisterOutput
import com.android.l2l.twolocal.model.response.RegisterResponse
import com.android.l2l.twolocal.model.response.base.ApiSingleResponse
import io.reactivex.Single

interface AuthenticationRepositoryHelper {

    fun login(login: LoginOutput): Single<ApiSingleResponse<ProfileInfo>>
    fun signUp(registerOutput: RegisterOutput): Single<ApiSingleResponse<ProfileInfo>>
    fun storeUserInfo(profile: ProfileInfo)
    fun verify2Fa(code: String, user_id: String): Single<ApiSingleResponse<TwoFAVerify>>
    fun saveUserLoggedIn()
}