package com.android.l2l.twolocal.model

import com.android.l2l.twolocal.model.enums.FiatType

data class TotalBalance(val currency: FiatType? = null, val balance: String? = null, val showAmount: Boolean = true) {


}