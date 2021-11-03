package com.android.l2l.twolocal.dataSourse.repository.market

import com.android.l2l.twolocal.dataSourse.remote.market.MarketRemoteDataSource
import com.android.l2l.twolocal.model.Company
import com.android.l2l.twolocal.model.MarketPlace
import com.android.l2l.twolocal.model.response.AddMarketPlaceResponse
import com.android.l2l.twolocal.model.response.base.ApiListResponse
import com.android.l2l.twolocal.model.response.base.ApiSingleResponse
import io.reactivex.Single
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*
import javax.inject.Inject

@ExperimentalCoroutinesApi
class MarketRepository @Inject constructor(
    private val remoteDataSource: MarketRemoteDataSource,
) :MarketRepositoryHelper {


    override fun getMarketPlaces(): Single<ApiSingleResponse<Company>> {
        return remoteDataSource.getMarketPlaces()
    }

    override fun addMarketPlace(body: HashMap<String, String>): Single<AddMarketPlaceResponse> {
        return remoteDataSource.addMarketPlace(body)
    }

}