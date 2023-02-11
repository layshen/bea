package com.epiboly.bea.http.model;

import androidx.annotation.Keep;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject
 *    time   : 2019/12/07
 *    desc   : 统一接口数据结构
 */
@Keep
public class HttpData<T> {

    /** 返回码 */
    private int status;
    private String desc;
    /** 数据 */
    private T data;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return desc ;
    }

    public String getDesc() {
        return desc;
    }

    public T getData() {
        return data;
    }

    /**
     * 是否请求成功
     */
    public boolean isRequestSucceed() {
        return status == 1000;
    }

    /**
     * 是否 Token 失效
     */
    public boolean isTokenFailure() {
        return status == 1017;
    }
}