package com.epiboly.bea2.http.api;


public final class PhoneApi extends BaseUserAPi {

    /** 新手机号 */
    private String phone;


    public PhoneApi setPhone(String phone) {
        this.phone = phone;
        return this;
    }
}