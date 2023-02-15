package com.epiboly.bea.http.api;

import android.text.TextUtils;

import com.hjq.http.config.IRequestApi;

/**
 * @author mao
 * @time 2023/1/21
 * @describe
 */
public class CheckVersionApi implements IRequestApi {
    @Override
    public String getApi() {
        return "UserServer/user/queryRecommendImage";
    }

    public static class Bean{
        //版本信息 eg "10.1"
        private String versionName;
        //下载url
        private String url;
        //更新描述 eg: "修复Bug\n优化用户体验"
        public String updateLog;
        //是否强制更新
        private Boolean isFocusUpdate = true;

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }

        public String getUrl() {
            if (TextUtils.isEmpty(url)){
                return "http://static.bea.net.cn/application/BEA_release.apk";
            }
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public boolean isFocusUpdate() {
            if (isFocusUpdate == null){
                return true;
            }
            return isFocusUpdate;
        }

        public String getUpdateLog() {
            if (TextUtils.isEmpty(updateLog)){
                return "新版本";
            }
            return updateLog;
        }

        public void setFocusUpdate(Boolean focusUpdate) {
            isFocusUpdate = focusUpdate;
        }
    }
}
