package com.epiboly.bea.util;
import android.text.TextUtils;

import com.epiboly.bea.http.api.CheckVersionApi;
import com.epiboly.bea.http.model.HttpData;
import com.epiboly.bea.http.model.IntegralServer;
import com.hjq.gson.factory.GsonFactory;
import com.hjq.http.EasyHttp;
import com.hjq.http.lifecycle.ApplicationLifecycle;
import com.hjq.http.listener.HttpCallback;
import com.tencent.mmkv.MMKV;

/**
 * @author vemao
 * @time 2023/2/13
 * @describe
 */
public class CheckUpdateHelper {

    private final MMKV mMmkv;
    private static volatile CheckUpdateHelper mInstance;

    private CheckUpdateHelper(){
        mMmkv = MMKV.mmkvWithID("CheckUpdate");
    }

    public static CheckUpdateHelper getInstance(){
        if (mInstance == null){
            synchronized (CheckUpdateHelper.class){
                if (mInstance == null){
                    mInstance = new CheckUpdateHelper();
                }
            }
        }
        return mInstance;
    }


    public void check(){
        EasyHttp.post(new ApplicationLifecycle())
                .server(new IntegralServer())
                .api(new CheckVersionApi())
                .request(new HttpCallback<HttpData<CheckVersionApi.Bean>>(null) {

                    @Override
                    public void onSucceed(HttpData<CheckVersionApi.Bean> data) {
                       if (data == null || data.getData() == null){
                           return;
                       }
                       if (data.isRequestSucceed()){
                           pubVersionInfo(data.getData());
                       }
                    }
                });
    }

    private void pubVersionInfo(CheckVersionApi.Bean data) {
        mMmkv.putString("version_info_from_server", GsonFactory.getSingletonGson().toJson(data));
    }

    public CheckVersionApi.Bean getVersionInfo() {
        String version_info_from_server = mMmkv.getString("version_info_from_server", "");
        try {
            CheckVersionApi.Bean bean = GsonFactory.getSingletonGson().fromJson(version_info_from_server, CheckVersionApi.Bean.class);
            if (bean != null){
                return bean;
            }
        }catch (Exception ignored){}
        return new CheckVersionApi.Bean();
    }

    public boolean isNeedUpdate(int versionCode, String versionName) {
        String latest_version_from_server = getVersionInfo().getVersionName();
        if (TextUtils.isEmpty(latest_version_from_server) || TextUtils.isEmpty(versionName)){
            return false;
        }
        if (TextUtils.equals(latest_version_from_server,versionName)){
            return true;
        }
        String[] split_server = latest_version_from_server.split("\\.");
        String[] split_local_version = versionName.split("\\.");
        if (split_server.length <= 0 || split_local_version.length <= 0){
            return false;
        }
        int serverBigVersion = MathUtil.parseInt(split_server[0]);
        int localBigVersion = MathUtil.parseInt(split_local_version[0]);
        if (serverBigVersion > localBigVersion){
            return true;
        }
        int serverSmallVersion = MathUtil.parseInt(split_server[1]);
        int localSmallVersion = MathUtil.parseInt(split_local_version[1]);
        return serverSmallVersion > localSmallVersion;
    }
}
