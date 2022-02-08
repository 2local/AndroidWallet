package com.android.l2l.twolocal.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Company(
    @SerializedName("companies")
    var companies: List<MarketPlace>
) : Parcelable

@Parcelize
data class MarketPlace(
    @SerializedName("company_name")
    var companyName: String? = "",
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("latitude")
    var latitude: String?,
    @SerializedName("longitude")
    var longitude: String?,
    @SerializedName("location")
    var location: String = "",
    @SerializedName("cci")
    var cci: String? = "",
    @SerializedName("sustainable")
    var sustainable: String? = "",
    @SerializedName("representative")
    var representative: String? = "",
    @SerializedName("address")
    var address: String? = "",
    @SerializedName("reserve")
    var reserve: String? = "",
    @SerializedName("status")
    var status: String? = "",
    @SerializedName("updated_at")
    var updatedAt: String? = "",
    @SerializedName("website_url")
    var websiteUrl: String? = "",
    @SerializedName("created_at")
    var createdAt: String? = ""
) : Parcelable