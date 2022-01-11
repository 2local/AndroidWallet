package com.android.l2l.twolocal.dataSourse.remote.exchangeRate;

import com.android.l2l.twolocal.model.response.ExchangeRateBitrueResponse;
import com.android.l2l.twolocal.model.response.ExchangeRateLatokenResponse;

import javax.inject.Inject;

import io.reactivex.Single;

public class ExchangeRateRemoteDataSource implements ExchangeRateRemoteDataSourceHelper {

    private ExchangeRateApiInterface apiInterface;

    @Inject
    public ExchangeRateRemoteDataSource(ExchangeRateApiInterface apiInterface) {
        this.apiInterface = apiInterface;
    }

    @Override
    public Single<ExchangeRateBitrueResponse> getCoinsExchangeRateFromBitrue(String symbol) {
        return apiInterface.getCoinsExchangeRateFromBitrue(symbol);
    }

    @Override
    public Single<ExchangeRateLatokenResponse> get2lcExchangeRateFromLatoken(String latokenCoinID, String latokenPairID) {
        return apiInterface.getCoinsExchangeRateFromLatoken(latokenCoinID, latokenPairID);
    }


}
