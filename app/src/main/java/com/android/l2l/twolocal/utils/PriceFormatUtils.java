package com.android.l2l.twolocal.utils;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public final class PriceFormatUtils {

    private static final String TAG = "CommonUtils";

    private PriceFormatUtils() {
    }


    public static String removeCharactersPriceIfExists(String price) {
        String normalPrice = "0.0";
        if (!TextUtils.isEmpty(price)) {
            price = CommonUtils.toEnglish(price);
            normalPrice = price.replaceAll(",", "");
        }

        if (TextUtils.isEmpty(normalPrice))
            normalPrice = "0.0";
        if (normalPrice.startsWith("."))
            normalPrice = "0" + normalPrice;
        return normalPrice;
    }

    public static BigDecimal stringToBigDecimal(String priceStr) {
        String price = removeCharactersPriceIfExists(priceStr);
        return new BigDecimal(price);
    }


    public static String formatToDecimalPriceTwoDigits(BigDecimal price_long) {
        try {
            NumberFormat numberFormat = DecimalFormat.getNumberInstance(Locale.US);
            DecimalFormat formatter = (DecimalFormat) numberFormat;
            formatter.applyPattern("###,###,##0.00");
            formatter.setRoundingMode(RoundingMode.DOWN);

            return formatter.format(price_long);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return "0.0";

    }

    public static String formatToDecimalPriceSixDigits(BigDecimal price) {
        try {
            NumberFormat numberFormat = DecimalFormat.getNumberInstance(Locale.US);
            DecimalFormat formatter = (DecimalFormat) numberFormat;
            formatter.applyPattern("###,###,##0.00####");
            formatter.setRoundingMode(RoundingMode.DOWN);

            return formatter.format(price);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return "0.0";

    }

    public static String formatToDecimalPriceSixDigitsOptional(BigDecimal price) {
        try {
            NumberFormat numberFormat = DecimalFormat.getNumberInstance(Locale.US);
            DecimalFormat formatter = (DecimalFormat) numberFormat;
            formatter.applyPattern("###,###,##0.######");
            formatter.setRoundingMode(RoundingMode.DOWN);

            return formatter.format(price);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return "0.0";

    }

}
