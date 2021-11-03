package com.android.l2l.twolocal.model.mapper;

import com.android.l2l.twolocal.model.WalletTransactionHistory;

import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;

public class Mapper_TransactionReceipt_To_WalletTransactionHistory {

//    public WalletTransactionHistory mapperToWalletTransactionHistory(TransactionReceipt receipt) {
//        WalletTransactionHistory walletTransactionHistory = new WalletTransactionHistory();
//        walletTransactionHistory.setBlockHash(receipt.getBlockHash());
//        walletTransactionHistory.setTransactionHash(receipt.getTransactionHash());
//        walletTransactionHistory.setHash(receipt.getTransactionHash());
//        walletTransactionHistory.setBlockNumber(String.valueOf(receipt.getBlockNumber()));
//        walletTransactionHistory.setFrom(receipt.getFrom());
////        walletTransactionHistory.setTo(receipt.getTo());// for 2lc it get contract address
//        walletTransactionHistory.setTxreceiptStatus(receipt.getStatus());
//        walletTransactionHistory.setTimeStamp(String.valueOf((int) (System.currentTimeMillis() / 1000)));
//        return walletTransactionHistory;
//    }

    public WalletTransactionHistory mapperToWalletTransactionHistory(TransactionReceipt receipt, String finalDestinationAddress, BigInteger amountWei) {
        WalletTransactionHistory walletTransactionHistory = new WalletTransactionHistory();
        walletTransactionHistory.setBlockHash(receipt.getBlockHash());
        walletTransactionHistory.setTransactionHash(receipt.getTransactionHash());
        walletTransactionHistory.setHash(receipt.getTransactionHash());
        walletTransactionHistory.setBlockNumber(String.valueOf(receipt.getBlockNumber()));
        walletTransactionHistory.setFrom(receipt.getFrom());
        walletTransactionHistory.setTo(finalDestinationAddress);
        walletTransactionHistory.setValue(amountWei.toString());
        walletTransactionHistory.setTxreceiptStatus(receipt.getStatus());
        walletTransactionHistory.setTimeStamp(String.valueOf((int) (System.currentTimeMillis() / 1000)));
        return walletTransactionHistory;
    }

}
