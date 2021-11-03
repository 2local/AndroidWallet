package com.android.l2l.twolocal.dataSourse.remote.auth;

import com.android.l2l.twolocal.dataSourse.remote.api.ApiConstants
import com.android.l2l.twolocal.dataSourse.remote.api.ApiConstants.LOGIN_INVESTOR_ENDPOINT
import com.android.l2l.twolocal.model.ProfileInfo
import com.android.l2l.twolocal.model.TwoFAVerify
import com.android.l2l.twolocal.model.output.LoginOutput
import com.android.l2l.twolocal.model.output.RegisterOutput
import com.android.l2l.twolocal.model.response.RegisterResponse
import com.android.l2l.twolocal.model.response.base.ApiSingleResponse
import io.reactivex.Single
import retrofit2.http.*
import java.util.*


interface AuthenticationApiInterface {

    @POST(LOGIN_INVESTOR_ENDPOINT)
    fun login(@Body login: LoginOutput): Single<ApiSingleResponse<ProfileInfo>>

    @POST(ApiConstants.SIGN_UP_INVESTOR_ENDPOINT)
    fun signUp(@Body login: RegisterOutput?): Single<ApiSingleResponse<ProfileInfo>>

    @POST(ApiConstants.VALIDATE_TWO_FA)
    fun verify2Fa(@Body body: HashMap<String, String>): Single<ApiSingleResponse<TwoFAVerify>>
}