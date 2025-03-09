package com.epiboly.bea2.http.model;

import com.epiboly.bea2.util.Cons;
import com.hjq.http.model.BodyType;

/**
 * @author vemao
 * @time 2023/1/24
 * @describe 节点服务器
 */
public class MyNodeServer extends RequestServer{
    @Override
    public String getHost() {
        if (Cons.isDEBUG){
            return "https://layshen.fun/wechat/";
        }
        return "https://layshen.fun/wechat/";
    }

    @Override
    public BodyType getType() {
        return  BodyType.FORM;
    }
}
