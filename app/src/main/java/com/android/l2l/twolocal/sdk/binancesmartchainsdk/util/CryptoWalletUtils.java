package com.android.l2l.twolocal.sdk.binancesmartchainsdk.util;

import android.content.Context;
import android.text.TextUtils;

import com.android.l2l.twolocal.dataSourse.local.db.AppDatabase;
import com.android.l2l.twolocal.model.Wallet;
import com.android.l2l.twolocal.model.enums.CryptoCurrencyType;
import com.android.l2l.twolocal.utils.SecurityUtils;
import com.android.l2l.twolocal.utils.WalletFactory;

import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDUtils;
import org.bitcoinj.wallet.DeterministicKeyChain;
import org.bitcoinj.wallet.DeterministicSeed;
import org.web3j.crypto.Credentials;

import java.io.File;
import java.math.BigInteger;
import java.util.List;

public class CryptoWalletUtils {

    public static String getPrivateKeyFromMnemonic(String mnemonic) {
        String privateKey = null;
        try {
            // BitcoinJ
            DeterministicSeed seed = new DeterministicSeed(mnemonic, null, "", System.currentTimeMillis() / 1000);
            DeterministicKeyChain chain = DeterministicKeyChain.builder().seed(seed).build();
            List<ChildNumber> keyPath = HDUtils.parsePath("M/44H/60H/0H/0/0");
            DeterministicKey key = chain.getKeyByPath(keyPath, true);
            BigInteger privKey = key.getPrivKey();
            // Web3j
            privateKey = privKey.toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return privateKey;
    }

    public static Credentials getWalletCredential(Context context, AppDatabase database, CryptoCurrencyType currencyType) {
//        Credentials credentials = Credentials.create("336b7abaab1bcb3fab4e5b1ac020da587da451d16e4e9869957cd860a17abd37");
        Credentials credentials = null;
        try {
            Wallet wallet = database.getWallet(currencyType);
            if (wallet != null) {
                if (!TextUtils.isEmpty(wallet.getMnemonic())) {
                    String mnemonic = SecurityUtils.getSecureString(wallet.getMnemonic(), wallet.getAddress(), context);
                    credentials = Credentials.create(getPrivateKeyFromMnemonic(mnemonic));
                } else if (!TextUtils.isEmpty(wallet.getPrivateKey())) {
                    String privateKey = SecurityUtils.getSecureString(wallet.getPrivateKey(), wallet.getAddress(), context);
                    credentials = Credentials.create(privateKey);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return credentials;
    }

    public static Wallet storeWallet(AppDatabase database,
                                     Context context,
                                     CryptoCurrencyType currencyType,
                                     String mnemonic,
                                     String privateKey,
                                     String fileName,
                                     String address,
                                     String uniqueKey,
                                     boolean isUserVerifiedMnemonic) {

        Wallet wallet = WalletFactory.getWallet(currencyType, address, uniqueKey);
        try {
            database.deleteWallet(currencyType);

            if (!TextUtils.isEmpty(mnemonic))
                wallet.setMnemonic(SecurityUtils.saveSecureString(mnemonic, address, context));
            if (!TextUtils.isEmpty(privateKey))
                wallet.setPrivateKey(SecurityUtils.saveSecureString(privateKey, address, context));

            wallet.setFileName(fileName);
            wallet.setUserVerifiedMnemonic(isUserVerifiedMnemonic);
            database.saveOrReplaceWallet(wallet);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return wallet;
    }

    public static File getWalletDestinationDirectory(Context context) {
        return new File(context.getFilesDir(), "");
    }
}
