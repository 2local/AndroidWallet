package com.android.l2l.twolocal.coin;

import com.android.l2l.twolocal.model.enums.FiatType
import com.android.l2l.twolocal.model.enums.CryptoCurrencyType
import com.android.l2l.twolocal.model.CoinExchangeRate
import com.google.common.truth.Truth
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.math.BigDecimal


@RunWith(JUnit4::class)
class CoinHelperTest {


    @Test
    fun `convert 2lc coin currency to usd fiat`() {
        //GIVEN
        val coinsExchangeRate = mutableListOf<CoinExchangeRate>()
        coinsExchangeRate.add(CoinExchangeRate(TwoLocalCoin.BITRUE_EXCHANGE_RATE_PAIR_2LC_USDT, 4.9, 5.6))
        val coin = CoinHelper.getCoin(CryptoCurrencyType.TwoLC, FiatType.USD, coinsExchangeRate)

        //THEN
        Truth.assertThat(coin is TwoLocalCoin)
        Truth.assertThat(coin.toFiat("1")).isEqualTo(BigDecimal("4.9"))
    }

    @Test
    fun `convert 2lc coin usd fiat to currency`() {
        //GIVEN
        val coinsExchangeRate = mutableListOf<CoinExchangeRate>()
        coinsExchangeRate.add(CoinExchangeRate(TwoLocalCoin.BITRUE_EXCHANGE_RATE_PAIR_2LC_USDT, 4.9, 5.6))
        val coin = CoinHelper.getCoin(CryptoCurrencyType.TwoLC, FiatType.USD, coinsExchangeRate)

        //THEN
        Truth.assertThat(coin is TwoLocalCoin)
        Truth.assertThat(coin.toCurrency("4.9")).isEqualTo(BigDecimal("1.0000000000"))
    }

    @Test
    fun `convert ethereum coin currency to usd fiat`() {
        //GIVEN
        val coinsExchangeRate = mutableListOf<CoinExchangeRate>()
        coinsExchangeRate.add(CoinExchangeRate(EthereumCoin.BITRUE_EXCHANGE_RATE_PAIR_ETH_USDT, 4.9, 5.6))
        val coin = CoinHelper.getCoin(CryptoCurrencyType.ETHEREUM, FiatType.USD, coinsExchangeRate)

        //THEN
        Truth.assertThat(coin is TwoLocalCoin)
        Truth.assertThat(coin.toFiat("1")).isEqualTo(BigDecimal("4.9"))
    }

    @Test
    fun `convert ethereum coin usd fiat to currency`() {
        //GIVEN
        val coinsExchangeRate = mutableListOf<CoinExchangeRate>()
        coinsExchangeRate.add(CoinExchangeRate(EthereumCoin.BITRUE_EXCHANGE_RATE_PAIR_ETH_USDT, 4.9, 5.6))
        val coin = CoinHelper.getCoin(CryptoCurrencyType.ETHEREUM, FiatType.USD, coinsExchangeRate)

        //THEN
        Truth.assertThat(coin is TwoLocalCoin)
        Truth.assertThat(coin.toCurrency("4.9")).isEqualTo(BigDecimal("1.0000000000"))
    }

}