package com.android.l2l.twolocal.dataSourse.local.prefs;


import com.android.l2l.twolocal.model.ProfileInfo;
import com.android.l2l.twolocal.model.Wallet;
import com.android.l2l.twolocal.model.enums.FiatType;
import com.android.l2l.twolocal.model.CoinExchangeRate;
import com.google.gson.Gson;

import java.util.List;

import javax.inject.Inject;

public class UserSession implements UserSessionHelper {

    private static final String USER_LOGGED_IN_KEY = "loggedIn";
    private static final String CURRENCY_KEY = "currency_v2";
    private static final String BALANCE_SEEN = "balance_seen";
    private static final String API_AUTH_KEY = "api_key";
    private static final String SHOW_INTRO_KEY = "show_intro";

    private static final String USER_PROFILE_KEY_v2 = "USER_PROFILE_KEY_v2";
    private static final String COIN_EXCHANGE_RATE = "COIN_EXCHANGE_RATE";
    private static final String USER_LOCAL_PASSWORD = "USER_LOCAL_PASSWORD";
    private static final String LOGIN_TOUCH_ID_ACTIVE = "LOGIN_TOUCH_ID_ACTIVE";


    private final Session preferences;

    @Inject
    public UserSession(Session session) {
        preferences = session;
    }

    @Override
    public void saveUserLoggedIn() {
        preferences.setPreferenceValue(USER_LOGGED_IN_KEY, true);
    }


    @Override
    public boolean isUserLoggedIn() {
        return preferences.getPreferenceValue(USER_LOGGED_IN_KEY, false);
    }

    @Override
    public void setUserLoggedOut() {
        preferences.setPreferenceValue(USER_LOGGED_IN_KEY, false);
    }

    @Override
    public void clearSession() {
        preferences.clear();
    }

    @Override
    public void setUserLocalPassword(String password) {
        preferences.setPreferenceValue(USER_LOCAL_PASSWORD, password);
    }

    @Override
    public String getUserLocalPassword() {
        return preferences.getPreferenceValue(USER_LOCAL_PASSWORD, null);
    }

    @Override
    public void setActiveLoginTouchID(boolean active) {
        preferences.setPreferenceValue(LOGIN_TOUCH_ID_ACTIVE, active);
    }

    @Override
    public boolean isActiveLoginTouchID() {
        return preferences.getPreferenceValue(LOGIN_TOUCH_ID_ACTIVE, false);
    }

    @Override
    public boolean showIntro() {
        return preferences.getPreferenceValue(SHOW_INTRO_KEY, true);
    }

    @Override
    public void setShowIntro(boolean show) {
        preferences.setPreferenceValue(SHOW_INTRO_KEY, show);
    }

    @Override
    public void saveCurrentCurrency(FiatType currency) {
        preferences.setPreferenceValue(CURRENCY_KEY, currency.toString());
    }

    @Override
    public FiatType getCurrentCurrency() {
        String currencyType = preferences.getPreferenceValue(CURRENCY_KEY, FiatType.USD.toString());
        return FiatType.toEnum(currencyType);
    }


    @Override
    public boolean getBalanceSeen() {
        return preferences.getPreferenceValue(BALANCE_SEEN, true);
    }

    @Override
    public void setBalanceSeen(boolean seen) {

        preferences.setPreferenceValue(BALANCE_SEEN, seen);
    }

    @Override
    public String getApiToken() {
        return preferences.getPreferenceValue(API_AUTH_KEY, "");
    }


    @Override
    public void saveProfileInfo(ProfileInfo profileInfo) {
        preferences.setObject(USER_PROFILE_KEY_v2, profileInfo);
    }

    @Override
    public ProfileInfo getProfileInfo() {
        return preferences.getObject(USER_PROFILE_KEY_v2, ProfileInfo.class);
    }

    @Override
    public String getUserId() {
        ProfileInfo profileInfo = getProfileInfo();
        if (profileInfo != null)
            return String.valueOf(profileInfo.getUser_Id());
        else
            return "";
    }

    @Override
    public List<CoinExchangeRate> getCoinExchangeRates() {
        return preferences.getArrayObject(COIN_EXCHANGE_RATE, CoinExchangeRate[].class);
    }

    @Override
    public void saveCoinExchangeRates(List<CoinExchangeRate> exchangeRate) {
        preferences.setObject(COIN_EXCHANGE_RATE, exchangeRate);
    }


    @Override
    public void saveApiToken(String token) {
        preferences.setPreferenceValue(API_AUTH_KEY, token);
    }
}
