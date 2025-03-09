package com.epiboly.bea2.http.api;

import androidx.annotation.Keep;

import com.hjq.http.config.IRequestApi;

/**
 * @author vemao
 * @time 2023/2/4
 * @describe
 */
public class SystemNoticeListApi implements IRequestApi {

    @Override
    public String getApi() {
        return "user/queryNotices";
    }

    private String uid;
    private String token;
    private int page;

    public SystemNoticeListApi setUid(String uid) {
        this.uid = uid;
        return this;
    }

    public SystemNoticeListApi setToken(String token) {
        this.token = token;
        return this;
    }

    public SystemNoticeListApi setPage(int page) {
        this.page = page;
        return this;
    }
    @Keep
    public static class Bean {
        private long id;
        private String adminUid;
        private String notice;
        private String createTime;
        private long timestamp;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getAdminUid() {
            return adminUid;
        }

        public void setAdminUid(String adminUid) {
            this.adminUid = adminUid;
        }

        public String getNotice() {
            return notice;
        }

        public void setNotice(String notice) {
            this.notice = notice;
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
