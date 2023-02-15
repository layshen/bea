package com.epiboly.bea.http.api;

import com.hjq.http.config.IRequestApi;

/**
 * @author vemao
 * @time 2023/2/8
 * @describe
 */
public class AzExchangeApi implements IRequestApi {

    private String password;

    @Override
    public String getApi() {
        return "IntegralServer/exchange/rollIntoOrOut";
    }

    private String token;
    private String uid;
    private float az;
    private int type = 1;

    public AzExchangeApi setToken(String token) {
        this.token = token;
        return this;
    }

    public AzExchangeApi setUid(String uid) {
        this.uid = uid;
        return this;
    }

    public AzExchangeApi setAz(float az) {
        this.az = az;
        return this;
    }

    public AzExchangeApi setPassword(String password) {
        this.password = password;
        return this;
    }
}
