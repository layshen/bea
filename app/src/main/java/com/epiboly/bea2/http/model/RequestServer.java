package com.epiboly.bea2.http.model;

import com.epiboly.bea2.util.Cons;
import com.hjq.http.config.IRequestServer;
import com.hjq.http.model.BodyType;


public class RequestServer implements IRequestServer {

    @Override
    public String getHost() {
        if (Cons.isDEBUG){
            return "https://layshen.fun/wechat/";
        }
        return "https://layshen.fun/wechat/";
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