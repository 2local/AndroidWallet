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
class BinanceCoinTest{

    @Before
    fun setup() {
        PowerMockito.mockStatic(TextUtils::class.java)
        PowerMockito.`when`(TextUtils.isEmpty(ArgumentMatchers.any(CharSequence::class.java))).thenAnswer { invocation ->
            val a = invocation.arguments[0] as? CharSequence
            a?.isEmpty() ?: true
        }
    }

    @Test
    fun `bnb to fiat usd`() {
        val coinPrice = CoinExchangeRate(BinanceCoin.BITRUE_EXCHANGE_RATE_PAIR_BNB_USDT,400.0032, 0.0)
        val binanceCoin = BinanceCoin(FiatType.USD, coinPrice)

        val result1 = binanceCoin.toFiat("1")
        val expecting1 = BigDecimal("400.0032")
        Truth.assertThat(result1).isEqualTo(expecting1)

        val result22 = binanceCoin.toFiat("0")
        val expecting22 = BigDecimal("0.0000")
        Truth.assertThat(result22).isEqualTo(expecting22)

        val result2 = binanceCoin.toFiat("2")
        val expecting2 = BigDecimal("800.0064")
        Truth.assertThat(result2).isEqualTo(expecting2)

        val result3 = binanceCoin.toFiat("2.2")
        val expecting3 = BigDecimal("880.00704")
        Truth.assertThat(result3).isEqualTo(expecting3)

        val result4 = binanceCoin.toFiat("2.8")
        val expecting4 = BigDecimal("1120.00896")
        Truth.assertThat(result4).isEqualTo(expecting4)

        val result5 = binanceCoin.toFiat("")
        val expecting5 = BigDecimal("0.00000")
        Truth.assertThat(result5).isEqualTo(expecting5)

    }

    @Test
    fun `bnb to fiat eur`() {
        val coinPrice = CoinExchangeRate(BinanceCoin.BITRUE_EXCHANGE_RATE_PAIR_BNB_USDT,0.0, 256.32)
        val binanceCoin = BinanceCoin(FiatType.EUR, coinPrice)

        val result1 = binanceCoin.toFiat("1")
        val expecting1 = BigDecimal("256.32")
        Truth.assertThat(result1).isEqualTo(expecting1)

        val result22 = binanceCoin.toFiat("0")
        val expecting22 = BigDecimal("0.00")
        Truth.assertThat(result22).isEqualTo(expecting22)

        val result2 = binanceCoin.toFiat("2")
        val expecting2 = BigDecimal("512.64")
        Truth.assertThat(result2).isEqualTo(expecting2)

        val result3 = binanceCoin.toFiat("2.2")
        val expecting3 = BigDecimal("563.904")
        Truth.assertThat(result3).isEqualTo(expecting3)

        val result4 = binanceCoin.toFiat("2.8")
        val expecting4 = BigDecimal("717.696")
        Truth.assertThat(result4).isEqualTo(expecting4)

        val result44 = binanceCoin.toFiat("0.1")
        val expecting44 = BigDecimal("25.632")
        Truth.assertThat(result44).isEqualTo(expecting44)

        val result5 = binanceCoin.toFiat("")
        val expecting5 = BigDecimal("0.000")
        Truth.assertThat(result5).isEqualTo(expecting5)
    }

    @Test
    fun `eur to bnb`() {
        val coinPrice = CoinExchangeRate(BinanceCoin.BITRUE_EXCHANGE_RATE_PAIR_BNB_USDT,0.0, 500.5)
        val binanceCoin = BinanceCoin(FiatType.EUR, coinPrice)

        val result1 = binanceCoin.toCurrency("157.88")
        val expecting1 = BigDecimal("0.3154445555")
        Truth.assertThat(result1).isEqualTo(expecting1)

        val result2 = binanceCoin.toCurrency("16")
        val expecting2 = BigDecimal("0.0319680320")
        Truth.assertThat(result2).isEqualTo(expecting2)

        val result3 = binanceCoin.toCurrency("1632")
        val expecting3 = BigDecimal("3.2607392608")
        Truth.assertThat(result3).isEqualTo(expecting3)

        val result4 = CommonUtils.formatToDecimalPriceSixDigits(binanceCoin.toCurrency("1622"))
        val expecting4 = CommonUtils.formatToDecimalPriceSixDigits(BigDecimal("3.240759"))
        Truth.assertThat(result4).isEqualTo(expecting4)

        val result44 = CommonUtils.formatToDecimalPriceSixDigits(binanceCoin.toCurrency("0"))
        val expecting44 = BigDecimal("0.00").toString()
        Truth.assertThat(result44).isEqualTo(expecting44)

        val result444 = CommonUtils.formatToDecimalPriceSixDigits(binanceCoin.toCurrency(""))
        val expecting444 = BigDecimal("0.00").toString()
        Truth.assertThat(result444).isEqualTo(expecting444)

        val result5 = CommonUtils.formatToDecimalPriceSixDigits(binanceCoin.toCurrency("10"))
        val expecting5 = CommonUtils.formatToDecimalPriceSixDigits(BigDecimal("0.01998"))
        Truth.assertThat(result5).isEqualTo(expecting5)

        val result6 = CommonUtils.formatToDecimalPriceSixDigits(binanceCoin.toCurrency("10.101"))
        val expecting6 = CommonUtils.formatToDecimalPriceSixDigits(BigDecimal("0.020181"))
        Truth.assertThat(result6).isEqualTo(expecting6)
    }

    @Test
    fun `usd to bnb`() {
        val coinPrice = CoinExchangeRate(BinanceCoin.BITRUE_EXCHANGE_RATE_PAIR_BNB_USDT,420.0, 0.0)
        val binanceCoin = BinanceCoin(FiatType.USD, coinPrice)

        val result1 = binanceCoin.toCurrency("1657.88")
        val expecting1 = BigDecimal("3.9473333334")
        Truth.assertThat(result1).isEqualTo(expecting1)

        val result2 = binanceCoin.toCurrency("16")
        val expecting2 = BigDecimal("0.0380952381")
        Truth.assertThat(result2).isEqualTo(expecting2)

        val result3 = binanceCoin.toCurrency("16322")
        val expecting3 = BigDecimal("38.8619047620")
        Truth.assertThat(result3).isEqualTo(expecting3)

        val result4 = CommonUtils.formatToDecimalPriceSixDigits(binanceCoin.toCurrency("16322"))
        val expecting4 = CommonUtils.formatToDecimalPriceSixDigits(BigDecimal("38.861904"))
        Truth.assertThat(result4).isEqualTo(expecting4)

        val result44 = CommonUtils.formatToDecimalPriceSixDigits(binanceCoin.toCurrency("0"))
        val expecting44 = BigDecimal("0.00").toString()
        Truth.assertThat(result44).isEqualTo(expecting44)

        val result444 = CommonUtils.formatToDecimalPriceSixDigits(binanceCoin.toCurrency(""))
        val expecting444 = BigDecimal("0.00").toString()
        Truth.assertThat(result444).isEqualTo(expecting444)

        val result5 = CommonUtils.formatToDecimalPriceSixDigits(binanceCoin.toCurrency("10"))
        val expecting5 = CommonUtils.formatToDecimalPriceSixDigits(BigDecimal("0.023809"))
        Truth.assertThat(result5).isEqualTo(expecting5)

        val result6 = CommonUtils.formatToDecimalPriceSixDigits(binanceCoin.toCurrency("10.101"))
        val expecting6 = CommonUtils.formatToDecimalPriceSixDigits(BigDecimal("0.02405"))
        Truth.assertThat(result6).isEqualTo(expecting6)
    }
}