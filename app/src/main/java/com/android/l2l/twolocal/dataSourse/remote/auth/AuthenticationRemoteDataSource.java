package com.android.l2l.twolocal.dataSourse.remote.auth;

import com.android.l2l.twolocal.model.ProfileInfo;
import com.android.l2l.twolocal.model.TwoFAVerify;
import com.android.l2l.twolocal.model.output.LoginOutput;
import com.android.l2l.twolocal.model.output.RegisterOutput;
import com.android.l2l.twolocal.model.response.RegisterResponse;
import com.android.l2l.twolocal.model.response.base.ApiSingleResponse;

import java.util.HashMap;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AuthenticationRemoteDataSource implements AuthenticationRemoteDataSourceHelper {

    private AuthenticationApiInterface apiInterface;

    @Inject
    public AuthenticationRemoteDataSource(AuthenticationApiInterface apiInterface) {
        this.apiInterface = apiInterface;
    }


    @Override
    public Single<ApiSingleResponse<ProfileInfo>> login(LoginOutput loginOutput) {
        return apiInterface.login(loginOutput);
    }


    @Override
    public Single<ApiSingleResponse<ProfileInfo>> signUp(RegisterOutput registerOutput) {
        return apiInterface.signUp(registerOutput)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }


    @Override
    public Single<ApiSingleResponse<TwoFAVerify>> verify2Fa(String code, String user_id) {
        HashMap<String, String> mBodyParameterMap = new HashMap<>();
        mBodyParameterMap.put("code", code);
        mBodyParameterMap.put("user_id", user_id);
        return apiInterface.verify2Fa(mBodyParameterMap)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

}
