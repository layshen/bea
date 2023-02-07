package com.epiboly.bea.http.model;

/**
 * @author vemao
 * @time 2023/1/24
 * @describe 节点服务器
 */
public class NodeServer extends RequestServer{
    @Override
    public String getHost() {
        return "http://117.50.163.220/";
    }
}
