package com.android.l2l.twolocal.model.response

import com.google.gson.annotations.SerializedName

data class ExchangeRateResponse(
        @SerializedName("symbol")
        val symbol: String,
        @SerializedName("price")
        val price: String
)