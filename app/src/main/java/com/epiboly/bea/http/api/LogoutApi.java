package com.epiboly.bea.http.api;

import com.hjq.http.config.IRequestApi;


public final class LogoutApi implements IRequestApi {

    @Override
    public String getApi() {
        return "UserServer/user/logout";
    }

    private String token;
    private String uid;

    public LogoutApi setToken(String token) {
        this.token = token;
        return this;
    }

    public LogoutApi setUid(String uid) {
        this.uid = uid;
        return this;
    }
}