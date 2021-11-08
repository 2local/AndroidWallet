package com.android.l2l.twolocal.model.response;

import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.web3j.utils.Convert;

public class BSCBalanceResponse {

    @Expose
    @SerializedName("status")
    private String status;
    @Expose
    @SerializedName("message")
    private String message;
    @Expose
    @SerializedName("result")
    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDecimalResult() {
        if (!TextUtils.isEmpty(result))
            return String.valueOf(Convert.fromWei(String.valueOf(Long.parseLong(result.replace("0x",""), 16)), Convert.Unit.GWEI));
        return "0";
    }
}
