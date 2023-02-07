package com.epiboly.bea.http.api;


/**
 * @author vemao
 * @time 2023/1/21
 * @describe
 */
public class UpdateImageApi extends BaseUserAPi {

    private String avatar;

    public UpdateImageApi setAvatar(String avatar) {
        this.avatar = avatar;
        return this;
    }
}
