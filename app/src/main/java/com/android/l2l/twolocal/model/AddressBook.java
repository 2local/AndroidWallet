package com.android.l2l.twolocal.model;

import com.android.l2l.twolocal.dataSourse.local.db.convertor.WalletTypeConverter;
import com.android.l2l.twolocal.model.enums.CryptoCurrencyType;
import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

@Entity(nameInDb = "AddressBook")
public class AddressBook {

    @Id
    private Long id;

    @Property
    private String name;

    @Property
    private String wallet_number;

    @Property
    @Convert(converter = WalletTypeConverter.class, columnType = String.class)
    @SerializedName("type")
    private CryptoCurrencyType type;

    public AddressBook(String name, String wallet_number, CryptoCurrencyType type) {
        this.name = name;
        this.wallet_number = wallet_number;
        this.type = type;
    }






    @Generated(hash = 44583721)
    public AddressBook() {
    }






    @Generated(hash = 468337242)
    public AddressBook(Long id, String name, String wallet_number,
            CryptoCurrencyType type) {
        this.id = id;
        this.name = name;
        this.wallet_number = wallet_number;
        this.type = type;
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

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWallet_number() {
        return this.wallet_number;
    }

    public void setWallet_number(String wallet_number) {
        this.wallet_number = wallet_number;
    }
}
