package com.epiboly.bea.http.api;

import androidx.annotation.Keep;

import com.hjq.http.config.IRequestApi;

/**
 * @author vemao
 * @time 2023/2/4
 * @describe
 */
public class AllianceActiveTopApi implements IRequestApi {

    @Override
    public String getApi() {
        return "IntegralServer/userInfo/queryFansTopBean";
    }

    private String uid;
    private String token;

    public AllianceActiveTopApi setUid(String uid) {
        this.uid = uid;
        return this;
    }

    public AllianceActiveTopApi setToken(String token) {
        this.token = token;
        return this;
    }

    @Keep
    public static class Bean {
        private String totalHashVal;//总算力
        private String totalValid;//总有效人数
        private String totalFans;//总粉丝数

        public String getTotalHashVal() {
            return totalHashVal;
        }

        public void setTotalHashVal(String totalHashVal) {
            this.totalHashVal = totalHashVal;
        }

        public String getTotalValid() {
            return totalValid;
        }

        public void setTotalValid(String totalValid) {
            this.totalValid = totalValid;
        }

        public String getTotalFans() {
            return totalFans;
        }

        public void setTotalFans(String totalFans) {
            this.totalFans = totalFans;
        }
    }
}
