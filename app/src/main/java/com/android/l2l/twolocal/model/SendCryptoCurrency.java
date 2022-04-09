package com.android.l2l.twolocal.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.android.l2l.twolocal.coin.Coin;
import com.android.l2l.twolocal.model.enums.CryptoCurrencyType;
import com.android.l2l.twolocal.model.enums.FiatType;
import com.android.l2l.twolocal.utils.PriceFormatUtils;

import java.util.Objects;

public class SendCryptoCurrency implements Parcelable {
    private String address;
    private String amount;
    private String transactionHash;
    private CryptoCurrencyType type;
    private FiatType currency; // dollar/euro
    private Coin coin;

    public SendCryptoCurrency() {
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public CryptoCurrencyType getType() {
        return type;
    }

    public void setType(CryptoCurrencyType type) {
        this.type = type;
    }

    public FiatType getCurrency() {
        return currency;
    }

    public void setCurrency(FiatType currency) {
        this.currency = currency;
    }

    public Coin getCoin() {
        return coin;
    }

    public void setCoin(Coin coin) {
        this.coin = coin;
    }

    public String getAmountPriceFormat() {
        return PriceFormatUtils.formatToDecimalPriceSixDigits(PriceFormatUtils.stringToBigDecimal(getAmount()));
    }

    public String getFiatPrice() {
        if (coin != null)
            return coin.toFiat(getAmount()).toString();
        else
            return "0";
    }

    public String getFiatPriceFormat() {
        if (coin != null)
            return PriceFormatUtils.formatToDecimalPriceSixDigits(coin.toFiat(getAmount()));
        else
            return "0";
    }

    public void setFiatPrice(String fiatPrice) {
        if (coin != null)
            setAmount(coin.toCurrency(fiatPrice).toString());
    }

    public void copyCoinInfo(Wallet wallet) {
        setCoin(wallet.getCoin());
        setCurrency(wallet.getCurrency());
        setType(wallet.getType());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SendCryptoCurrency wallet = (SendCryptoCurrency) o;
        return type == wallet.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    protected SendCryptoCurrency(Parcel in) {
        address = in.readString();
        amount = in.readString();
        int tmpCurrency = in.readInt();
        this.currency = tmpCurrency == -1 ? null : FiatType.values()[tmpCurrency];
        int tmpType = in.readInt();
        this.type = tmpType == -1 ? null : CryptoCurrencyType.values()[tmpType];
        this.coin = in.readParcelable(Coin.class.getClassLoader());
    }


    public static final Creator<SendCryptoCurrency> CREATOR = new Creator<SendCryptoCurrency>() {
        @Override
        public SendCryptoCurrency createFromParcel(Parcel in) {
            return new SendCryptoCurrency(in);
        }

        @Override
        public SendCryptoCurrency[] newArray(int size) {
            return new SendCryptoCurrency[size];
        }
    };


    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(address);
        parcel.writeString(amount);
        parcel.writeInt(this.currency == null ? -1 : this.currency.ordinal());
        parcel.writeInt(this.type == null ? -1 : this.type.ordinal());
        parcel.writeParcelable((Parcelable) this.coin, flags);
    }
}
