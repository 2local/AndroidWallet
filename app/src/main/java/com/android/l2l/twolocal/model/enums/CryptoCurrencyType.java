package com.android.l2l.twolocal.model.enums;

import com.android.l2l.twolocal.R;
import com.android.l2l.twolocal.coin.BinanceCoin;
import com.android.l2l.twolocal.coin.EthereumCoin;
import com.android.l2l.twolocal.coin.TwoLocalCoin;

import java.util.ArrayList;
import java.util.Collections;

public enum CryptoCurrencyType {
    TwoLC(TwoLocalCoin.WALLET_NAME, TwoLocalCoin.WALLET_SYMBOL, TwoLocalCoin.WALLET_NETWORK, R.drawable.ic_local),
    ETHEREUM(EthereumCoin.WALLET_NAME, EthereumCoin.WALLET_SYMBOL,  EthereumCoin.WALLET_NETWORK, R.drawable.img_ethereum),
    BINANCE(BinanceCoin.WALLET_NAME, BinanceCoin.WALLET_SYMBOL,  BinanceCoin.WALLET_NETWORK, R.drawable.img_binance),
    NONE("", "", "",0);


    private final String name;
    private final String network;
    private final String symbol;
    private final int icon;

    CryptoCurrencyType(String name, String symbol, String network, int icon) {
        this.name = name;
        this.icon = icon;
        this.symbol = symbol;
        this.network = network;
    }

    public static ArrayList<CryptoCurrencyType> walletArray() {
        ArrayList<CryptoCurrencyType> items = new ArrayList<>();
        Collections.addAll(items, CryptoCurrencyType.values());
        items.remove(NONE);
        return items;
    }

    public String getMyName() {
        return name;
    }

    public String getMyNetwork() {
        return network;
    }

    public int getIcon() {
        return icon;
    }

    public String getSymbol() {
        return symbol;
    }

}



