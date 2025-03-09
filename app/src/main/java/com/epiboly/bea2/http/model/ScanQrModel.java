package com.epiboly.bea2.http.model;

import androidx.annotation.Keep;

/**
 * @author vemao
 * @time 2023/3/10
 * @describe
 */
@Keep
public class ScanQrModel {

    private String uid;
    private String userName;
    private String avatar;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }


}
