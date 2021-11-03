package com.android.l2l.twolocal.dataSourse.repository.exchangeRate

import com.android.l2l.twolocal.dataSourse.local.ExchangeRateLocalDataSource
import com.android.l2l.twolocal.dataSourse.remote.exchangeRate.ExchangeRateRemoteDataSourceHelper
import com.android.l2l.twolocal.model.CoinExchangeRate
import com.android.l2l.twolocal.model.response.ExchangeRateResponse
import io.reactivex.Single
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class ExchangeRateRepository @Inject constructor(
    private val remoteDataSource: ExchangeRateRemoteDataSourceHelper,
    private val localDataSource: ExchangeRateLocalDataSource,
) : ExchangeRateRepositoryHelper {

    override fun getCoinExchangeRate(symbol: String): Single<ExchangeRateResponse> {
        return remoteDataSource.getCoinsExchangeRate(symbol).doOnSuccess {
        }
    }

    override fun saveCoinExchangeRate(coinsExchangeRate: List<CoinExchangeRate>) {
        localDataSource.saveCoinsExchangeRates(coinsExchangeRate)
    }

}