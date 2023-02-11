package com.epiboly.bea.http.api;

import androidx.annotation.Keep;

import com.hjq.http.config.IRequestApi;

public final class RegisterApi implements IRequestApi {


    @Override
    public String getApi() {
        return "UserServer/user/regist";
    }

    /** 手机号 */
    private String phone;
    /** 验证码 */
    private String validCode;
    /** 密码 */
    private String password;

    private String nickName;

    private String recommendUid;

    public RegisterApi setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public RegisterApi setCode(String code) {
        this.validCode = code;
        return this;
    }

    public RegisterApi setPassword(String password) {
        this.password = password;
        return this;
    }

    public RegisterApi setNick(String nickName) {
        this.nickName = nickName;
        return this;
    }

    public RegisterApi setRecommendUid(String recommendUid) {
        this.recommendUid = recommendUid;
        return this;
    }

    @Keep
    public final static class Bean {

    }
}