package com.epiboly.bea.http.api;

import com.hjq.http.config.IRequestApi;

/**
 * @author vemao
 * @time 2023/1/29
 * @describe
 */
public class FinishAdvertiseWatchApi implements IRequestApi {

    @Override
    public String getApi() {
        return "IntegralServer/advertise/finishAdvertiseWatch";
    }

    private String token;
    private String sort;

    public FinishAdvertiseWatchApi setToken(String token) {
        this.token = token;
        return this;
    }

    public FinishAdvertiseWatchApi setSort(String sort) {
        this.sort = sort;
        return this;
    }
}
