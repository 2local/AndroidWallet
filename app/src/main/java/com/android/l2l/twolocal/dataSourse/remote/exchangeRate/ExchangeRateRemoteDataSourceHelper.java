package com.android.l2l.twolocal.dataSourse.remote.exchangeRate;//package com.android.l2l.twolocal.dataSourse.remote.l2l;


import com.android.l2l.twolocal.model.response.ExchangeRateResponse;

import io.reactivex.Single;

public interface ExchangeRateRemoteDataSourceHelper {

    Single<ExchangeRateResponse> getCoinsExchangeRate(String symbol);

}
