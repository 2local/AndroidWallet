package com.android.l2l.twolocal.dataSourse.remote.currency;

import com.android.l2l.twolocal.model.response.BSCTransactionGasResponse;
import com.android.l2l.twolocal.model.response.EtherTransactionGasResponse;
import com.android.l2l.twolocal.model.response.EtherTransactionResponse;

import javax.inject.Inject;

import io.reactivex.Single;

public class CryptoCurrencyRemoteDataSource implements CryptoCurrencyRemoteDataSourceHelper {

    private final CryptoCurrencyApiInterface apiInterface;

    @Inject
    public CryptoCurrencyRemoteDataSource(CryptoCurrencyApiInterface apiInterface) {
        this.apiInterface = apiInterface;
    }

    @Override
    public Single<EtherTransactionResponse> getEtherTransactions(String walletAddress, String sort,int page,int offset) {
        return  apiInterface.getEtherTransactions(walletAddress, sort,page ,offset);
    }

    @Override
    public Single<EtherTransactionGasResponse> getEtherTransactionsGas() {
        return apiInterface.getEtherTransactionsGas();
    }

    @Override
    public Single<BSCTransactionGasResponse> getBSCTransactionsGas() {
        return apiInterface.getBSCTransactionsGas();
    }

    @Override
    public Single<EtherTransactionResponse> getBSCTransactions(String walletAddress, String contractAddress, String sort, int page, int offset) {
        return  apiInterface.getBSCTransactions(walletAddress,contractAddress, sort,page ,offset);
    }

    @Override
    public Single<EtherTransactionResponse> getBinanceTransactions(String walletAddress, String sort, int page, int offset) {
        return  apiInterface.getBinanceTransactions(walletAddress, sort,page ,offset);
    }
}
