package com.epiboly.bea.http.api;

import com.hjq.http.config.IRequestApi;

/**
 * @author vemao
 * @time 2023/1/29
 * @describe
 */
public class NodeInfoApi implements IRequestApi {
    @Override
    public String getApi() {
        return "IntegralServer/node/getUserPurchaseNodeInfo";
    }

    private String uid;

    public NodeInfoApi setUid(String uid) {
        this.uid = uid;
        return this;
    }
}
