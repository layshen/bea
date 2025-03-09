package com.epiboly.bea2.http.api;

import com.hjq.http.config.IRequestApi;

/**
 * @author vemao
 * @time 2023/1/21
 * @describe
 */
public class BaseUserAPi implements IRequestApi {
    public String token;
    public String uid;

    public BaseUserAPi setToken(String token) {
        this.token = token;
        return this;
    }

    public BaseUserAPi setUid(String uid) {
        this.uid = uid;
        return this;
    }

    @Override
    public String getApi() {
        return "UserServer/user/editUserInfo";
    }
}