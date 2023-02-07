package com.epiboly.bea.http.api;

import com.hjq.http.config.IRequestApi;

/**
 * @author vemao
 * @time 2023/1/23
 * @describe
 */
public class NodeExchangeApi implements IRequestApi {
    @Override
    public String getApi() {
        return "IntegralServer/node/addUserNodeInfo";
    }

    private String uid;
    private String nid;

    public NodeExchangeApi setNid(String nid) {
        this.nid = nid;
        return this;
    }

    public NodeExchangeApi setUid(String uid) {
        this.uid = uid;
        return this;
    }
}
