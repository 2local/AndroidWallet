package com.android.l2l.twolocal.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class CoinExchangeRate implements Parcelable {
    @SerializedName("symbol")
    private String symbol;
    @SerializedName("usd")
    private double usd;
    @SerializedName("euro")
    private double euro;

    public CoinExchangeRate(String symbol, double usd, double euro) {
        this.symbol = symbol;
        this.usd = usd;
        this.euro = euro;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getUsd() {
        return usd;
    }

    public void setUsd(double usd) {
        this.usd = usd;
    }

    public double getEuro() {
        return euro;
    }

    public void setEuro(double euro) {
        this.euro = euro;
    }

    public static final Creator<CoinExchangeRate> CREATOR = new Creator<CoinExchangeRate>() {
        @Override
        public CoinExchangeRate createFromParcel(Parcel in) {
            return new CoinExchangeRate(in);
        }

        @Override
        public CoinExchangeRate[] newArray(int size) {
            return new CoinExchangeRate[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    protected CoinExchangeRate(Parcel in) {
        symbol = in.readString();
        usd = in.readDouble();
        euro = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(symbol);
        parcel.writeDouble(usd);
        parcel.writeDouble(euro);
    }
}
