package com.android.l2l.twolocal.model;

import com.android.l2l.twolocal.coin.Coin;
import com.android.l2l.twolocal.model.enums.CryptoCurrencyType;
import com.android.l2l.twolocal.utils.CommonUtils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class TransactionGas {
    @Expose
    @SerializedName("LastBlock")
    private String lastBlock;
    @Expose
    @SerializedName("SafeGasPrice")
    private String safeGasPrice;
    @Expose
    @SerializedName("ProposeGasPrice")
    private String proposeGasPrice;
    @Expose
    @SerializedName("FastGasPrice")
    private String fastGasPrice;

    private Coin coin;
    private CryptoCurrencyType GasCryptoCurrencyType;

    public CryptoCurrencyType getGasCryptoCurrencyType() {
        return GasCryptoCurrencyType;
    }

    public void setGasCryptoCurrencyType(CryptoCurrencyType gasCryptoCurrencyType) {
        GasCryptoCurrencyType = gasCryptoCurrencyType;
    }

    public Coin getCoin() {
        return coin;
    }

    public void setCoin(Coin coin) {
        this.coin = coin;
    }

    public String getSafeGasFiatPrice() {
        return CommonUtils.formatToDecimalPriceTwoDigits(coin.toFiat(getSafeGasPrice()));
    }

    public String getFastGasFiatPrice() {
        return CommonUtils.formatToDecimalPriceTwoDigits(coin.toFiat(getFastGasPrice()));
    }

    public String getProposeGasFiatPrice() {
        return CommonUtils.formatToDecimalPriceTwoDigits(coin.toFiat(getProposeGasPrice()));
    }


    public String getLastBlock() {
        return lastBlock;
    }

    public void setLastBlock(String lastBlock) {
        this.lastBlock = lastBlock;
    }

    public String getSafeGasPrice() {
        return safeGasPrice;
    }

    public void setSafeGasPrice(String safeGasPrice) {
        this.safeGasPrice = safeGasPrice;
    }

    public String getProposeGasPrice() {
        return proposeGasPrice;
    }

    public void setProposeGasPrice(String proposeGasPrice) {
        this.proposeGasPrice = proposeGasPrice;
    }

    public String getFastGasPrice() {
        return fastGasPrice;
    }

    public void setFastGasPrice(String fastGasPrice) {
        this.fastGasPrice = fastGasPrice;
    }
}
