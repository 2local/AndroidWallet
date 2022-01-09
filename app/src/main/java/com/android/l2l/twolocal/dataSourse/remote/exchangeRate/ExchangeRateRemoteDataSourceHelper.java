package com.android.l2l.twolocal.dataSourse.remote.exchangeRate;//package com.android.l2l.twolocal.dataSourse.remote.l2l;


import com.android.l2l.twolocal.model.response.ExchangeRateBitrueResponse;
import com.android.l2l.twolocal.model.response.ExchangeRateLatokenResponse;

import io.reactivex.Single;

public interface ExchangeRateRemoteDataSourceHelper {

    Single<ExchangeRateBitrueResponse> getCoinsExchangeRateFromBitrue(String symbol);
    Single<ExchangeRateLatokenResponse> get2lcExchangeRateFromLatoken(String latokenCoinID ,String latokenPairID);

}
