package com.epiboly.bea2.http.api;

import com.hjq.http.config.IRequestApi;


public final class ValidCodeApi implements IRequestApi {


    @Override
    public String getApi() {
        return "UserServer/user/validCode";
    }

    /** 手机号 */
    private String phone;
    /**
     * 验证码类型 0-登录 1-注册 2-忘记密码 3-修改用户信息
     */
    private int type;
    private String validCode;

    public ValidCodeApi setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public ValidCodeApi setType(int type) {
        this.type = type;
        return this;
    }

    public ValidCodeApi setValidCode(String validCode) {
        this.validCode = validCode;
        return this;
    }
}