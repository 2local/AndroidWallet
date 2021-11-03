package com.android.l2l.twolocal.coin

import android.os.Parcelable
import com.android.l2l.twolocal.dataSourse.remote.api.ApiConstants
import com.android.l2l.twolocal.model.CoinExchangeRate
import com.android.l2l.twolocal.model.Wallet
import com.android.l2l.twolocal.model.enums.CryptoCurrencyType
import com.android.l2l.twolocal.model.enums.FiatType
import com.android.l2l.twolocal.utils.CommonUtils
import kotlinx.parcelize.Parcelize
import org.web3j.utils.Convert
import java.math.BigDecimal
import java.math.RoundingMode

@Parcelize
data class EthereumCoin(val currency: FiatType? = null, val exchangeRate: CoinExchangeRate? = null) : Coin, Parcelable {

    companion object: DefaultWallet{
        const val WALLET_NAME = "Ethereum"
        const val WALLET_SYMBOL = "ETH"
        const val WALLET_NETWORK = "ERC-20"
        const val BITRUE_EXCHANGE_RATE_PAIR_ETH_USDT = "ETHUSDT"

        override fun getDefaultWallet(address: String, uniqueKey: String): Wallet {
            return Wallet(WALLET_NAME, CryptoCurrencyType.ETHEREUM, 0, FiatType.USD, address, uniqueKey)
        }
        override fun transactionScan(transactionHashCode: String): String {
            return ApiConstants.ETHER_TRANSACTION_DETAIL_URL + transactionHashCode
        }
    }

    override fun toFiat(currencyAmount: String): BigDecimal {
        if (currency == FiatType.EUR)
            return convertCurrencyEuro(currencyAmount) else if (currency == FiatType.USD) return convertCurrencyDollar(currencyAmount)
        return BigDecimal("0.0")
    }


    override fun toCurrency(fiatAmount: String): BigDecimal {
        if (currency == FiatType.EUR)
            return convertEuroToCurrency(fiatAmount) else if (currency == FiatType.USD) return convertDollarToCurrency(fiatAmount)
        return BigDecimal("0.0")
    }


    override fun convertDollarToCurrency(dollarAmount: String): BigDecimal {
        try {
            if (exchangeRate != null && exchangeRate.usd != 0.0)
                return CommonUtils.stringToBigDecimal(dollarAmount).divide(BigDecimal.valueOf(exchangeRate.usd), 10, RoundingMode.CEILING)
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
        return BigDecimal("0.0")
    }

    override fun convertCurrencyDollar(currencyAmount: String): BigDecimal {
        try {
            if (exchangeRate != null && exchangeRate.usd != 0.0)
                return BigDecimal.valueOf(exchangeRate.usd).multiply(CommonUtils.stringToBigDecimal(currencyAmount))
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
        return BigDecimal("0.0")
    }

    override fun convertCurrencyEuro(currencyAmount: String): BigDecimal {
        try {
            if (exchangeRate != null && exchangeRate.euro != 0.0)
                return BigDecimal.valueOf(exchangeRate.euro).multiply(CommonUtils.stringToBigDecimal(currencyAmount))
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
        return BigDecimal("0.0")
    }

    override fun convertEuroToCurrency(euroAmount: String): BigDecimal {
        try {
            if (exchangeRate != null && exchangeRate.euro != 0.0)
                return CommonUtils.stringToBigDecimal(euroAmount).divide(BigDecimal.valueOf(exchangeRate.euro), 10, RoundingMode.CEILING)
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
        return BigDecimal("0.0")
    }

    override fun convertWeiToNormal(currencyAmount: String?): BigDecimal {
        return if (currencyAmount?.isNotBlank() == true) Convert.fromWei(currencyAmount, Convert.Unit.ETHER) else BigDecimal("0.0")
    }

    override fun convertNormalToWei(currencyAmount: String?): BigDecimal {
        return if (currencyAmount?.isNotBlank() == true) Convert.toWei(currencyAmount, Convert.Unit.ETHER) else BigDecimal("0.0")
    }
}