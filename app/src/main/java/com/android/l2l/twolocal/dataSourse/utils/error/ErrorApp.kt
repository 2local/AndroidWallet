package com.android.l2l.twolocal.dataSourse.utils.error
import com.google.gson.annotations.SerializedName


data class ErrorApp(
        @SerializedName("timestamp")
        val timestamp: String,
        @SerializedName("path")
        val path: String,
        @SerializedName("status")
        var status: Int,
        @SerializedName("error")
        val error: String,
        @SerializedName("message")
        val message: String,
        @SerializedName("requestId")
        val requestId: String
)
