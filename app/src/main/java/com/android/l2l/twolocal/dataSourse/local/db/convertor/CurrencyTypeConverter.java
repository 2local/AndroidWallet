package com.android.l2l.twolocal.dataSourse.local.db.convertor;


import com.android.l2l.twolocal.model.enums.FiatType;

import org.greenrobot.greendao.converter.PropertyConverter;

public class CurrencyTypeConverter implements PropertyConverter<FiatType, String> {
    @Override
    public FiatType convertToEntityProperty(String databaseValue) {
        return FiatType.valueOf(databaseValue);
    }

    @Override
    public String convertToDatabaseValue(FiatType entityProperty) {
        return entityProperty.name();
    }
}