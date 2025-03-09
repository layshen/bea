package com.epiboly.bea2.http.api;

import com.hjq.http.config.IRequestApi;


public final class GetCodeApi implements IRequestApi {
    public static int TYPE_LOGON = 0;
    public static int TYPE_REGISTER = 1;
    public static int TYPE_FORGET_PSW = 2;
    public static int TYPE_UPDATE_USER = 3;

    public static boolean isSendSuccess(int status) {
        return 1027 == status;
    }

    @Override
    public String getApi() {
        return "UserServer/user/sendCode";
    }

    /** 手机号 */
    private String phone;
    /**
     * 验证码类型 0-登录 1-注册 2-忘记密码 3-修改用户信息
     */
    private int type;

    public GetCodeApi setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public GetCodeApi setType(int type) {
        this.type = type;
        return this;
    }
}