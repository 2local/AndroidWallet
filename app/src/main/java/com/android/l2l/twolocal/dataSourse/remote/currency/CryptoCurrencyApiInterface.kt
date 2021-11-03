package com.android.l2l.twolocal.dataSourse.remote.currency

import com.android.l2l.twolocal.dataSourse.remote.api.ApiConstants
import com.android.l2l.twolocal.model.response.BSCTransactionGasResponse
import com.android.l2l.twolocal.model.response.EtherTransactionGasResponse
import com.android.l2l.twolocal.model.response.EtherTransactionResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query


interface CryptoCurrencyApiInterface {

    @GET(ApiConstants.ETHER_TRANSACTION_LIST)
    fun getEtherTransactions(
        @Query("address") address: String,
        @Query("sort") sort: String,
        @Query("page") page: Int,
        @Query("offset") offset: Int
    ): Single<EtherTransactionResponse>

    @GET(ApiConstants.ETHER_TRANSACTION_GAS)
    fun getEtherTransactionsGas(): Single<EtherTransactionGasResponse>


    @GET(ApiConstants.BSC_TRANSACTION_LIST)
    fun getBSCTransactions(
        @Query("address") address: String,
        @Query("contractaddress") contractaddress: String,
        @Query("sort") sort: String,
        @Query("page") page: Int,
        @Query("offset") offset: Int
    ): Single<EtherTransactionResponse>

    @GET(ApiConstants.BSC_TRANSACTION_GAS)
    fun getBSCTransactionsGas(): Single<BSCTransactionGasResponse>

    @GET(ApiConstants.BINANCE_TRANSACTION_LIST)
    fun getBinanceTransactions(
        @Query("address") address: String,
        @Query("sort") sort: String,
        @Query("page") page: Int,
        @Query("offset") offset: Int
    ): Single<EtherTransactionResponse>
}