package com.android.l2l.twolocal.coin;

import com.android.l2l.twolocal.model.Wallet
import java.math.BigDecimal;


interface Coin {

    fun toFiat(currencyAmount: String): BigDecimal
    fun toCurrency(fiatAmount: String): BigDecimal

    fun convertDollarToCurrency(dollarAmount: String): BigDecimal
    fun convertCurrencyDollar(currencyAmount: String): BigDecimal

    fun convertCurrencyEuro(currencyAmount: String): BigDecimal
    fun convertEuroToCurrency(euroAmount: String): BigDecimal

    fun convertWeiToNormal(currencyAmount: String?): BigDecimal
    fun convertNormalToWei(currencyAmount: String?): BigDecimal
}

interface DefaultWallet {
    fun getDefaultWallet(address: String, uniqueKey: String): Wallet
    fun transactionScan(transactionHashCode: String): String
}