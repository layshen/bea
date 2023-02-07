package com.epiboly.bea.http.model;

import com.hjq.http.config.IRequestServer;
import com.hjq.http.model.BodyType;


public class RequestServer implements IRequestServer {

    @Override
    public String getHost() {
        return "http://117.50.176.132/";
    }

    @Override
    public String getPath() {
        return "";
    }

    @Override
    public BodyType getType() {
        // 以表单的形式提交参数
        return BodyType.JSON;
    }
}