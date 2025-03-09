package com.epiboly.bea2.http.api;

import com.hjq.http.config.IRequestApi;

public class ExchangeProductApi implements IRequestApi {
    @Override
    public String getApi() {
        return "product/exchangeProduct";
    }

    private String uid;

    private Integer productId;

    public String getUid() {
        return uid;
    }

    public ExchangeProductApi setUid(String uid) {
        this.uid = uid;
        return this;
    }

    public Integer getProductId() {
        return productId;
    }

    public ExchangeProductApi setProductId(Integer productId) {
        this.productId = productId;
        return this;
    }
}
