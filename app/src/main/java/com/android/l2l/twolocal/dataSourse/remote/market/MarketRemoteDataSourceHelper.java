package com.android.l2l.twolocal.dataSourse.remote.market;


import com.android.l2l.twolocal.model.Company;
import com.android.l2l.twolocal.model.response.AddMarketPlaceResponse;
import com.android.l2l.twolocal.model.response.base.ApiSingleResponse;

import java.util.HashMap;

import io.reactivex.Single;
import retrofit2.http.Body;

public interface MarketRemoteDataSourceHelper {

    Single<ApiSingleResponse<Company>> getMarketPlaces();

    Single<AddMarketPlaceResponse> addMarketPlace(@Body HashMap<String, String> body);

}
