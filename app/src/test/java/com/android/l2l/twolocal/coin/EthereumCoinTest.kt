package com.android.l2l.twolocal.coin

import android.text.TextUtils
import com.android.l2l.twolocal.model.CoinExchangeRate
import com.android.l2l.twolocal.model.enums.FiatType
import com.android.l2l.twolocal.utils.PriceFormatUtils
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
class EthereumCoinTest{

    @Before
    fun setup() {
        PowerMockito.mockStatic(TextUtils::class.java)
        PowerMockito.`when`(TextUtils.isEmpty(ArgumentMatchers.any(CharSequence::class.java))).thenAnswer { invocation ->
            val a = invocation.arguments[0] as? CharSequence
            a?.isEmpty() ?: true
        }
    }

    @Test
    fun `convert ethereum to fiat usd`() {
        val coinPrice = CoinExchangeRate(EthereumCoin.BITRUE_EXCHANGE_RATE_PAIR_ETH_USDT,2555.2, 0.0)
        val ethereumCoin = EthereumCoin(FiatType.USD, coinPrice)

        //condition 1
        val result1 = ethereumCoin.toFiat("1")
        val expecting1 = BigDecimal("2555.2")
        Truth.assertThat(result1).isEqualTo(expecting1)

        //condition 2
        val result2 = ethereumCoin.toFiat("2")
        val expecting2 = BigDecimal("5110.4")
        Truth.assertThat(result2).isEqualTo(expecting2)

        //condition 3
        val result3 = ethereumCoin.toFiat("2.2")
        val expecting3 = BigDecimal("5621.44")
        Truth.assertThat(result3).isEqualTo(expecting3)

        //condition 4
        val result4 = ethereumCoin.toFiat("2.8")
        val expecting4 = BigDecimal("7154.56")
        Truth.assertThat(result4).isEqualTo(expecting4)
    }

    @Test
    fun `convert ethereum to fiat eur`() {
        val coinPrice = CoinExchangeRate(EthereumCoin.BITRUE_EXCHANGE_RATE_PAIR_ETH_USDT,0.0, 2555.7)
        val ethereumCoin = EthereumCoin(FiatType.EUR, coinPrice)

        //condition 1
        val result1 = ethereumCoin.toFiat("1")
        val expecting1 = BigDecimal("2555.7")
        Truth.assertThat(result1).isEqualTo(expecting1)

        //condition 2
        val result2 = ethereumCoin.toFiat("2")
        val expecting2 = BigDecimal("5111.4")
        Truth.assertThat(result2).isEqualTo(expecting2)

        //condition 3
        val result3 = ethereumCoin.toFiat("2.2")
        val expecting3 = BigDecimal("5622.54")
        Truth.assertThat(result3).isEqualTo(expecting3)

        //condition 4
        val result4 = ethereumCoin.toFiat("2.8")
        val expecting4 = BigDecimal("7155.96")
        Truth.assertThat(result4).isEqualTo(expecting4)
    }

    @Test
    fun `eur to ethereum`() {
        val coinPrice = CoinExchangeRate(EthereumCoin.BITRUE_EXCHANGE_RATE_PAIR_ETH_USDT,0.0, 2555.76)
        val ethereumCoin = EthereumCoin(FiatType.EUR, coinPrice)

        //condition 1
        val result1 = ethereumCoin.toCurrency("1657.88")
        val expecting1 = BigDecimal("0.6486837575")
        Truth.assertThat(result1).isEqualTo(expecting1)

        //condition 2
        val result2 = ethereumCoin.toCurrency("16")
        val expecting2 = BigDecimal("0.0062603688")
        Truth.assertThat(result2).isEqualTo(expecting2)

        //condition 3
        val result3 = ethereumCoin.toCurrency("16322")
        val expecting3 = BigDecimal("6.3863586566")
        Truth.assertThat(result3).isEqualTo(expecting3)

        //condition 4
        val result4 = PriceFormatUtils.formatToDecimalPriceSixDigits(ethereumCoin.toCurrency("16322"))
        val expecting4 = PriceFormatUtils.formatToDecimalPriceSixDigits(BigDecimal("6.386358"))
        Truth.assertThat(result4).isEqualTo(expecting4)

        //condition 5
        val result44 = PriceFormatUtils.formatToDecimalPriceSixDigits(ethereumCoin.toCurrency("0"))
        val expecting44 = PriceFormatUtils.formatToDecimalPriceSixDigits(BigDecimal("0.000000"))
        Truth.assertThat(result44).isEqualTo(expecting44)

        //condition 6
        val result444 = PriceFormatUtils.formatToDecimalPriceSixDigits(ethereumCoin.toCurrency(""))
        val expecting444 = PriceFormatUtils.formatToDecimalPriceSixDigits( BigDecimal("0.000000"))
        Truth.assertThat(result444).isEqualTo(expecting444)

        //condition 7
        val result5 = PriceFormatUtils.formatToDecimalPriceSixDigits(ethereumCoin.toCurrency("16322.44"))
        val expecting5 = PriceFormatUtils.formatToDecimalPriceSixDigits(BigDecimal("6.386530"))
        Truth.assertThat(result5).isEqualTo(expecting5)

        //condition 8
        val result6 = PriceFormatUtils.formatToDecimalPriceSixDigits(ethereumCoin.toCurrency("16"))
        val expecting6 = PriceFormatUtils.formatToDecimalPriceSixDigits(BigDecimal("0.006260"))
        Truth.assertThat(result6).isEqualTo(expecting6)
    }

    @Test
    fun `usd to ethereum`() {
        val coinPrice = CoinExchangeRate(EthereumCoin.BITRUE_EXCHANGE_RATE_PAIR_ETH_USDT,2555.76, 0.0)
        val ethereumCoin = EthereumCoin(FiatType.USD, coinPrice)

        //condition 1
        val result1 = ethereumCoin.toCurrency("1657.88")
        val expecting1 = BigDecimal("0.6486837575")
        Truth.assertThat(result1).isEqualTo(expecting1)

        //condition 2
        val result2 = ethereumCoin.toCurrency("16")
        val expecting2 = BigDecimal("0.0062603688")
        Truth.assertThat(result2).isEqualTo(expecting2)

        //condition 3
        val result3 = ethereumCoin.toCurrency("16322")
        val expecting3 = BigDecimal("6.3863586566")
        Truth.assertThat(result3).isEqualTo(expecting3)

        //condition 4
        val result4 = PriceFormatUtils.formatToDecimalPriceSixDigits(ethereumCoin.toCurrency("16322"))
        val expecting4 = BigDecimal("6.386358").toString()
        Truth.assertThat(result4).isEqualTo(expecting4)

        //condition 5
        val result44 = PriceFormatUtils.formatToDecimalPriceSixDigits(ethereumCoin.toCurrency("0"))
        val expecting44 = BigDecimal("0.00").toString()
        Truth.assertThat(result44).isEqualTo(expecting44)

        //condition 7
        val result444 = PriceFormatUtils.formatToDecimalPriceSixDigits(ethereumCoin.toCurrency(""))
        val expecting444 = BigDecimal("0.00").toString()
        Truth.assertThat(result444).isEqualTo(expecting444)

        //condition 8
        val result5 = PriceFormatUtils.formatToDecimalPriceSixDigits(ethereumCoin.toCurrency("16322.44"))
        val expecting5 = BigDecimal("6.38653").toString()
        Truth.assertThat(result5).isEqualTo(expecting5)

        //condition 9
        val result6 = PriceFormatUtils.formatToDecimalPriceSixDigits(ethereumCoin.toCurrency("16"))
        val expecting6 = BigDecimal("0.00626").toString()
        Truth.assertThat(result6).isEqualTo(expecting6)
    }

    @Test
    fun `convert normal ether to wei value`() {
        val ethereumCoin = EthereumCoin(null, null)

        //condition 1
        val result1 = ethereumCoin.convertNormalToWei("1657.88")
        val expecting1 = BigDecimal("1657880000000000000000.00")
        Truth.assertThat(result1).isEqualTo(expecting1)

        //condition 2
        val result11 = ethereumCoin.convertNormalToWei("0.01")
        val expecting11 = BigDecimal("10000000000000000.00")
        Truth.assertThat(result11).isEqualTo(expecting11)

        //condition 3
        val result111 = ethereumCoin.convertNormalToWei("0.41504")
        val expecting111 = BigDecimal("415040000000000000.00000")
        Truth.assertThat(result111).isEqualTo(expecting111)

        //condition 4
        val result2 = ethereumCoin.convertNormalToWei("")
        val expecting2 = BigDecimal("0.0")
        Truth.assertThat(result2).isEqualTo(expecting2)

        //condition 5
        val result3 = ethereumCoin.convertNormalToWei(null)
        val expecting3 = BigDecimal("0.0")
        Truth.assertThat(result3).isEqualTo(expecting3)

    }

    @Test
    fun `convert wei  to normal ether value`() {
        val ethereumCoin = EthereumCoin(null, null)

        //condition 1
        val result1 = ethereumCoin.convertWeiToNormal("1657880000000000000000")
        val expecting1 = BigDecimal("1657.88")
        Truth.assertThat(result1).isEqualTo(expecting1)

        //condition 2
        val result11 = ethereumCoin.convertWeiToNormal("10000000000000000")
        val expecting11 = BigDecimal("0.01")
        Truth.assertThat(result11).isEqualTo(expecting11)

        //condition 3
        val result111 = ethereumCoin.convertWeiToNormal("415040000000000000")
        val expecting111 = BigDecimal("0.41504")
        Truth.assertThat(result111).isEqualTo(expecting111)

        //condition 4
        val result2 = ethereumCoin.convertWeiToNormal("")
        val expecting2 = BigDecimal("0.0")
        Truth.assertThat(result2).isEqualTo(expecting2)

        //condition 5
        val result3 = ethereumCoin.convertWeiToNormal(null)
        val expecting3 = BigDecimal("0.0")
        Truth.assertThat(result3).isEqualTo(expecting3)

    }
}