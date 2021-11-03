package com.android.l2l.twolocal.model.enums;

import com.android.l2l.twolocal.coin.BinanceCoin;
import com.android.l2l.twolocal.coin.EthereumCoin;
import com.android.l2l.twolocal.coin.TwoLocalCoin;

public enum TokenExchangeRatePairs {
    TwoLC(TwoLocalCoin.BITRUE_EXCHANGE_RATE_PAIR_2LC_USDT),
    ETHEREUM(EthereumCoin.BITRUE_EXCHANGE_RATE_PAIR_ETH_USDT),
    BINANCE(BinanceCoin.BITRUE_EXCHANGE_RATE_PAIR_BNB_USDT);


    private final String pair;

    TokenExchangeRatePairs(String pair) {
        this.pair = pair;
    }

    public String getPair() {
        return pair;
    }

}



