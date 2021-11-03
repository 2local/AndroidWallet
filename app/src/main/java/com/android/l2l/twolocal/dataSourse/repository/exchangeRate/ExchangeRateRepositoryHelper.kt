package com.android.l2l.twolocal.dataSourse.repository.exchangeRate


import com.android.l2l.twolocal.model.CoinExchangeRate
import com.android.l2l.twolocal.model.response.ExchangeRateResponse
import io.reactivex.Single


interface ExchangeRateRepositoryHelper {

    fun getCoinExchangeRate(symbol: String): Single<ExchangeRateResponse>
    fun saveCoinExchangeRate(coinsExchangeRate: List<CoinExchangeRate>)
}