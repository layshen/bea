package com.epiboly.bea2.http.api;


/**
 * @author vemao
 * @time 2023/1/21
 * @describe
 */
public class UpdateNickApi extends BaseUserAPi {

    private String nickName;

    public UpdateNickApi setNickName(String nickName) {
        this.nickName = nickName;
        return this;
    }
}
