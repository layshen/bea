package com.epiboly.bea.http.model;

import com.hjq.http.model.BodyType;

/**
 * @author vemao
 * @time 2023/1/24
 * @describe 节点服务器
 */
public class MyNodeServer extends RequestServer{
    @Override
    public String getHost() {
        return "http://117.50.163.220/";
    }

    @Override
    public BodyType getType() {
        return  BodyType.FORM;
    }
}
