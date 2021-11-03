package com.android.l2l.twolocal.dataSourse.remote.profile;

import com.android.l2l.twolocal.dataSourse.remote.api.ApiConstants
import com.android.l2l.twolocal.model.ProfileInfo
import com.android.l2l.twolocal.model.response.base.ApiBaseResponse
import com.android.l2l.twolocal.model.response.base.ApiSingleResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface ProfileApiInterface {

    @GET(ApiConstants.GET_PROFILE_DATA_ENDPOINT)
    fun getProfileData(@Query("user_id") user_id: String): Single<ApiSingleResponse<ProfileInfo>>

    @POST(ApiConstants.UPDATE_PROFILE_ENDPOINT)
    fun changePassword(@Body body: ProfileInfo): Single<ApiBaseResponse>

    @POST(ApiConstants.UPDATE_PROFILE_ENDPOINT)
    fun disable2Fa(@Body body: ProfileInfo): Single<ApiBaseResponse>

    @POST(ApiConstants.UPDATE_PROFILE_ENDPOINT)
    fun updateProfile(@Body body: ProfileInfo): Single<ApiSingleResponse<ProfileInfo>>
}