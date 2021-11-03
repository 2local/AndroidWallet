package com.android.l2l.twolocal.dataSourse.local.db.convertor;


import com.android.l2l.twolocal.model.enums.CryptoCurrencyType;

import org.greenrobot.greendao.converter.PropertyConverter;

public class WalletTypeConverter implements PropertyConverter<CryptoCurrencyType, String> {
    @Override
    public CryptoCurrencyType convertToEntityProperty(String databaseValue) {
        return databaseValue == null? CryptoCurrencyType.TwoLC : CryptoCurrencyType.valueOf(databaseValue);
    }

    @Override
    public String convertToDatabaseValue(CryptoCurrencyType entityProperty) {
        return entityProperty == null ? CryptoCurrencyType.TwoLC.name() : entityProperty.name();
    }
}