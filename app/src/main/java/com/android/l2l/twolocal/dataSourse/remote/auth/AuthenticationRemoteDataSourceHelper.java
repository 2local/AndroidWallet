package com.android.l2l.twolocal.dataSourse.remote.auth;


import com.android.l2l.twolocal.model.ProfileInfo;
import com.android.l2l.twolocal.model.TwoFAVerify;
import com.android.l2l.twolocal.model.output.LoginOutput;
import com.android.l2l.twolocal.model.output.RegisterOutput;
import com.android.l2l.twolocal.model.response.RegisterResponse;
import com.android.l2l.twolocal.model.response.base.ApiSingleResponse;

import io.reactivex.Single;

public interface AuthenticationRemoteDataSourceHelper {

    Single<ApiSingleResponse<ProfileInfo>> login(LoginOutput loginOutput);

    Single<ApiSingleResponse<ProfileInfo>> signUp(RegisterOutput registerOutput);

    Single<ApiSingleResponse<TwoFAVerify>> verify2Fa(String code, String user_id);
}
