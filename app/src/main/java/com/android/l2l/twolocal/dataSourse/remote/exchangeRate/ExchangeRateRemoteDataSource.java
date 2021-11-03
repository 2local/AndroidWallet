package com.android.l2l.twolocal.dataSourse.remote.exchangeRate;

import com.android.l2l.twolocal.model.response.ExchangeRateResponse;

import javax.inject.Inject;

import io.reactivex.Single;

public class ExchangeRateRemoteDataSource implements ExchangeRateRemoteDataSourceHelper {

    private ExchangeRateApiInterface apiInterface;

    @Inject
    public ExchangeRateRemoteDataSource(ExchangeRateApiInterface apiInterface) {
        this.apiInterface = apiInterface;
    }

    @Override
    public Single<ExchangeRateResponse> getCoinsExchangeRate(String symbol) {
        return apiInterface.getCoinsExchangeRate(symbol);
    }


}
