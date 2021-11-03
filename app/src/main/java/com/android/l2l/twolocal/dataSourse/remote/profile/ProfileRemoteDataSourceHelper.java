package com.android.l2l.twolocal.dataSourse.remote.profile;


import com.android.l2l.twolocal.model.ProfileInfo;
import com.android.l2l.twolocal.model.response.base.ApiBaseResponse;
import com.android.l2l.twolocal.model.response.base.ApiSingleResponse;

import io.reactivex.Single;

public interface ProfileRemoteDataSourceHelper {

    Single<ApiSingleResponse<ProfileInfo>> profile(String userID);

    Single<ApiSingleResponse<ProfileInfo>> updateProfile(ProfileInfo profile);

    Single<ApiBaseResponse> changePassword(ProfileInfo profile);

    Single<ApiBaseResponse> disable2Fa(ProfileInfo profile);
}
