package com.android.l2l.twolocal.dataSourse.local

import com.android.l2l.twolocal.dataSourse.local.prefs.UserSessionHelper
import com.android.l2l.twolocal.model.CoinExchangeRate
import javax.inject.Inject

class ExchangeRateLocalDataSource @Inject constructor(
    private val userSession: UserSessionHelper,
) {
    fun saveCoinsExchangeRates(exchangeRate: List<CoinExchangeRate>) = userSession.saveCoinExchangeRates(exchangeRate)

}