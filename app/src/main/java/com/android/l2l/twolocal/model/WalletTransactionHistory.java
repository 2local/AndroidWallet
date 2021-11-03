package com.android.l2l.twolocal.model;

import com.android.l2l.twolocal.R;
import com.android.l2l.twolocal.coin.CoinHelper;
import com.android.l2l.twolocal.dataSourse.local.db.convertor.WalletTypeConverter;
import com.android.l2l.twolocal.coin.Coin;
import com.android.l2l.twolocal.model.enums.CryptoCurrencyType;
import com.android.l2l.twolocal.utils.CommonUtils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;

import java.math.BigDecimal;

import static com.android.l2l.twolocal.utils.constants.AppConstants.TRANSACTION_DROPPED;
import static com.android.l2l.twolocal.utils.constants.AppConstants.TRANSACTION_SUCCESS;

@Entity(nameInDb = "WalletTransactionHistory")
public class WalletTransactionHistory {
    @Expose
    @Property
    @SerializedName("blockHash")
    private String blockHash;
    @Expose
    @Id
    @SerializedName("hash")
    private String hash;
    @Property
    @Expose
    @SerializedName("confirmations")
    private String confirmations;
    @Property
    @Expose
    @SerializedName("gasUsed")
    private String gasUsed;
    @Expose
    @Property
    @SerializedName("cumulativeGasUsed")
    private String cumulativeGasUsed;
    @Expose
    @Property
    @SerializedName("contractAddress")
    private String contractAddress;
    @Expose
    @Property
    @SerializedName("input")
    private String input;
    @Expose
    @Property
    @SerializedName("txreceipt_status")
    private String txreceiptStatus;
    @Expose
    @Property
    @SerializedName("isError")
    private String isError;
    @Expose
    @Property
    @SerializedName("gasPrice")
    private String gasPrice;
    @Expose
    @Property
    @SerializedName("gas")
    private String gas;
    @Expose
    @Property
    @SerializedName("value")
    private String value;
    @Expose
    @Property(nameInDb = "toWallet")
    @SerializedName("to")
    private String to;
    @Expose
    @Property(nameInDb = "fromWallet")
    @SerializedName("from")
    private String from;
    @Expose
    @Property
    @SerializedName("transactionIndex")
    private String transactionIndex;
    @Expose
    @Property
    @SerializedName("nonce")
    private String nonce;
    @Property
    @Expose
    @SerializedName("timeStamp")
    private String timeStamp;
    @Expose
    @Property
    @SerializedName("blockNumber")
    private String blockNumber;
    @Expose
    @Property
    @SerializedName("transactionHash")
    @Deprecated
    private String transactionHash;
    @Expose
    @Property
    @SerializedName("tokenName")
    private String tokenName;
    @Expose
    @Property
    @SerializedName("tokenSymbol")
    private String tokenSymbol;
    @Expose
    @Property
    @SerializedName("tokenDecimal")
    private String tokenDecimal;

    @Convert(converter = WalletTypeConverter.class, columnType = String.class)
    @SerializedName("type")
    private CryptoCurrencyType type;

    @Transient
    private boolean isSend;
//    @Transient
//    private String fiatPrice;

    @Generated(hash = 1452898307)
    public WalletTransactionHistory(String blockHash, String hash, String confirmations, String gasUsed,
            String cumulativeGasUsed, String contractAddress, String input, String txreceiptStatus,
            String isError, String gasPrice, String gas, String value, String to, String from,
            String transactionIndex, String nonce, String timeStamp, String blockNumber, String transactionHash,
            String tokenName, String tokenSymbol, String tokenDecimal, CryptoCurrencyType type) {
        this.blockHash = blockHash;
        this.hash = hash;
        this.confirmations = confirmations;
        this.gasUsed = gasUsed;
        this.cumulativeGasUsed = cumulativeGasUsed;
        this.contractAddress = contractAddress;
        this.input = input;
        this.txreceiptStatus = txreceiptStatus;
        this.isError = isError;
        this.gasPrice = gasPrice;
        this.gas = gas;
        this.value = value;
        this.to = to;
        this.from = from;
        this.transactionIndex = transactionIndex;
        this.nonce = nonce;
        this.timeStamp = timeStamp;
        this.blockNumber = blockNumber;
        this.transactionHash = transactionHash;
        this.tokenName = tokenName;
        this.tokenSymbol = tokenSymbol;
        this.tokenDecimal = tokenDecimal;
        this.type = type;
    }

    @Generated(hash = 1166095254)
    public WalletTransactionHistory() {
    }

    public Coin getCoin() {
        return CoinHelper.INSTANCE.getCoin(type, null, null);
    }
//    public String getFiatPrice() {
//        return fiatPrice;
//    }
//
//    public void setFiatPrice(String fiatPrice) {
//        this.fiatPrice = fiatPrice;
//    }

    public boolean isSend() {
        return isSend;
    }

    public void setSend(boolean send) {
        isSend = send;
    }

    public int getTransactionTransferTypeStringResource() {
        if (isSend())
            return R.string.activity_transaction_history_send;
        return R.string.activity_transaction_history_receive;
    }

    public String getConfirmations() {
        return confirmations;
    }

    public void setConfirmations(String confirmations) {
        this.confirmations = confirmations;
    }

    public String getGasUsed() {
        return gasUsed;
    }

    public void setGasUsed(String gasUsed) {
        this.gasUsed = gasUsed;
    }

    public String getCumulativeGasUsed() {
        return cumulativeGasUsed;
    }

    public void setCumulativeGasUsed(String cumulativeGasUsed) {
        this.cumulativeGasUsed = cumulativeGasUsed;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getTxreceiptStatus() {
        if (txreceiptStatus!=null)
            return txreceiptStatus.replace("0x", "").trim();
        else
            return "1";
    }

    public int getTxreceiptStatusString() {
        if (getTxreceiptStatus().equals(TRANSACTION_SUCCESS))
            return R.string.complete;
        else if (getTxreceiptStatus().equals(TRANSACTION_DROPPED))
            return R.string.dropped;
        else
            return R.string.pending;
    }

    public void setTxreceiptStatus(String txreceiptStatus) {
        this.txreceiptStatus = txreceiptStatus;
    }

    public String getIsError() {
        return isError;
    }

    public void setIsError(String isError) {
        this.isError = isError;
    }

    public String getGasPrice() {
        return gasPrice;
    }

    public void setGasPrice(String gasPrice) {
        this.gasPrice = gasPrice;
    }

    public String getGas() {
        return gas;
    }

    public void setGas(String gas) {
        this.gas = gas;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTransactionIndex() {
        return transactionIndex;
    }

    public void setTransactionIndex(String transactionIndex) {
        this.transactionIndex = transactionIndex;
    }

    public String getBlockHash() {
        return blockHash;
    }

    public void setBlockHash(String blockHash) {
        this.blockHash = blockHash;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(String blockNumber) {
        this.blockNumber = blockNumber;
    }

    public CryptoCurrencyType getType() {
        return type;
    }

    public void setType(CryptoCurrencyType type) {
        this.type = type;
    }

    public String getDate() {
        return CommonUtils.convertTimestampTo_dd_MMM_yyyy_HH_mm(timeStamp);
    }

    public String getValuePriceFormatted() {
        return CommonUtils.formatToDecimalPriceSixDigits(getCoin().convertWeiToNormal(getValue()));
    }

    public BigDecimal getNormalValue() {
        return getCoin().convertWeiToNormal(getValue());
    }

    public String getTokenName() {
        return this.tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public String getTokenSymbol() {
        return this.tokenSymbol;
    }

    public void setTokenSymbol(String tokenSymbol) {
        this.tokenSymbol = tokenSymbol;
    }

    public String getTokenDecimal() {
        return this.tokenDecimal;
    }

    public void setTokenDecimal(String tokenDecimal) {
        this.tokenDecimal = tokenDecimal;
    }

    public String getTransactionHash() {
        return this.transactionHash;
    }

    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }

}
