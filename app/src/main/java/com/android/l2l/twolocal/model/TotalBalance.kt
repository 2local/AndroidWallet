package com.android.l2l.twolocal.model

import com.android.l2l.twolocal.utils.CommonUtils

data class TotalBalance(val currency: String? = null, val balance: String? = null, val fiatBalance: String? = null, val showAmount: Boolean = true){


    var balancePriceFormatter: String =  CommonUtils.formatToDecimalPriceSixDigitsOptional(CommonUtils.stringToBigDecimal(balance))
    var fiatBalancePriceFormatter: String =  CommonUtils.formatToDecimalPriceSixDigits(CommonUtils.stringToBigDecimal(fiatBalance))
}