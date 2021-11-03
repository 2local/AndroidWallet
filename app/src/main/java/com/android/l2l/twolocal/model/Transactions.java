package com.android.l2l.twolocal.model;

import android.text.TextUtils;

import com.android.l2l.twolocal.coin.Coin;
import com.android.l2l.twolocal.model.enums.CryptoCurrencyType;
import com.android.l2l.twolocal.utils.CommonUtils;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;

@Entity(nameInDb = "transactions")
public class Transactions {

    @Id(autoincrement = false)
    private Long id;
    @Property
    private String user_id;
    @Property
    private String quantity;
    @Property
    private String status;
    @Property
    private String date;
    @Property
    private String source;
    @Property
    private String time;
    @Property
    private String currency;
    @Property
    private String l2l;


    @Transient
    private CryptoCurrencyType type;

    @Transient
    private transient Coin coin;




    @Generated(hash = 338950345)
    public Transactions() {
    }

    @Generated(hash = 1798710802)
    public Transactions(Long id, String user_id, String quantity, String status, String date,
            String source, String time, String currency, String l2l) {
        this.id = id;
        this.user_id = user_id;
        this.quantity = quantity;
        this.status = status;
        this.date = date;
        this.source = source;
        this.time = time;
        this.currency = currency;
        this.l2l = l2l;
    }

    //use just for income or expose transaction
    public String getFiatPrice() {
        return TextUtils.isEmpty(getQuantity()) ? "0" : coin.toFiat(getQuantity()).toString();
    }

    public Coin getCoin() {
        return coin;
    }

    public void setCoin(Coin coin) {
        this.coin = coin;
    }

    public CryptoCurrencyType getType() {
        return type;
    }

    public void setType(CryptoCurrencyType type) {
        this.type = type;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUser_id() {
        return this.user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getQuantity() {
        return this.quantity;
    }

    public String getQuantityPriceFormatted() {
        return CommonUtils.formatToDecimalPriceTwoDigits(CommonUtils.stringToBigDecimal(getQuantity()));
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSource() {
        return this.source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getL2l() {
        return l2l;
    }

    public String getL2lPriceFormatted() {
        return CommonUtils.formatToDecimalPriceTwoDigits(CommonUtils.stringToBigDecimal(getL2l()));
    }

    public void setL2l(String l2l) {
        this.l2l = l2l;
    }
}
