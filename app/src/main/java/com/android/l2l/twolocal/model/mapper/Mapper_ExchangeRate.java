package com.android.l2l.twolocal.model.mapper;

import com.android.l2l.twolocal.model.CoinExchangeRate;
import com.android.l2l.twolocal.model.response.ExchangeRateResponse;


public class Mapper_ExchangeRate {


    public static CoinExchangeRate mapperToCoinExchangeRate(ExchangeRateResponse rate) {
        return new CoinExchangeRate(rate.getSymbol(), Double.parseDouble(rate.getPrice()), Double.parseDouble(rate.getPrice()));
    }
}