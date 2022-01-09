package com.android.l2l.twolocal.dataSourse.remote.exchangeRate;

import com.android.l2l.twolocal.dataSourse.remote.api.ApiConstants
import com.android.l2l.twolocal.model.response.ExchangeRateBitrueResponse
import com.android.l2l.twolocal.model.response.ExchangeRateLatokenResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface ExchangeRateApiInterface {

    @GET(ApiConstants.GET_EXCHANGE_RATE_ENDPOINT_BITRUE)
    fun getCoinsExchangeRateFromBitrue(@Query("symbol") symbol: String): Single<ExchangeRateBitrueResponse>

    @GET(ApiConstants.GET_EXCHANGE_RATE_ENDPOINT_LATOKEN + "{latokenCoinID}/{latokenPairID}")
    fun getCoinsExchangeRateFromLatoken(
        @Path("latokenCoinID") latokenCoinID: String,
        @Path("latokenPairID") latokenPairID: String
    ): Single<ExchangeRateLatokenResponse>
}