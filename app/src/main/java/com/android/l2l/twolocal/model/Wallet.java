package com.android.l2l.twolocal.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.android.l2l.twolocal.dataSourse.local.db.convertor.WalletTypeConverter;
import com.android.l2l.twolocal.coin.Coin;
import com.android.l2l.twolocal.model.enums.CryptoCurrencyType;
import com.android.l2l.twolocal.model.enums.FiatType;
import com.android.l2l.twolocal.utils.CommonUtils;
import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;

import java.util.Objects;

import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "Wallet")
public class Wallet implements Parcelable {
    @Id
    @Property(nameInDb = "UNIQUE_ID")
    @SerializedName("uniqueID")
    private String uniqueID; //wallet address for coin/ contract address for token
    @Property(nameInDb = "ADDRESS")
    @SerializedName("address")
    private String address; //wallet address
    @Property(nameInDb = "WALLET_NAME")
    @SerializedName("walletName")
    private String walletName;
    @Property(nameInDb = "AMOUNT")
    @SerializedName("amount")
    private String amount;
    @Property(nameInDb = "TYPE")
    @Convert(converter = WalletTypeConverter.class, columnType = String.class)
    @SerializedName("type")
    private CryptoCurrencyType type;
    @Property(nameInDb = "USER_VERIFIED_MNEMONIC")
    @SerializedName("userVerifiedMnemonic")
    private boolean userVerifiedMnemonic; // only verified wallet show to user
    @Property(nameInDb = "FILE_NAME")
    @SerializedName("etherFileName")
    private String fileName; // key store file name generated by web3
    @Property(nameInDb = "MNEMONIC")
    @SerializedName("mnemonic")
    private String mnemonic;
    @Property(nameInDb = "PRIVATE_KEY")
    @SerializedName("privateKey")
    private String privateKey;
    @Property(nameInDb = "CREATE_WALLET_DATE")
    @SerializedName("createWalletDate")
    private long createWalletDate;
    @Property(nameInDb = "SHOWING_ORDER")
    @SerializedName("showingOrder")
    private int showingOrder; // l2l wallet must be first one in list

    @Transient
    @SerializedName("currency_v2")
    private FiatType currency; // dollar/euro
    @Transient
    @SerializedName("showAmount")
    boolean showAmount = true; //use **** if false for wallet amounts
    @Transient
    @SerializedName("isSelected")
    private boolean isSelected;// use for create wallet list (selected/unselected)

    @Transient
    private transient Coin coin;

    public Wallet(String walletName, CryptoCurrencyType type) {
        this.walletName = walletName;
        this.type = type;
    }

    public Wallet(String walletName, CryptoCurrencyType type, int showingOrder, FiatType currency, String address, String uniqueID) {
        this.walletName = walletName;
        this.address = address;
        this.uniqueID = uniqueID;
        this.amount = "0";
        this.type = type;
        this.createWalletDate = System.currentTimeMillis();
        this.showingOrder = showingOrder;
        this.currency = currency;
    }


    public Wallet(CryptoCurrencyType type) {
        this.type = type;
    }

    public Wallet() {
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public Coin getCoin() {
        return coin;
    }

    public void setCoin(Coin coin) {
        this.coin = coin;
    }

    public boolean isShowAmount() {
        return showAmount;
    }

    public void setShowAmount(boolean showAmount) {
        this.showAmount = showAmount;
    }

    public boolean isUserVerifiedMnemonic() {
        return userVerifiedMnemonic;
    }

    public void setUserVerifiedMnemonic(boolean userVerifiedMnemonic) {
        this.userVerifiedMnemonic = userVerifiedMnemonic;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEtherFileName() {
        return fileName;
    }

    public void setEtherFileName(String etherFileName) {
        this.fileName = etherFileName;
    }

    public String getWalletName() {
        return walletName;
    }

    public void setWalletName(String walletName) {
        this.walletName = walletName;
    }


    public FiatType getCurrency() {
        return currency;
    }

    public void setCurrency(FiatType currency) {
        this.currency = currency;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAmountPriceFormat() {
        return CommonUtils.formatToDecimalPriceSixDigits(CommonUtils.stringToBigDecimal(getAmount()));
    }


    public String getFiatPrice() {
        if (coin != null)
            return coin.toFiat(getAmount()).toString();
        else return "0";
    }

    public String getFiatPriceFormat() {
        if (coin != null)
            return CommonUtils.formatToDecimalPriceTwoDigits(coin.toFiat(getAmount()));
        else return "0";
    }

    public void setFiatPrice(String fiatPrice) {
        if (coin != null)
            setAmount(coin.toCurrency(fiatPrice).toString());
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public CryptoCurrencyType getType() {
        return type;
    }

    public void setType(CryptoCurrencyType type) {
        this.type = type;
    }

    public boolean getShowAmount() {
        return this.showAmount;
    }

    public boolean getIsSelected() {
        return this.isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public boolean getUserVerifiedMnemonic() {
        return this.userVerifiedMnemonic;
    }

    public long getCreateWalletDate() {
        return this.createWalletDate;
    }

    public void setCreateWalletDate(long createWalletDate) {
        this.createWalletDate = createWalletDate;
    }

    public int getShowingOrder() {
        return this.showingOrder;
    }

    public void setShowingOrder(int showingOrder) {
        this.showingOrder = showingOrder;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMnemonic() {
        return this.mnemonic;
    }

    public void setMnemonic(String mnemonic) {
        this.mnemonic = mnemonic;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Wallet wallet = (Wallet) o;
        return type == wallet.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    protected Wallet(Parcel in) {
        address = in.readString();
        walletName = in.readString();
        amount = in.readString();
        userVerifiedMnemonic = in.readByte() != 0;
        fileName = in.readString();
        mnemonic = in.readString();
        privateKey = in.readString();
        createWalletDate = in.readLong();
        showingOrder = in.readInt();
        showAmount = in.readByte() != 0;
        isSelected = in.readByte() != 0;
        int tmpCurrency = in.readInt();
        this.currency = tmpCurrency == -1 ? null : FiatType.values()[tmpCurrency];
        int tmpType = in.readInt();
        this.type = tmpType == -1 ? null : CryptoCurrencyType.values()[tmpType];
        this.coin = in.readParcelable(Coin.class.getClassLoader());
    }


    @Generated(hash = 353525902)
    public Wallet(String uniqueID, String address, String walletName, String amount, CryptoCurrencyType type, boolean userVerifiedMnemonic,
            String fileName, String mnemonic, String privateKey, long createWalletDate, int showingOrder) {
        this.uniqueID = uniqueID;
        this.address = address;
        this.walletName = walletName;
        this.amount = amount;
        this.type = type;
        this.userVerifiedMnemonic = userVerifiedMnemonic;
        this.fileName = fileName;
        this.mnemonic = mnemonic;
        this.privateKey = privateKey;
        this.createWalletDate = createWalletDate;
        this.showingOrder = showingOrder;
    }


    public static final Creator<Wallet> CREATOR = new Creator<Wallet>() {
        @Override
        public Wallet createFromParcel(Parcel in) {
            return new Wallet(in);
        }

        @Override
        public Wallet[] newArray(int size) {
            return new Wallet[size];
        }
    };


    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(address);
        parcel.writeString(walletName);
        parcel.writeString(amount);
        parcel.writeByte((byte) (userVerifiedMnemonic ? 1 : 0));
        parcel.writeString(fileName);
        parcel.writeString(mnemonic);
        parcel.writeString(privateKey);
        parcel.writeLong(createWalletDate);
        parcel.writeInt(showingOrder);
        parcel.writeByte((byte) (showAmount ? 1 : 0));
        parcel.writeByte((byte) (isSelected ? 1 : 0));
        parcel.writeInt(this.currency == null ? -1 : this.currency.ordinal());
        parcel.writeInt(this.type == null ? -1 : this.type.ordinal());
        parcel.writeParcelable((Parcelable) this.coin, flags);
    }
}
