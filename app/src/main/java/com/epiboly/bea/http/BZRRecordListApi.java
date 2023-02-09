package com.epiboly.bea.http;

import com.hjq.http.config.IRequestApi;

/**
 * @author vemao
 * @time 2023/2/4
 * @describe
 */
public class BZRRecordListApi implements IRequestApi {

    @Override
    public String getApi() {
        return "IntegralServer/exchange/bzrExchangeRecord";
    }

    private String uid;
    private String token;
    private long userBzrRecordId;

    public BZRRecordListApi setUid(String uid) {
        this.uid = uid;
        return this;
    }

    public BZRRecordListApi setToken(String token) {
        this.token = token;
        return this;
    }

    public BZRRecordListApi setUserBzrRecordId(long userBzrRecordId) {
        this.userBzrRecordId = userBzrRecordId;
        return this;
    }

    public static class Bean {
        private long id;
        private String uid;//用户id
        private String bzr;
        private String createTime;
        private long timestamp;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getBzr() {
            return bzr;
        }

        public void setBzr(String bzr) {
            this.bzr = bzr;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }
    }
}
