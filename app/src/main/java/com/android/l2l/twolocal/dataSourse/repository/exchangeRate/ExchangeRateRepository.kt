package com.android.l2l.twolocal.dataSourse.repository.exchangeRate

import com.android.l2l.twolocal.dataSourse.local.ExchangeRateLocalDataSource
import com.android.l2l.twolocal.dataSourse.remote.exchangeRate.ExchangeRateRemoteDataSourceHelper
import com.android.l2l.twolocal.model.CoinExchangeRate
import com.android.l2l.twolocal.model.response.ExchangeRateBitrueResponse
import com.android.l2l.twolocal.model.response.ExchangeRateLatokenResponse
import io.reactivex.Single
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class ExchangeRateRepository @Inject constructor(
    private val remoteDataSource: ExchangeRateRemoteDataSourceHelper,
    private val localDataSource: ExchangeRateLocalDataSource,
) : ExchangeRateRepositoryHelper {

    override fun getCoinExchangeRateFromBitrue(symbol: String): Single<ExchangeRateBitrueResponse> {
        return remoteDataSource.getCoinsExchangeRateFromBitrue(symbol).doOnSuccess {
        }
    }

    override fun getCoinExchangeRateFromLatoken(latokenCoinID: String, latokenPairID: String): Single<ExchangeRateLatokenResponse> {
        return remoteDataSource.get2lcExchangeRateFromLatoken(latokenCoinID, latokenPairID).doOnSuccess {
        }
    }

    override fun saveCoinExchangeRate(coinsExchangeRate: List<CoinExchangeRate>) {
        localDataSource.saveCoinsExchangeRates(coinsExchangeRate)
    }

}