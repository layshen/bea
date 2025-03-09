package com.epiboly.bea2.http.model;

import com.epiboly.bea2.util.Cons;

/**
 * @author vemao
 * @time 2023/1/24
 * @describe 节点服务器
 */
public class IntegralServer extends RequestServer{
    @Override
    public String getHost() {
        if (Cons.isDEBUG){
            return "https://layshen.fun/wechat/";
        }
        return "https://layshen.fun/wechat/";
    }
}
