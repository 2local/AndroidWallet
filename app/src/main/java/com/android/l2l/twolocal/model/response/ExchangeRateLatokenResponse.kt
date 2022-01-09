package com.android.l2l.twolocal.model.response

import com.google.firebase.encoders.annotations.Encodable
import com.google.gson.annotations.SerializedName

data class ExchangeRateLatokenResponse(
        var symbol: String,
        @SerializedName("lastPrice")
        val price: String
)