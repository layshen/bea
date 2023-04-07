package com.epiboly.bea.advertisement;

import android.app.Application;
import android.os.Build;
import android.webkit.WebView;

import com.epiboly.bea.cache.UserHelper;
import com.jiagu.sdk.OSETSDKProtected;
import com.kc.openset.OSETSDK;
import com.kc.openset.listener.OSETInitListener;
import com.sigmob.sdk.base.models.config.Common;

/**
 * @author vemao
 * @time 2023/4/6
 * @describe 广告sdk初始化
 */
public class AdSdkInit {
    public static void init(String processName, Application application){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // 安卓9.0后不允许多进程使用同一个数据目录
            try {
                WebView.setDataDirectorySuffix(processName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        OSETSDKProtected.install(application);
        OSETSDK.getInstance().setUserId(UserHelper.getInstance().getUser().getUid());
        OSETSDK.getInstance().init(application, AdCons.AppKey, new OSETInitListener() {
            @Override
            public void onError(String s) {
                //初始化失败：会调用不到广告，清选择合适的时机重新进行初始化
            }

            @Override
            public void onSuccess() {
                //初始化成功：可以开始调用广告
            }
        });
    }
}
