package com.epiboly.bea.http.api;

import androidx.annotation.Keep;

import com.hjq.http.config.IRequestApi;

/**
 * @author vemao
 * @time 2023/2/8
 * @describe
 */
public class NodeListPurchaseNodeInfo implements IRequestApi {
    @Override
    public String getApi() {
        return "IntegralServer/node/getUserPurchaseNodeInfo";
    }

    private String token;
    private String uid;

    public NodeListPurchaseNodeInfo setToken(String token) {
        this.token = token;
        return this;
    }

    public NodeListPurchaseNodeInfo setUid(String uid) {
        this.uid = uid;
        return this;
    }

    @Keep
    public static class Bean{
        public int nodeType;//可上节点类型
        public int nodeNumber;//可上节点数量
    }
}
