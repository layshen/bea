package com.epiboly.bea.http.api;

import com.hjq.http.config.IRequestApi;

/**
 * @author vemao
 * @time 2023/2/4
 * @describe
 */
public class AzRecordListApi implements IRequestApi {

    @Override
    public String getApi() {
        return "IntegralServer/exchange/azExchangeRecord";
    }

    private String uid;
    private String token;
    private long userExchangeId;

    public AzRecordListApi setUid(String uid) {
        this.uid = uid;
        return this;
    }

    public AzRecordListApi setToken(String token) {
        this.token = token;
        return this;
    }

    public AzRecordListApi setUserExchangeId(long userExchangeId) {
        this.userExchangeId = userExchangeId;
        return this;
    }

    public static class Bean {
        private long id;
        private float az;//算力数量
        private int source;
        private String createTime;
        private long timestamp;

        public float getAz() {
            return az;
        }

        public void setAz(float az) {
            this.az = az;
        }

        public int getSource() {
            return source;
        }

        public String getSourceDesc() {
            switch (source){
                case 0:
                    return "交易所到app";
                case 1:
                    return "app到交易所";
                case 2:
                    return "分红获得";
                case 3:
                    return "承兑商买入";
                case 4:
                    return "承兑商卖出";
                case 5:
                    return "每日任务完成获得";
                case 6:
                    return "下级上节点获得";
            }
            return "";
        }

        public void setSource(int source) {
            this.source = source;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }
    }
}
