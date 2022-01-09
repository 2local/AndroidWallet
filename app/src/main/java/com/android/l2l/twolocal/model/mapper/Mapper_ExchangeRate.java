package com.android.l2l.twolocal.model.mapper;

import com.android.l2l.twolocal.model.CoinExchangeRate;
import com.android.l2l.twolocal.model.response.ExchangeRateBitrueResponse;
import com.android.l2l.twolocal.model.response.ExchangeRateLatokenResponse;


public class Mapper_ExchangeRate {


    public static CoinExchangeRate mapperToCoinExchangeRate(ExchangeRateBitrueResponse rate) {
        return new CoinExchangeRate(rate.getSymbol(), Double.parseDouble(rate.getPrice()), Double.parseDouble(rate.getPrice()));
    }

    public static CoinExchangeRate mapperToCoinExchangeRate(ExchangeRateLatokenResponse rate) {
        return new CoinExchangeRate(rate.getSymbol(), Double.parseDouble(rate.getPrice()), Double.parseDouble(rate.getPrice()));
    }
}