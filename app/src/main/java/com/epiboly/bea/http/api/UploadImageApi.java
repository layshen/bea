package com.epiboly.bea.http.api;

import com.hjq.http.config.IRequestApi;

import java.io.File;


public final class UploadImageApi implements IRequestApi {

    @Override
    public String getApi() {
        return "UserServer/upload/img";
    }

    /** 图片文件 */
    private File avatar;

    private String token;

    public UploadImageApi setImage(File image) {
        this.avatar = image;
        return this;
    }

    public UploadImageApi setToken(String token) {
        this.token = token;
        return this;
    }
}