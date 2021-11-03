package com.android.l2l.twolocal.model.response

import com.google.gson.annotations.SerializedName


data class AddMarketPlaceResponse(
    @SerializedName("code")
    var code: Int
) {
    data class Record(
        @SerializedName("company")
        var company: Company
    ) {
        data class Company(
            @SerializedName("company_name")
            var companyName: String,
            @SerializedName("created_at")
            var createdAt: String,
            @SerializedName("id")
            var id: Int,
            @SerializedName("latitude")
            var latitude: String,
            @SerializedName("longitude")
            var longitude: String,
            @SerializedName("reserve")
            var reserve: String,
            @SerializedName("status")
            var status: String,
            @SerializedName("updated_at")
            var updatedAt: String,
            @SerializedName("address")
            var websiteUrl: String
        )
    }
}