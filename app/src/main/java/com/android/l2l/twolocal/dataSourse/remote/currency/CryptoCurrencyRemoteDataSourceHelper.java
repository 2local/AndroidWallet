package com.android.l2l.twolocal.dataSourse.remote.currency;


import com.android.l2l.twolocal.model.response.BSCBalanceResponse;
import com.android.l2l.twolocal.model.response.BSCTransactionGasResponse;
import com.android.l2l.twolocal.model.response.EtherTransactionGasResponse;
import com.android.l2l.twolocal.model.response.EtherTransactionResponse;

import io.reactivex.Single;

public interface CryptoCurrencyRemoteDataSourceHelper {

    Single<EtherTransactionGasResponse> getEtherTransactionsGas();
    Single<EtherTransactionResponse> getEtherTransactions(String walletAddress, String sort, int page,int offset);

    Single<BSCTransactionGasResponse> getBSCTransactionsGas();
    Single<EtherTransactionResponse> getBSCTransactions(String walletAddress,String contractAddress, String sort, int page,int offset);

    Single<EtherTransactionResponse> getBinanceTransactions(String walletAddress, String sort, int page,int offset);

    Single<BSCBalanceResponse> getBSC_BEP20_token_balance(String walletAddress,String contractAddress);
}
