package com.epiboly.bea2.http.api;

import com.hjq.http.config.IRequestApi;

/**
 * @author vemao
 * @time 2023/2/7
 * @describe
 */
public class EditTradePasswordApi implements IRequestApi {
    @Override
    public String getApi() {
        return "UserServer/user/editExPassword";
    }

    private String phone;
    private String newPassword;

    public EditTradePasswordApi setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public EditTradePasswordApi setNewPassword(String newPassword) {
        this.newPassword = newPassword;
        return this;
    }
}
