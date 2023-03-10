package com.epiboly.bea.http.model;

import com.epiboly.bea.rich.BuildConfig;
import com.epiboly.bea.util.Cons;
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
            return "http://117.50.192.232:8080/";
        }
        return "http://117.50.163.220/";
    }

    @Override
    public BodyType getType() {
        return  BodyType.FORM;
    }
}
