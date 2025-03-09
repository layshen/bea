package com.epiboly.bea2.http.api;

import com.hjq.http.config.IRequestApi;


public final class ForgetPasswordApi implements IRequestApi {

    @Override
    public String getApi() {
        return "UserServer/user/editUserInfo";
    }

    /** 密码 */
    private String password;

    private String phone;

    private String validCode;

    public ForgetPasswordApi setPassword(String password) {
        this.password = password;
        return this;
    }

    public ForgetPasswordApi setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public ForgetPasswordApi setValidCode(String validCode) {
        this.validCode = validCode;
        return this;
    }
}