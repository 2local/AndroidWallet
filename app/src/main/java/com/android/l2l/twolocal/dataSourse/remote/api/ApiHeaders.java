package com.android.l2l.twolocal.dataSourse.remote.api;


import com.android.l2l.twolocal.dataSourse.local.prefs.UserSessionHelper;

import org.jetbrains.annotations.Contract;

import javax.inject.Inject;

public class ApiHeaders {

    private UserSessionHelper prefsHelper;

    @Contract(pure = true)
    @Inject
    public ApiHeaders(UserSessionHelper prefsHelper){
        this.prefsHelper = prefsHelper;
    }

    public String getAuthorization() {
        return prefsHelper.getApiToken();
    }

}
