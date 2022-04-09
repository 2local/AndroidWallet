package com.android.l2l.twolocal.dataSourse.remote.auth;

import com.android.l2l.twolocal.dataSourse.remote.api.ApiConstants
import com.android.l2l.twolocal.dataSourse.remote.api.ApiConstants.LOGIN_INVESTOR_ENDPOINT
import com.android.l2l.twolocal.model.ProfileInfo
import com.android.l2l.twolocal.model.TwoFAVerify
import com.android.l2l.twolocal.model.request.LoginRequest
import com.android.l2l.twolocal.model.request.RegisterRequest
import com.android.l2l.twolocal.model.response.base.ApiSingleResponse
import io.reactivex.Single
import retrofit2.http.*
import java.util.*


interface AuthenticationApiInterface {

    @POST(LOGIN_INVESTOR_ENDPOINT)
    fun login(@Body login: LoginRequest): Single<ApiSingleResponse<ProfileInfo>>

    @POST(ApiConstants.SIGN_UP_INVESTOR_ENDPOINT)
    fun signUp(@Body login: RegisterRequest?): Single<ApiSingleResponse<ProfileInfo>>

    @POST(ApiConstants.VALIDATE_TWO_FA)
    fun verify2Fa(@Body body: HashMap<String, String>): Single<ApiSingleResponse<TwoFAVerify>>
}