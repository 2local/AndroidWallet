package com.android.l2l.twolocal.model.mapper;

import com.android.l2l.twolocal.coin.EthereumCoin;
import com.android.l2l.twolocal.model.WalletTransactionHistory;
import com.android.l2l.twolocal.model.enums.CryptoCurrencyType;

import org.web3j.protocol.core.methods.response.EthSendTransaction;

import java.math.BigInteger;

import static com.android.l2l.twolocal.utils.constants.AppConstants.TRANSACTION_PENDING;

public class Mapper_EthSendTransaction_To_WalletTransactionHistory {


    public WalletTransactionHistory mapperToWalletTransactionHistory(EthSendTransaction receipt, BigInteger amount, String from, String to, CryptoCurrencyType walletType) {
        WalletTransactionHistory walletTransactionHistory = new WalletTransactionHistory();
        walletTransactionHistory.setTransactionHash(receipt.getTransactionHash());
        walletTransactionHistory.setHash(receipt.getTransactionHash());
        walletTransactionHistory.setFrom(from);
        walletTransactionHistory.setTo(to);
        walletTransactionHistory.setType(walletType);
        walletTransactionHistory.setValue(amount.toString());
        walletTransactionHistory.setTxreceiptStatus(TRANSACTION_PENDING);
        walletTransactionHistory.setTimeStamp(String.valueOf((int) (System.currentTimeMillis() / 1000)));
        return walletTransactionHistory;
    }
}