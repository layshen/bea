package com.epiboly.bea2.http.api;

import androidx.annotation.Keep;

import com.epiboly.bea2.http.model.MyNodeBean;
import com.hjq.http.config.IRequestApi;

import java.util.ArrayList;

/**
 * @author vemao
 * @time 2023/2/4
 * @describe
 */
public class UserBuyNodeInfoApi implements IRequestApi {
    @Override
    public String getApi() {
        return "IntegralServer/userInfo/getUserBuyNodeInfoByUid";
    }

    private String uid;
    private String token;

    public UserBuyNodeInfoApi setUid(String uid) {
        this.uid = uid;
        return this;
    }

    public UserBuyNodeInfoApi setToken(String token) {
        this.token = token;
        return this;
    }

    @Keep
    public class Wrapper{
        private ArrayList<MyNodeBean> userNodeRefDTOList;

        public ArrayList<MyNodeBean> getUserNodeRefDTOList() {
            return userNodeRefDTOList;
        }

        public void setUserNodeRefDTOList(ArrayList<MyNodeBean> userNodeRefDTOList) {
            this.userNodeRefDTOList = userNodeRefDTOList;
        }
    }
}
