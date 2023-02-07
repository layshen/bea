package com.epiboly.bea.http.api;



public final class UserInfoApi extends BaseUserAPi {

    @Override
    public String getApi() {
        return "UserServer/user/queryUserInfo";
    }
}