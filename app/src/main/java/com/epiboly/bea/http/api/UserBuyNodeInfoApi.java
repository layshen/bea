package com.epiboly.bea.http.api;

import com.hjq.http.config.IRequestApi;

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

    public class Bean {
        private int invalidTNum;//过期体检节点数量
        private int invalidANum;//过期A级节点数量
        private int invalidBNum;
        private int invalidCNum;
        private int invalidDNum;
        private int invalidENum;
        private int invalidFNum;


        private int validTNum;
        private int validANum;
        private int validBNum;
        private int validCNum;
        private int validDNum;
        private int validENum;
        private int validFNum;


        public int getInvalidTNum() {
            return invalidTNum;
        }

        public int getInvalidANum() {
            return invalidANum;
        }

        public int getInvalidBNum() {
            return invalidBNum;
        }

        public int getInvalidCNum() {
            return invalidCNum;
        }

        public int getInvalidDNum() {
            return invalidDNum;
        }

        public int getInvalidENum() {
            return invalidENum;
        }

        public int getInvalidFNum() {
            return invalidFNum;
        }

        public int getValidTNum() {
            return validTNum;
        }

        public int getValidANum() {
            return validANum;
        }

        public int getValidBNum() {
            return validBNum;
        }

        public int getValidCNum() {
            return validCNum;
        }

        public int getValidDNum() {
            return validDNum;
        }

        public int getValidENum() {
            return validENum;
        }

        public int getValidFNum() {
            return validFNum;
        }
    }
}
