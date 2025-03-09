package com.epiboly.bea2.http.api;

import com.hjq.http.config.IRequestApi;


public final class LoginByCodeApi implements IRequestApi {

    @Override
    public String getApi() {
        return "UserServer/user/loginByCode";
    }

    /** 手机号 */
    private String phone;
    /** 验证码 */
    private String validCode;

    public LoginByCodeApi setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public LoginByCodeApi setValidCode(String validCode) {
        this.validCode = validCode;
        return this;
    }

    public final static class Bean {


    }
}