package com.android.l2l.twolocal.dataSourse.remote.api;

import com.android.l2l.twolocal.BuildConfig;

public class ApiConstants {

    public static final int REQUEST_TIMEOUT_DURATION = 30;
    public static final int ETHER_MAX_TRANSACTION_RECORDS = 100;

    public static final String BASE_URL = BuildConfig.BASE_URL;
    

    public static final String GET_MARKET_PLACE_LIST_ENDPOINT = "https://2local.io/api/get-companies";
    public static final String ADD_MARKET_PLACE_ENDPOINT = "api/create-company";
    public static final String LOGIN_INVESTOR_ENDPOINT = "api/auth/login";
    public static final String SIGN_UP_INVESTOR_ENDPOINT = "api/auth/register";
    public static final String UPDATE_PROFILE_ENDPOINT = "api/profile/update-profile";
    public static final String GET_PROFILE_DATA_ENDPOINT = "api/profile/get-profile";
    public static final String VALIDATE_TWO_FA = "api/auth/validate-twofa/";
    public static final String GET_EXCHANGE_RATE_ENDPOINT_BITRUE = "https://www.bitrue.com/api/v1/ticker/price";
    public static final String GET_EXCHANGE_RATE_ENDPOINT_LATOKEN = "https://api.latoken.com/v2/ticker/";
    public static final String GOOGLE_QR_CODE = "https://www.google.com/maps/search/?api=1&query=";

    //API keys
    public static final String LATOKEN_USD_PAIR_ID =  "0c3a106d-bde3-4c13-a26e-3fd2394529e5";

    public static final String BSC_SCAN_PAI_KEY =  BuildConfig.BSC_SCAN_PAI_KEY;
    public static final String ETHER_SCAN_PAI_KEY =  BuildConfig.ETHER_SCAN_PAI_KEY;
    public static final String ETHER_MAINNET_INFURA_API_KEY =  BuildConfig.ETHER_MAINNET_INFURA_API_KEY;

    //ether
    public static final String ETHER_SCAN_URL = "https://api-cn.etherscan.com/api";
    public static final String ETHER_TRANSACTION_DETAIL_URL = "https://etherscan.io/tx/";
    public static final String ETHER_WEB3_MAINNET_INFURA_URL = "https://mainnet.infura.io/v3/" + ETHER_MAINNET_INFURA_API_KEY;

    public static final String ETHER_TRANSACTION_LIST = ETHER_SCAN_URL + "?module=account&action=txlist&apikey="+ETHER_SCAN_PAI_KEY;
    public static final String ETHER_TRANSACTION_GAS = ETHER_SCAN_URL + "?module=gastracker&action=gasoracle&apikey="+ETHER_SCAN_PAI_KEY;


    //binance smart chain
    public static final String BSC_WEB3_MAINNET_URL = "https://bsc-dataseed.binance.org";
    public static final String BSC_SCAN_URL = "https://api.bscscan.com/api";
    public static final String BSC_TRANSACTION_DETAIL_URL = "https://bscscan.com/tx/";

    public static final String BSC_TRANSACTION_LIST = BSC_SCAN_URL + "?module=account&action=tokentx&apikey="+BSC_SCAN_PAI_KEY;
    public static final String BSC_TRANSACTION_GAS = BSC_SCAN_URL + "?module=proxy&action=eth_gasPrice&apikey=" + BSC_SCAN_PAI_KEY;
    public static final String BSC_BEP_20_TOKEN_BALANCE = BSC_SCAN_URL + "?module=account&action=tokenbalance&tag=latest&apikey="+BSC_SCAN_PAI_KEY;

    //binance
    public static final String BINANCE_TRANSACTION_LIST = BSC_SCAN_URL + "?module=account&action=txlist&apikey="+BSC_SCAN_PAI_KEY;

   }
