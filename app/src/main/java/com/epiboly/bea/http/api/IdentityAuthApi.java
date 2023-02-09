package com.epiboly.bea.http.api;

import com.hjq.http.config.IRequestApi;

/**
 * @author vemao
 * @time 2023/1/21
 * @describe
 */
public class IdentityAuthApi implements IRequestApi {
    private String token;
    private String uid;
    private String name;
    private String phone;
    private String idCard;

    public IdentityAuthApi setName(String name) {
        this.name = name;
        return this;
    }

    public IdentityAuthApi setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public IdentityAuthApi setToken(String token) {
        this.token = token;
        return this;
    }

    public IdentityAuthApi setUid(String uid) {
        this.uid = uid;
        return this;
    }

    public IdentityAuthApi setIdCard(String idCard) {
        this.idCard = idCard;
        return this;
    }

    @Override
    public String getApi() {
        return "UserServer/user/authentication";
    }
}
