package com.android.l2l.twolocal.dataSourse.local.prefs;


import com.android.l2l.twolocal.model.ProfileInfo;
import com.android.l2l.twolocal.model.Wallet;
import com.android.l2l.twolocal.model.enums.FiatType;
import com.android.l2l.twolocal.model.CoinExchangeRate;

import java.util.List;

public interface UserSessionHelper {
    void saveUserLoggedIn();
    boolean isUserLoggedIn();
    void setUserLoggedOut();

    void clearSession();

    void setUserLocalPassword(String password);
    String getUserLocalPassword();

    void setActiveLoginTouchID(boolean active);
    boolean isActiveLoginTouchID();

    boolean showIntro();
    void setShowIntro(boolean show);

    void saveCurrentCurrency(FiatType currency);
    FiatType getCurrentCurrency();


    void setBalanceSeen(boolean seen);
    boolean getBalanceSeen();

    void saveApiToken(String token);
    String getApiToken();

    void saveProfileInfo(ProfileInfo profileInfo);
    ProfileInfo getProfileInfo();

    String getUserId();

    List<CoinExchangeRate> getCoinExchangeRates();
    void saveCoinExchangeRates(List<CoinExchangeRate> exchangeRate);

    boolean showInstruction();
    void changeShowInstruction(boolean show);
}
