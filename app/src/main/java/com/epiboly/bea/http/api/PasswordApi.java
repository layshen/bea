package com.epiboly.bea.http.api;

import com.hjq.http.config.IRequestApi;


public final class PasswordApi implements IRequestApi {

    @Override
    public String getApi() {
        return "UserServer/user/editUserInfo";
    }

    /** 密码 */
    private String password;
    private String phone;

    public PasswordApi setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public PasswordApi setPassword(String password) {
        this.password = password;
        return this;
    }
}