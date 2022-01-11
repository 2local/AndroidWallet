package com.android.l2l.twolocal.dataSourse.repository.exchangeRate


import com.android.l2l.twolocal.model.CoinExchangeRate
import com.android.l2l.twolocal.model.response.ExchangeRateBitrueResponse
import com.android.l2l.twolocal.model.response.ExchangeRateLatokenResponse
import io.reactivex.Single


interface ExchangeRateRepositoryHelper {

    fun getCoinExchangeRateFromBitrue(symbol: String): Single<ExchangeRateBitrueResponse>
    fun getCoinExchangeRateFromLatoken(latokenCoinID: String, latokenPairID: String): Single<ExchangeRateLatokenResponse>
    fun saveCoinExchangeRate(coinsExchangeRate: List<CoinExchangeRate>)
}