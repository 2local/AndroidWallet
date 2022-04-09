package com.android.l2l.twolocal.dataSourse.remote.auth;


import com.android.l2l.twolocal.model.ProfileInfo;
import com.android.l2l.twolocal.model.TwoFAVerify;
import com.android.l2l.twolocal.model.request.LoginRequest;
import com.android.l2l.twolocal.model.request.RegisterRequest;
import com.android.l2l.twolocal.model.response.base.ApiSingleResponse;

import io.reactivex.Single;

public interface AuthenticationRemoteDataSourceHelper {

    Single<ApiSingleResponse<ProfileInfo>> login(LoginRequest loginOutput);

    Single<ApiSingleResponse<ProfileInfo>> signUp(RegisterRequest registerOutput);

    Single<ApiSingleResponse<TwoFAVerify>> verify2Fa(String code, String user_id);
}
