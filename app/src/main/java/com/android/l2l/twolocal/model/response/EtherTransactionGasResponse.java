package com.android.l2l.twolocal.model.response;

import com.android.l2l.twolocal.model.TransactionGas;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EtherTransactionGasResponse {

    @Expose
    @SerializedName("result")
    private TransactionGas result;
    @Expose
    @SerializedName("message")
    private String message;
    @Expose
    @SerializedName("status")
    private String status;

    public TransactionGas getResult() {
        return result;
    }

    public void setResult(TransactionGas result) {
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
