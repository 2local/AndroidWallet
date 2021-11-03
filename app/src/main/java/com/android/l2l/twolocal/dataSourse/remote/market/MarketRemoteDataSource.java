package com.android.l2l.twolocal.dataSourse.remote.market;

import com.android.l2l.twolocal.model.Company;
import com.android.l2l.twolocal.model.response.AddMarketPlaceResponse;
import com.android.l2l.twolocal.model.response.base.ApiSingleResponse;

import java.util.HashMap;

import javax.inject.Inject;

import io.reactivex.Single;

public class MarketRemoteDataSource implements MarketRemoteDataSourceHelper {

    private MarketApiInterface apiInterface;

    @Inject
    public MarketRemoteDataSource(MarketApiInterface apiInterface) {
        this.apiInterface = apiInterface;
    }

    @Override
    public Single<ApiSingleResponse<Company>> getMarketPlaces() {
        return apiInterface.getMarketPlaces();
    }

    @Override
    public Single<AddMarketPlaceResponse> addMarketPlace(HashMap<String, String> body) {
        return apiInterface.addMarketPlace(body);
    }
}
