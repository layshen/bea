package com.epiboly.bea.http.api;

import com.hjq.http.config.IRequestApi;

/**
 * @author mao
 * @time 2023/1/21
 * @describe
 */
public class InviteFriendApi implements IRequestApi {
    @Override
    public String getApi() {
        return "UserServer/user/queryRecommendImage";
    }

    private String token;
    private String uid;

    public InviteFriendApi setToken(String token) {
        this.token = token;
        return this;
    }

    public InviteFriendApi setUid(String uid) {
        this.uid = uid;
        return this;
    }
}
