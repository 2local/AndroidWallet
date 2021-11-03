package com.android.l2l.twolocal.model.response;

import com.android.l2l.twolocal.model.WalletTransactionHistory;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class EtherTransactionResponse {

    @Expose
    @SerializedName("result")
    private ArrayList<WalletTransactionHistory> result;
    @Expose
    @SerializedName("message")
    private String message;
    @Expose
    @SerializedName("status")
    private String status;

    public ArrayList<WalletTransactionHistory> getResult() {
        return result;
    }

    public void setResult(ArrayList<WalletTransactionHistory> result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
