package com.android.l2l.twolocal.utils;

import android.content.Context;

import com.android.l2l.twolocal.utils.scytale.Crypto;
import com.android.l2l.twolocal.utils.scytale.Options;
import com.android.l2l.twolocal.utils.scytale.Store;

import java.math.BigDecimal;
import java.math.MathContext;

import javax.crypto.SecretKey;

public class SecurityUtils {

    public static  String saveSecureString(String text, String alias, Context context) {
        Store store = new Store(context);
        if (!store.hasKey(alias)) {
            store.generateSymmetricKey(alias, null);
        }

        SecretKey key = store.getSymmetricKey(alias, null);

        Crypto crypto = new Crypto(Options.TRANSFORMATION_SYMMETRIC);

        return crypto.encrypt(text, key);
    }

    public static String getSecureString(String encryptedData, String alias, Context context) {
        Store store = new Store(context);
        if (!store.hasKey(alias)) {
            store.generateSymmetricKey(alias, null);
        }

        SecretKey key = store.getSymmetricKey(alias, null);

        Crypto crypto = new Crypto(Options.TRANSFORMATION_SYMMETRIC);

        return crypto.decrypt(encryptedData, key);
    }

}
