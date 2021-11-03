package com.android.l2l.twolocal.model.enums;

import com.android.l2l.twolocal.utils.constants.AppConstants;

public enum FiatType {
    USD(AppConstants.DOLLAR_CURRENCY_KEY, AppConstants.DOLLAR_CURRENCY_SYMBOL),
    EUR(AppConstants.EURO_CURRENCY_KEY, AppConstants.EURO_CURRENCY_SYMBOL);


    private final String name;
    private final String symbol;

    FiatType(String name, String symbol) {
        this.name = name;
        this.symbol = symbol;
    }

    public static FiatType toEnum (String enumString) {
        try {
            return valueOf(enumString);
        } catch (Exception ex) {
            // For error cases
            return USD;
        }
    }

    public String getMyName() {
        return name;
    }
    public String getMySymbol() {
        return symbol;
    }


}



