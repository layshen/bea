package com.epiboly.bea2.http.api;

import com.hjq.http.config.IRequestApi;

/**
 * @author vemao
 * @time 2023/2/8
 * @describe
 */
public class AzQrExchangeApi implements IRequestApi {

    private String exPassword;

    @Override
    public String getApi() {
        return "IntegralServer/exchange/qrRechargePay";
    }

    private String token;
    private String uid;
    private String inUid;
    private float az;

    public AzQrExchangeApi setToken(String token) {
        this.token = token;
        return this;
    }

    public AzQrExchangeApi setUid(String uid) {
        this.uid = uid;
        return this;
    }

    public AzQrExchangeApi setInUid(String inUid) {
        this.inUid = inUid;
        return this;
    }

    public AzQrExchangeApi setAz(float az) {
        this.az = az;
        return this;
    }

    public AzQrExchangeApi setPassword(String password) {
        this.exPassword = password;
        return this;
    }
}
