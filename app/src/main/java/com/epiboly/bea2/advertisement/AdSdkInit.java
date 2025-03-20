package com.epiboly.bea2.advertisement;

import android.app.Application;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.webkit.WebView;

import com.blankj.utilcode.util.Utils;
import com.epiboly.bea2.cache.UserHelper;
import com.jiagu.sdk.OSETSDKProtected;
import com.kc.openset.config.OSETSDK;
import com.kc.openset.config.controller.OSETCustomController;
import com.kc.openset.listener.OSETInitListener;
//import com.kc.openset.OSETSDK;
//import com.kc.openset.config.controller.OSETCustomController;
//import com.kc.openset.listener.OSETInitListener;

/**
 * @author vemao
 * @time 2023/4/6
 * @describe 广告sdk初始化
 */
public class AdSdkInit {

    static Handler HANDLER = new Handler(Looper.getMainLooper());
    public static boolean isInitSuccess = false;
    private static final String TAG = "AdSdkInit";

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
        isInitSuccess = false;
        exeMaybeInit(null);
    }

    public static void exeMaybeInit(Callback callback) {
        if (isInitSuccess){
            if (callback != null){
                Log.d(TAG,"初始化过了 直接执行"+Thread.currentThread().getName());
                callback.onSuccess();
            }
            return;
        }
        OSETSDK.getInstance().setUserId(UserHelper.getInstance().getUser().getUid());
        OSETSDK.getInstance()
                .setCustomController(new OSETCustomController(){})
                .init(Utils.getApp(), AdCons.AppKey, new OSETInitListener(){
                    @Override
                    public void onError(String s) {
                        HANDLER.post(new Runnable() {
                            @Override
                            public void run() {
                                Log.d(TAG,"初始化广告 onError "+Thread.currentThread().getName());
                                isInitSuccess = false;
                                if (callback != null){
                                    callback.onError();
                                }
                            }
                        });
                    }

                    @Override
                    public void onSuccess() {
//初始化成功：可以开始调用广告
                        HANDLER.post(new Runnable() {
                            @Override
                            public void run() {
                                Log.d(TAG,"初始化广告 onSuccess " +Thread.currentThread().getName());
                                isInitSuccess = true;
                                if (callback != null){
                                    callback.onSuccess();
                                }
                            }
                        });
                    }
                });
//        OSETSDK.getInstance().init(Utils.getApp(), AdCons.AppKey, new OSETInitListener() {
//            @Override
//            public void onError(String s) {
//                HANDLER.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        Log.d(TAG,"初始化广告 onError "+Thread.currentThread().getName());
//                        isInitSuccess = false;
//                        if (callback != null){
//                            callback.onError();
//                        }
//                    }
//                });
//            }
//
//            @Override
//            public void onSuccess() {
//                //初始化成功：可以开始调用广告
//                HANDLER.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        Log.d(TAG,"初始化广告 onSuccess " +Thread.currentThread().getName());
//                        isInitSuccess = true;
//                        if (callback != null){
//                            callback.onSuccess();
//                        }
//                    }
//                });
//            }
//
//        });
    }

    public interface Callback{
        void onSuccess();
        void onError();
    }
}
