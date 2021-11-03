package com.android.l2l.twolocal.dataSourse.remote.exchangeRate;

import com.android.l2l.twolocal.dataSourse.remote.api.ApiConstants
import com.android.l2l.twolocal.model.response.ExchangeRateResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query


interface ExchangeRateApiInterface {

    @GET(ApiConstants.GET_EXCHANGE_RATE_ENDPOINT)
    fun getCoinsExchangeRate(@Query("symbol") symbol: String): Single<ExchangeRateResponse>

}