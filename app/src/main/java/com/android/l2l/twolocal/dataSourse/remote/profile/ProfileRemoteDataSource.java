package com.android.l2l.twolocal.dataSourse.remote.profile;

import com.android.l2l.twolocal.model.ProfileInfo;
import com.android.l2l.twolocal.model.response.base.ApiBaseResponse;
import com.android.l2l.twolocal.model.response.base.ApiSingleResponse;

import javax.inject.Inject;

import io.reactivex.Single;

public class ProfileRemoteDataSource implements ProfileRemoteDataSourceHelper {

    private ProfileApiInterface apiInterface;

    @Inject
    public ProfileRemoteDataSource(ProfileApiInterface apiInterface) {
        this.apiInterface = apiInterface;
    }

    @Override
    public Single<ApiSingleResponse<ProfileInfo>> profile(String userID) {
        return apiInterface.getProfileData(userID);
    }

    @Override
    public Single<ApiSingleResponse<ProfileInfo>> updateProfile(ProfileInfo profile) {
        return apiInterface.updateProfile(profile);
    }

    @Override
    public Single<ApiBaseResponse> changePassword(ProfileInfo profile) {
        return apiInterface.changePassword(profile);
    }

    @Override
    public Single<ApiBaseResponse> disable2Fa(ProfileInfo profile) {
        return apiInterface.disable2Fa(profile);
    }
}
