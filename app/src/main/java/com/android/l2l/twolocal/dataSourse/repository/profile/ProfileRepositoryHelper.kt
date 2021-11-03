package com.android.l2l.twolocal.dataSourse.repository.profile

import com.android.l2l.twolocal.model.ProfileInfo
import com.android.l2l.twolocal.model.response.base.ApiBaseResponse
import com.android.l2l.twolocal.model.response.base.ApiSingleResponse
import io.reactivex.Single

interface ProfileRepositoryHelper {

    fun profile(): Single<ApiSingleResponse<ProfileInfo>>

    fun updateProfile(profile: ProfileInfo): Single<ApiSingleResponse<ProfileInfo>>

    fun changePassword(profile: ProfileInfo): Single<ApiBaseResponse>

    fun disable2Fa(profile: ProfileInfo): Single<ApiBaseResponse>

    fun signOut(): Single<Boolean>
    fun resetWalletAndSignOut(): Single<Boolean>
}