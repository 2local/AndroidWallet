package com.android.l2l.twolocal.coin

import com.android.l2l.twolocal.model.CoinExchangeRate
import com.android.l2l.twolocal.model.enums.CryptoCurrencyType
import com.android.l2l.twolocal.model.enums.FiatType

object CoinHelper {

    fun getCoin(walletType: CryptoCurrencyType, currencyType: FiatType?, exchangeRateList: List<CoinExchangeRate>?): Coin {
        return when (walletType) {
            CryptoCurrencyType.ETHEREUM -> EthereumCoin(currencyType, getCoinExchangeRate(exchangeRateList, CryptoCurrencyType.ETHEREUM.symbol))
            CryptoCurrencyType.BINANCE -> BinanceCoin(currencyType, getCoinExchangeRate(exchangeRateList, CryptoCurrencyType.BINANCE.symbol))
            CryptoCurrencyType.TwoLC -> TwoLocalCoin(currencyType, getCoinExchangeRate(exchangeRateList, CryptoCurrencyType.TwoLC.symbol))
            else -> TwoLocalCoin(currencyType, getCoinExchangeRate(exchangeRateList, CryptoCurrencyType.TwoLC.symbol))
        }
    }

    fun getContractAddress(walletType: CryptoCurrencyType): String? {
        return if (walletType == CryptoCurrencyType.TwoLC) TwoLocalCoin.TWOlc_WALLET_CONTRACT else ""
    }

    private fun getCoinExchangeRate(exchangeRateList: List<CoinExchangeRate>?, targetSymbol: String): CoinExchangeRate? {
        return exchangeRateList?.find { it.symbol.startsWith(targetSymbol)}
    }
}