package com.epiboly.bea.http.api;

import com.hjq.http.config.IRequestApi;

/**
 * @author mao
 * @time 2023/1/23
 * @describe
 */
public class NodeListApi implements IRequestApi {
    @Override
    public String getApi() {
        return "IntegralServer/node/getAllNode";
    }

    private String token;

    public NodeListApi setToken(String token) {
        this.token = token;
        return this;
    }
}
