package com.android.l2l.twolocal.model.output;

import com.google.gson.annotations.SerializedName;

public class LoginOutput {
    @SerializedName("email")
    private String username;
    @SerializedName("password")
    private String password;

    public LoginOutput(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
