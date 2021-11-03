package com.android.l2l.twolocal.dataSourse.repository.market

import com.android.l2l.twolocal.model.Company
import com.android.l2l.twolocal.model.response.AddMarketPlaceResponse
import com.android.l2l.twolocal.model.response.base.ApiSingleResponse
import io.reactivex.Single
import java.util.*

interface MarketRepositoryHelper {

    fun getMarketPlaces(): Single<ApiSingleResponse<Company>>

    fun addMarketPlace(body: HashMap<String, String>): Single<AddMarketPlaceResponse>

}