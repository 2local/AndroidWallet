package com.android.l2l.twolocal.model;

import com.google.gson.annotations.SerializedName;

public class TwoFAVerify {
    @SerializedName("valid")
    private boolean valid;

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
}
