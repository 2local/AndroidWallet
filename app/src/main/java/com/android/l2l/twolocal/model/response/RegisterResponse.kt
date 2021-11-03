package com.android.l2l.twolocal.model.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
        @SerializedName("code")
        val code: String
)