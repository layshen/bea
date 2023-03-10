package com.epiboly.bea.http.model;

import com.epiboly.bea.rich.BuildConfig;
import com.epiboly.bea.util.Cons;
import com.hjq.http.config.IRequestServer;
import com.hjq.http.model.BodyType;


public class RequestServer implements IRequestServer {

    @Override
    public String getHost() {
        if (Cons.isDEBUG){
            return "http://117.50.172.87:8080/";
        }
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