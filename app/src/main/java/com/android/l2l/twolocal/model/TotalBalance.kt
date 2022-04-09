package com.android.l2l.twolocal.model

import com.android.l2l.twolocal.utils.PriceFormatUtils

data class TotalBalance(val currency: String? = null, val balance: String? = null, val fiatBalance: String? = null, val showAmount: Boolean = true){


    var balancePriceFormatter: String =  PriceFormatUtils.formatToDecimalPriceSixDigitsOptional(
        PriceFormatUtils.stringToBigDecimal(balance))
    var fiatBalancePriceFormatter: String =  PriceFormatUtils.formatToDecimalPriceSixDigits(
        PriceFormatUtils.stringToBigDecimal(fiatBalance))
}