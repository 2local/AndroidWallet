package com.android.l2l.twolocal.dataSourse.remote.market;

import com.android.l2l.twolocal.dataSourse.remote.api.ApiConstants
import com.android.l2l.twolocal.model.Company
import com.android.l2l.twolocal.model.MarketPlace
import com.android.l2l.twolocal.model.response.AddMarketPlaceResponse
import com.android.l2l.twolocal.model.response.base.ApiListResponse
import com.android.l2l.twolocal.model.response.base.ApiSingleResponse
import io.reactivex.Single
import org.bouncycastle.asn1.ocsp.SingleResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import java.util.*


interface MarketApiInterface {

    @POST(ApiConstants.ADD_MARKET_PLACE_ENDPOINT)
    fun addMarketPlace(@Body body: HashMap<String, String>): Single<AddMarketPlaceResponse>

    @GET(ApiConstants.GET_MARKET_PLACE_LIST_ENDPOINT)
    fun getMarketPlaces(): Single<ApiSingleResponse<Company>>

}