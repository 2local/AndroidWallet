package com.android.l2l.twolocal.utils;

import android.text.TextUtils;

import com.android.l2l.twolocal.coin.BinanceCoin;
import com.android.l2l.twolocal.model.Wallet;
import com.android.l2l.twolocal.coin.EthereumCoin;
import com.android.l2l.twolocal.coin.TwoLocalCoin;
import com.android.l2l.twolocal.model.enums.CryptoCurrencyType;

import java.util.ArrayList;

public final class WalletFactory {

    //available wallet
    public static ArrayList<Wallet> getAvailableWalletList() {
        ArrayList<Wallet> walletList = new ArrayList<>();
        walletList.add(getWallet(CryptoCurrencyType.TwoLC,"",""));
        walletList.add(getWallet(CryptoCurrencyType.ETHEREUM,"",""));
        walletList.add(getWallet(CryptoCurrencyType.BINANCE,"",""));
        return walletList;
    }

    public static Wallet getWallet(CryptoCurrencyType walletType, String address, String uniqueKey) {
        if (walletType == CryptoCurrencyType.ETHEREUM)
            return EthereumCoin.Companion.getDefaultWallet(address, uniqueKey);
        else if (walletType == CryptoCurrencyType.TwoLC)
            return TwoLocalCoin.Companion.getDefaultWallet(address, uniqueKey);
        else if (walletType == CryptoCurrencyType.BINANCE)
            return BinanceCoin.Companion.getDefaultWallet(address, uniqueKey);
        else
            return TwoLocalCoin.Companion.getDefaultWallet(address, uniqueKey);
    }


    public static String getTransactionScanUrl(CryptoCurrencyType walletType, String transactionHashCode) throws Exception {
        if (walletType == null || TextUtils.isEmpty(transactionHashCode)) {
            throw new Exception("Invalid inputs");
        }
        if (walletType == CryptoCurrencyType.ETHEREUM)
            return  EthereumCoin.Companion.transactionScan(transactionHashCode);
        else if (walletType == CryptoCurrencyType.TwoLC)
            return  TwoLocalCoin.Companion.transactionScan(transactionHashCode);
        else if (walletType == CryptoCurrencyType.BINANCE)
            return  BinanceCoin.Companion.transactionScan(transactionHashCode);
        else
            return "";
    }

}
