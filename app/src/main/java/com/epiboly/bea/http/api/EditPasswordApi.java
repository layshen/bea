package com.epiboly.bea.http.api;

import com.hjq.http.config.IRequestApi;

/**
 * @author vemao
 * @time 2023/2/7
 * @describe
 */
public class EditPasswordApi implements IRequestApi {
    @Override
    public String getApi() {
        return "UserServer/user/editPassword";
    }

    private String phone;
    private String newPassword;

    public EditPasswordApi setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public EditPasswordApi setNewPassword(String newPassword) {
        this.newPassword = newPassword;
        return this;
    }
}
