package com.android.l2l.twolocal.model.response.base;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApiListResponse<T> extends ApiBaseResponse {

    @SerializedName("record")
    private List<T> record;

    public ApiListResponse(String message) {
        super(message);
    }

    public List<T> getRecord() {
        return record;
    }

}
