package com.epiboly.bea2.app;

import static com.epiboly.bea2.PrivacyInfo.PRIVACY_INFO;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import com.blankj.utilcode.util.ProcessUtils;
import com.blankj.utilcode.util.Utils;
import com.epiboly.bea2.advertisement.AdSdkInit;
import com.epiboly.bea2.cache.DeviceIdentifierHelper;
import com.epiboly.bea2.cache.UserHelper;
import com.epiboly.bea2.http.model.RequestHandler;
import com.epiboly.bea2.http.model.RequestServer;
import com.epiboly.bea2.manager.ActivityManager;
import com.epiboly.bea2.util.CheckUpdateHelper;
import com.epiboly.bea2.util.NetLogStrategy;
import com.hjq.bar.TitleBar;
import com.epiboly.bea2.R;
import com.epiboly.bea2.aop.Log;
import com.epiboly.bea2.http.glide.GlideApp;
import com.epiboly.bea2.other.AppConfig;
import com.epiboly.bea2.other.CrashHandler;
import com.epiboly.bea2.other.DebugLoggerTree;
import com.epiboly.bea2.other.MaterialHeader;
import com.epiboly.bea2.other.SmartBallPulseFooter;
import com.epiboly.bea2.other.TitleBarStyle;
import com.epiboly.bea2.other.ToastLogInterceptor;
import com.epiboly.bea2.other.ToastStyle;
import com.hjq.gson.factory.GsonFactory;
import com.hjq.http.EasyConfig;
import com.hjq.toast.ToastUtils;
import com.hjq.umeng.UmengClient;
import com.meituan.android.walle.WalleChannelReader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.mmkv.MMKV;

import java.net.Proxy;

import okhttp3.OkHttpClient;
import timber.log.Timber;

public class AppApplication extends Application {

    @Log("启动耗时")
    @Override
    public void onCreate() {
        super.onCreate();

        //初始化sdk
        initSdk(this);
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        // 清理所有图片内存缓存
        GlideApp.get(this).onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        // 根据手机内存剩余情况清理图片内存缓存
        GlideApp.get(this).onTrimMemory(level);
    }

    /**
     * 初始化一些第三方框架
     */
    public void initSdk(Application application) {
        android.util.Log.d("AppApplication 启动sdk", "onCreate: 启动sdk了");

        Utils.init(this);
        // 设置标题栏初始化器
        TitleBar.setDefaultStyle(new TitleBarStyle());

        // 设置全局的 Header 构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator((cx, layout) ->
                new MaterialHeader(application).setColorSchemeColors(ContextCompat.getColor(application, R.color.common_accent_color)));
        // 设置全局的 Footer 构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator((cx, layout) -> new SmartBallPulseFooter(application));
        // 设置全局初始化器
        SmartRefreshLayout.setDefaultRefreshInitializer((cx, layout) -> {
            // 刷新头部是否跟随内容偏移
            layout.setEnableHeaderTranslationContent(true)
                    // 刷新尾部是否跟随内容偏移
                    .setEnableFooterTranslationContent(true)
                    // 加载更多是否跟随内容偏移
                    .setEnableFooterFollowWhenNoMoreData(true)
                    // 内容不满一页时是否可以上拉加载更多
                    .setEnableLoadMoreWhenContentNotFull(false)
                    // 仿苹果越界效果开关
                    .setEnableOverScrollDrag(false);
        });

        // 初始化吐司
        ToastUtils.init(application, new ToastStyle());
        // 设置调试模式
        ToastUtils.setDebugMode(AppConfig.isDebug());
        // 设置 Toast 拦截器
        ToastUtils.setInterceptor(new ToastLogInterceptor());

        // 本地异常捕捉
        CrashHandler.register(application);

        // Activity 栈管理初始化
        ActivityManager.getInstance().init(application);

        // MMKV 初始化
        MMKV.initialize(application);
        // Bugly 异常捕捉
        String uid = UserHelper.getInstance().getUser().getUid();
        if (!TextUtils.isEmpty(uid)){
            CrashReport.setUserId(uid);
        }
        String channel = WalleChannelReader.getChannel(application);
        if (!TextUtils.isEmpty(channel)){
            CrashReport.setAppChannel(application, WalleChannelReader.getChannel(application));
        }
        CrashReport.initCrashReport(application, AppConfig.getBuglyId(), AppConfig.isDebug());


        // 网络请求框架初始化
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .proxy(Proxy.NO_PROXY)
                .build();

        EasyConfig.with(okHttpClient)
                // 是否打印日志
                .setLogEnabled(AppConfig.isLogEnable())
                .setLogStrategy(new NetLogStrategy())
                // 设置服务器配置
                .setServer(new RequestServer())
                // 设置请求处理策略
                .setHandler(new RequestHandler(application))
                // 设置请求重试次数
                .setRetryCount(1)
                .setInterceptor((api, params, headers) -> {
                    // 添加全局请求头
//                    headers.put("deviceOaid", UmengClient.getDeviceOaid());
//                    headers.put("versionName", AppConfig.getVersionName());
//                    headers.put("versionCode", String.valueOf(AppConfig.getVersionCode()));
                    headers.put("un_encript","layShen@123");
                    headers.put("channel","67d0d695-2da4-45d8-9182-f000dba692a4");
                    headers.put("token", UserHelper.getInstance().getToken());
                    headers.put("device_unique_id",DeviceIdentifierHelper.getInstance().getDeviceUniqueId(application));
                    // 添加全局请求参数
                    // params.put("6666666", "6666666");

                    try {
//                        android.util.Log.d("maoweiyi","18856915241 = "+new String(RSAUtil.encrypt("18856915241")));
//                        android.util.Log.d("maoweiyi","123456 = "+new String(RSAUtil.encrypt("123456")));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                })
                .into();

        // 设置 Json 解析容错监听
        GsonFactory.setJsonCallback((typeToken, fieldName, jsonToken) -> {
            // 上报到 Bugly 错误列表
            CrashReport.postCatchedException(new IllegalArgumentException(
                    "类型解析异常：" + typeToken + "#" + fieldName + "，后台返回的类型为：" + jsonToken));
        });

        // 初始化日志打印
        if (AppConfig.isLogEnable()) {
            Timber.plant(new DebugLoggerTree());
        }

        // 注册网络状态变化监听
        ConnectivityManager connectivityManager = ContextCompat.getSystemService(application, ConnectivityManager.class);
        if (connectivityManager != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(new ConnectivityManager.NetworkCallback() {
                @Override
                public void onLost(@NonNull Network network) {
                    Activity topActivity = ActivityManager.getInstance().getTopActivity();
                    if (!(topActivity instanceof LifecycleOwner)) {
                        return;
                    }

                    LifecycleOwner lifecycleOwner = ((LifecycleOwner) topActivity);
                    if (lifecycleOwner.getLifecycle().getCurrentState() != Lifecycle.State.RESUMED) {
                        return;
                    }

                    ToastUtils.show(R.string.common_network_error);
                }
            });
        }
//        CheckUpdateHelper.getInstance().check();
    }

    private void initThirdSdk(Application application) {
        // 友盟统计、登录、分享 SDK
        UmengClient.init(application, AppConfig.isLogEnable());
        AdSdkInit.init(ProcessUtils.getCurrentProcessName(),this);
    }
}