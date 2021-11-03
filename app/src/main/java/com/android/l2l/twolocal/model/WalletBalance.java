package com.android.l2l.twolocal.model;

import com.google.gson.annotations.SerializedName;

public class WalletBalance {

    @SerializedName("code")
    private String code;
    @SerializedName("balance")
    private String balance;

    public WalletBalance(String balance, String code) {
        this.code = code;
        this.balance = balance;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
}
