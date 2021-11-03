package com.android.l2l.twolocal.coin

import android.text.TextUtils
import com.android.l2l.twolocal.model.CoinExchangeRate
import com.android.l2l.twolocal.model.enums.FiatType
import com.android.l2l.twolocal.utils.CommonUtils
import com.google.common.truth.Truth
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import java.math.BigDecimal

@RunWith(PowerMockRunner::class)
@PrepareForTest(TextUtils::class)
class TwoLocalCoinTest{

    @Before
    fun setup() {
        PowerMockito.mockStatic(TextUtils::class.java)
        PowerMockito.`when`(TextUtils.isEmpty(ArgumentMatchers.any(CharSequence::class.java))).thenAnswer { invocation ->
            val a = invocation.arguments[0] as? CharSequence
            a?.isEmpty() ?: true
        }
    }

    @Test
    fun `2lc to fiat usd`() {
        val coinPrice = CoinExchangeRate(TwoLocalCoin.BITRUE_EXCHANGE_RATE_PAIR_2LC_USDT,0.0032, 0.0)
        val twoLocalCoin = TwoLocalCoin(FiatType.USD, coinPrice)
        val result1 = twoLocalCoin.toFiat("1")
        val expecting1 = BigDecimal("0.0032")
        Truth.assertThat(result1).isEqualTo(expecting1)

        val result22 = twoLocalCoin.toFiat("0")
        val expecting22 = BigDecimal("0.0000")
        Truth.assertThat(result22).isEqualTo(expecting22)

        val result2 = twoLocalCoin.toFiat("2")
        val expecting2 = BigDecimal("0.0064")
        Truth.assertThat(result2).isEqualTo(expecting2)

        val result3 = twoLocalCoin.toFiat("2.2")
        val expecting3 = BigDecimal("0.00704")
        Truth.assertThat(result3).isEqualTo(expecting3)

        val result4 = twoLocalCoin.toFiat("2.8")
        val expecting4 = BigDecimal("0.00896")
        Truth.assertThat(result4).isEqualTo(expecting4)

        val result5 = twoLocalCoin.toFiat("")
        val expecting5 = BigDecimal("0.00000")
        Truth.assertThat(result5).isEqualTo(expecting5)

    }

    @Test
    fun `2lc to fiat eur`() {
        val coinPrice = CoinExchangeRate(TwoLocalCoin.BITRUE_EXCHANGE_RATE_PAIR_2LC_USDT,0.0, 0.0032)
        val twoLocalCoin = TwoLocalCoin(FiatType.EUR, coinPrice)

        val result1 = twoLocalCoin.toFiat("1")
        val expecting1 = BigDecimal("0.0032")
        Truth.assertThat(result1).isEqualTo(expecting1)

        val result22 = twoLocalCoin.toFiat("0")
        val expecting22 = BigDecimal("0.0000")
        Truth.assertThat(result22).isEqualTo(expecting22)

        val result2 = twoLocalCoin.toFiat("2")
        val expecting2 = BigDecimal("0.0064")
        Truth.assertThat(result2).isEqualTo(expecting2)

        val result3 = twoLocalCoin.toFiat("2.2")
        val expecting3 = BigDecimal("0.00704")
        Truth.assertThat(result3).isEqualTo(expecting3)

        val result4 = twoLocalCoin.toFiat("2.8")
        val expecting4 = BigDecimal("0.00896")
        Truth.assertThat(result4).isEqualTo(expecting4)

        val result44 = twoLocalCoin.toFiat("0.1")
        val expecting44 = BigDecimal("0.00032")
        Truth.assertThat(result44).isEqualTo(expecting44)

        val result5 = twoLocalCoin.toFiat("")
        val expecting5 = BigDecimal("0.00000")
        Truth.assertThat(result5).isEqualTo(expecting5)
    }

    @Test
    fun `eur to 2lc`() {
        val coinPrice = CoinExchangeRate(TwoLocalCoin.BITRUE_EXCHANGE_RATE_PAIR_2LC_USDT,0.0, 0.0032)
        val twoLocalCoin = TwoLocalCoin(FiatType.EUR, coinPrice)
        val result1 = twoLocalCoin.toCurrency("1657.88")
        val expecting1 = BigDecimal("518087.5000000000")
        Truth.assertThat(result1).isEqualTo(expecting1)

        val result2 = twoLocalCoin.toCurrency("16")
        val expecting2 = BigDecimal("5000.0000000000")
        Truth.assertThat(result2).isEqualTo(expecting2)

        val result3 = twoLocalCoin.toCurrency("16322")
        val expecting3 = BigDecimal("5100625.0000000000")
        Truth.assertThat(result3).isEqualTo(expecting3)

        val result4 = CommonUtils.formatToDecimalPriceSixDigits(twoLocalCoin.toCurrency("16322"))
        val expecting4 = CommonUtils.formatToDecimalPriceSixDigits(BigDecimal("5100625.000000"))
        Truth.assertThat(result4).isEqualTo(expecting4)

        val result44 = CommonUtils.formatToDecimalPriceSixDigits(twoLocalCoin.toCurrency("0"))
        val expecting44 = BigDecimal("0.00").toString()
        Truth.assertThat(result44).isEqualTo(expecting44)

        val result444 = CommonUtils.formatToDecimalPriceSixDigits(twoLocalCoin.toCurrency(""))
        val expecting444 = BigDecimal("0.00").toString()
        Truth.assertThat(result444).isEqualTo(expecting444)

        val result5 = CommonUtils.formatToDecimalPriceSixDigits(twoLocalCoin.toCurrency("10"))
        val expecting5 = CommonUtils.formatToDecimalPriceSixDigits(BigDecimal("3125.000000"))
        Truth.assertThat(result5).isEqualTo(expecting5)

        val result6 = CommonUtils.formatToDecimalPriceSixDigits(twoLocalCoin.toCurrency("10.101"))
        val expecting6 = CommonUtils.formatToDecimalPriceSixDigits(BigDecimal("3156.562500"))
        Truth.assertThat(result6).isEqualTo(expecting6)
    }

    @Test
    fun `usd to 2lc`() {
        val coinPrice = CoinExchangeRate(TwoLocalCoin.BITRUE_EXCHANGE_RATE_PAIR_2LC_USDT,0.0032, 0.0)
        val twoLocalCoin = TwoLocalCoin(FiatType.USD, coinPrice)

        val result1 = twoLocalCoin.toCurrency("1657.88")
        val expecting1 = BigDecimal("518087.5000000000")
        Truth.assertThat(result1).isEqualTo(expecting1)

        val result2 = twoLocalCoin.toCurrency("16")
        val expecting2 = BigDecimal("5000.0000000000")
        Truth.assertThat(result2).isEqualTo(expecting2)

        val result3 = twoLocalCoin.toCurrency("16322")
        val expecting3 = BigDecimal("5100625.0000000000")
        Truth.assertThat(result3).isEqualTo(expecting3)

        val result4 = CommonUtils.formatToDecimalPriceSixDigits(twoLocalCoin.toCurrency("16322"))
        val expecting4 = CommonUtils.formatToDecimalPriceSixDigits(BigDecimal("5100625.000000"))
        Truth.assertThat(result4).isEqualTo(expecting4)

        val result44 = CommonUtils.formatToDecimalPriceSixDigits(twoLocalCoin.toCurrency("0"))
        val expecting44 = BigDecimal("0.00").toString()
        Truth.assertThat(result44).isEqualTo(expecting44)

        val result444 = CommonUtils.formatToDecimalPriceSixDigits(twoLocalCoin.toCurrency(""))
        val expecting444 = BigDecimal("0.00").toString()
        Truth.assertThat(result444).isEqualTo(expecting444)

        val result5 = CommonUtils.formatToDecimalPriceSixDigits(twoLocalCoin.toCurrency("10"))
        val expecting5 = CommonUtils.formatToDecimalPriceSixDigits(BigDecimal("3125.000000"))
        Truth.assertThat(result5).isEqualTo(expecting5)

        val result6 = CommonUtils.formatToDecimalPriceSixDigits(twoLocalCoin.toCurrency("10.101"))
        val expecting6 = CommonUtils.formatToDecimalPriceSixDigits(BigDecimal("3156.562500"))
        Truth.assertThat(result6).isEqualTo(expecting6)
    }
}