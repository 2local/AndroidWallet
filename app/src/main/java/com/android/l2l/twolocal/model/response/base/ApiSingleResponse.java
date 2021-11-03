package com.android.l2l.twolocal.model.response.base;

import com.google.gson.annotations.SerializedName;


public class ApiSingleResponse<T> extends ApiBaseResponse {

    @SerializedName("record")
    private T record;

    public ApiSingleResponse(String message, int code) {
        super(code, message);
    }

    public ApiSingleResponse(String message) {
        super(message);
    }

    public T getRecord() {
        return record;
    }

    public void setRecord(T record) {
        this.record = record;
    }
}
