package com.epiboly.bea.http.api;

import com.hjq.http.config.IRequestApi;

/**
 * @author vemao
 * @time 2023/1/21
 * @describe
 */
public class QrScanRequestAPI implements IRequestApi {
    public String url;

    public QrScanRequestAPI setUrl(String url) {
        this.url = url;
        return this;
    }

    @Override
    public String getApi() {
        return url;
    }
}