package com.android.l2l.twolocal.model;

import com.google.gson.annotations.SerializedName;


public class ProfileInfo {

    @SerializedName("id")
    private Long user_Id;
    @SerializedName("name")
    private String name;
    @SerializedName("email")
    private String email;
    @SerializedName("mobile_number")
    private String mobile_number;
    @SerializedName("twofa")
    private String twofa;
    @SerializedName("first_name")
    private String first_name;
    @SerializedName("last_name")
    private String last_name;
    @SerializedName("birthday")
    private String birthday;
    @SerializedName("country")
    private String country;
    @SerializedName("city")
    private String city;
    @SerializedName("state")
    private String state;
    @SerializedName("post_code")
    private String post_code;
    @SerializedName("address")
    private String address;
    @SerializedName("created_at")
    private String created_at;
    @SerializedName("updated_at")
    private String updated_at;
    @SerializedName("twofa_status")
    private String twoFa_status;
    @SerializedName("status")
    private String status;
    @SerializedName("country_code")
    private String country_code; // if empty is string
    @SerializedName("api_token")
    private String api_token;
    @SerializedName("access_token")
    private String access_token;
    @SerializedName("token_type")
    private String token_type;
    @SerializedName("affilated_status")
    private String affiliatedStatus;
    @SerializedName("affiliate_code")
    private String affiliateCode;
    @SerializedName("wallet")
    private String wallet; // bsc/2lc

    public ProfileInfo(Long user_Id, String name,
                       String email, String mobile_number,
                       String twofa, String first_name, String last_name, String birthday, String country,
                       String city, String state, String post_code, String address,
                       String created_at, String updated_at, String twoFa_status,
                       String status, String country_code,
                       String affiliatedStatus, String affiliateCode, String wallet ) {
        this.user_Id = user_Id;
        this.name = name;
        this.email = email;
        this.mobile_number = mobile_number;
        this.twofa = twofa;
        this.first_name = first_name;
        this.last_name = last_name;
        this.birthday = birthday;
        this.country = country;
        this.city = city;
        this.state = state;
        this.post_code = post_code;
        this.address = address;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.twoFa_status = twoFa_status;
        this.status = status;
        this.country_code = country_code;
        this.affiliatedStatus = affiliatedStatus;
        this.affiliateCode = affiliateCode;
        this.wallet = wallet;
    }

    public ProfileInfo() {
    }


    public Long getUser_Id() {
        return this.user_Id;
    }

    public void setUser_Id(Long user_Id) {
        this.user_Id = user_Id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile_number() {
        return this.mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getTwofa() {
        return this.twofa;
    }

    public void setTwofa(String twofa) {
        this.twofa = twofa;
    }

    public String getFirst_name() {
        return this.first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return this.last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getBirthday() {
        return this.birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPost_code() {
        return this.post_code;
    }

    public void setPost_code(String post_code) {
        this.post_code = post_code;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCreated_at() {
        return this.created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return this.updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getTwoFa_status() {
        return this.twoFa_status;
    }

    public void setTwoFa_status(String twoFa_status) {
        this.twoFa_status = twoFa_status;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCountry_code() {
        return this.country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getApi_token() {
        return api_token;
    }

    public void setApi_token(String api_token) {
        this.api_token = api_token;
    }

    public String getAffiliatedStatus() {
        return affiliatedStatus;
    }

    public void setAffiliatedStatus(String affiliatedStatus) {
        this.affiliatedStatus = affiliatedStatus;
    }

    public String getAffiliateCode() {
        return affiliateCode;
    }

    public void setAffiliateCode(String affiliateCode) {
        this.affiliateCode = affiliateCode;
    }

    public String getWallet() {
        return wallet;
    }

    public void setWallet(String wallet) {
        this.wallet = wallet;
    }

    public boolean twoFaIsActive() {
        return (twoFa_status != null && twoFa_status.equalsIgnoreCase("true")); //&& (twofa!=null && twofa.equalsIgnoreCase("true"));
    }
}
