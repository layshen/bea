package com.epiboly.bea.http.api;

import androidx.annotation.Keep;

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

    @Keep
    public static class Bean {
        private long id;
        private String az;//算力数量
        private int source;
        private String createTime;
        private long timestamp;

        public String getAz() {
            return az;
        }

        public void setAz(String az) {
            this.az = az;
        }

        public int getSource() {
            return source;
        }

        public String getSourceDesc() {
            switch (source){
                case 0:
                    return "转入";
                case 1:
                    return "转出";
                case 2:
                    return "分红收益";
                case 3:
                    return "承兑商买入";
                case 4:
                    return "承兑商卖出";
                case 5:
                    return "节点收益";
                case 6:
                case 7:
                    return "加成收益";
                case 8:
                    return "兑换节点";
                case 11:
                    return "扫码收入";
                case 12:
                    return "扫码支出";
                case 13:
                    return "释放";
            }
            return "";
        }

        public String getSignDesc() {
            switch (source){
                case 0:
                    return "+";
                case 1:
                    return "-";
                case 2:
                    return "+";
                case 3:
                    return "";
                case 4:
                    return "";
                case 5:
                    return "+";
                case 6:
                case 7:
                    return "+";
                case 8:
                    return "-";
                case 11:
                    return "+";
                case 12:
                    return "-";
                case 13:
                    return "+";
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
