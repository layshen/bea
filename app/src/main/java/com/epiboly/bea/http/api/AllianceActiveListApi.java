package com.epiboly.bea.http.api;

import androidx.annotation.Keep;

import com.hjq.http.config.IRequestApi;

/**
 * @author vemao
 * @time 2023/2/4
 * @describe
 */
public class AllianceActiveListApi implements IRequestApi {

    @Override
    public String getApi() {
        return "UserServer/user/queryUserFans";
    }

    private String token;
    private String uid;
    private int page;

    public String getToken() {
        return token;
    }

    public AllianceActiveListApi setToken(String token) {
        this.token = token;
        return this;
    }

    public String getUid() {
        return uid;
    }

    public AllianceActiveListApi setUid(String uid) {
        this.uid = uid;
        return this;
    }

    public int getPage() {
        return page;
    }

    public AllianceActiveListApi setPage(int page) {
        this.page = page;
        return this;
    }

    @Keep
    public static class Bean{
        private String uid;
        private String nickName;
        private String avatar;
        private String phone;
        private int authentication;//用户认证状态 0-未认证 1-已认证
        private String hashVal;
        private String leagueHashVal;
        private int level;//用户等级： 1-实习着 2-引路者 3-开拓者 4-缔造者
        private String createTime;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public int getAuthentication() {
            return authentication;
        }

        public void setAuthentication(int authentication) {
            this.authentication = authentication;
        }

        public String getHashVal() {
            return hashVal;
        }

        public void setHashVal(String hashVal) {
            this.hashVal = hashVal;
        }

        public String getLeagueHashVal() {
            return leagueHashVal;
        }

        public void setLeagueHashVal(String leagueHashVal) {
            this.leagueHashVal = leagueHashVal;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getLevel() {
            return level;
        }

        public int getLevelDesc() {
            switch (level){
                case 1:

            }
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }
    }
}
