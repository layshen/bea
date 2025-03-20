package com.epiboly.bea2.advertisement.ui;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.epiboly.bea2.R;
import com.epiboly.bea2.advertisement.AdCons;
import com.epiboly.bea2.advertisement.AdSdkInit;
import com.epiboly.bea2.app.AppActivity;
import com.epiboly.bea2.app.PrivacyPolicyActivity;
import com.epiboly.bea2.cache.SettingsCache;
import com.epiboly.bea2.cache.UserHelper;
import com.epiboly.bea2.home.HomeMainActivity;
import com.epiboly.bea2.login.LoginActivity;
import com.kc.openset.ad.listener.OSETSplashAdLoadListener;
import com.kc.openset.ad.listener.OSETSplashListener;
import com.kc.openset.ad.splash.OSETSplash;
import com.kc.openset.ad.splash.OSETSplashAd;

public class SplashActivity extends AppActivity {
    private static final String TAG = "SplashActivity";

    private FrameLayout fl;
    private Activity activity;
    private boolean isOnPause = false;//判断是否跳转了广告落地页
    private boolean isClick = false;//是否进行了点击
    private boolean isClose = false;//是否回调了Close

    @Override
    protected int getLayoutId() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void initView() {
        activity = this;
        fl = findViewById(R.id.fl);
    }

    @Override
    protected void initData() {
        actionHome();
    }

    private void showAd() {
        long startTime = System.currentTimeMillis();
        OSETSplash.getInstance().setContext(this).setPosId(AdCons.POS_ID_Splash).loadAd(new OSETSplashAdLoadListener() {
            @Override
            public void onLoadSuccess(OSETSplashAd osetSplashAd) {
                int ecpm = osetSplashAd.getECPM();
                String requestId = osetSplashAd.getRequestId();
                boolean usable = osetSplashAd.isUsable();
                if (!usable) {
                    showAd();
                    return;
                }
                Log.e(TAG, "onLoadSuccess ecpm=" + ecpm);
                osetSplashAd.showAd(SplashActivity.this, fl, new OSETSplashListener() {
                    @Override
                    public void onClick() {
//                        Log.e(TAG, "onClick");
                        isClick = true;
                        Log.e(TAG, "onClick");
                    }

                    @Override
                    public void onClose() {
                        Log.e(TAG, "onclose +isOnPause=" + isOnPause + "isClick=" + isClick);
                        isClose = true;
                        if (!isOnPause && !isClick) {//如果已经调用了onPause说明已经跳转了广告落地页
                        startActivity(new Intent(activity, HomeMainActivity.class));
                        finish();
                        }
//                        Log.e(TAG, "onclose ");
//                        startActivity(new Intent(activity, MainActivity.class));
//                        finish();
                    }

                    @Override
                    public void onShow() {
                        Log.e(TAG, "onShow " + ( System.currentTimeMillis() - startTime));
                    }

                    @Override
                    public void onError(String s, String s1) {
//                        Log.e(TAG, "onError——————code:" + s + "----message:" + s1);
//                        startActivity(new Intent(activity, MainActivity.class));
//                        finish();
                        Log.e(TAG, "onError——————code:" + s + "----message:" + s1);
                        startActivity(new Intent(activity, HomeMainActivity.class));
                        finish();
                    }
                });
            }

            @Override
            public void onLoadFail(String s, String s1) {
                Log.e(TAG, "onError——————code:" + s + "----message:" + s1);
//                actionHome();
                startActivity(new Intent(activity, HomeMainActivity.class));
//                HomeMainActivity.start(this);
            }
        });
//        OSETSplash.getInstance().show(this, fl, AdCons.POS_ID_Splash, new OSETListener() {
//            @Override
//            public void onShow() {
//                Log.e(TAG, "onShow");
//            }
//
//            @Override
//            public void onError(String s, String s1) {
//                Log.e(TAG, "onError——————code:" + s + "----message:" + s1);
//                actionHome();
//            }
//
//            @Override
//            public void onClick() {
//                isClick = true;
//                Log.e(TAG, "onClick");
//            }
//
//            @Override
//            public void onClose() {
//                Log.e(TAG, "onclose +isOnPause=" + isOnPause + "isClick=" + isClick);
//                isClose = true;
//                if (!isOnPause && !isClick) {//如果已经调用了onPause说明已经跳转了广告落地页
//                    actionHome();
//                }
//            }
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");
        if (isOnPause && isClose) {
            //判断是否点击，并且跳转了落地页，如果是，就相当于走了onclose
            actionHome();
        } else {
            isClick = false;
            isOnPause = false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "onPause");
        if (isClick) {
            isOnPause = true;
        }
    }

    @Override
    protected void onDestroy() {
//        OSETSplash.getInstance().;
        super.onDestroy();
    }

    public void actionHome() {
        if (UserHelper.getInstance().isLogin() && SettingsCache.create().isAutoLogin()) {
            if (AdSdkInit.isInitSuccess){
                showAd();
            }else {
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AdSdkInit.exeMaybeInit(new AdSdkInit.Callback() {
                            @Override
                            public void onSuccess() {
                                showAd();
                            }

                            @Override
                            public void onError() {
                                actionHome();
                            }
                        });
                    }
                },300);
            }
        } else {
            UserHelper.getInstance().clear();

            // 检查用户是否已经同意隐私政策
            boolean isPrivacyPolicyAgreed = getSharedPreferences("MyPrefs", MODE_PRIVATE)
                    .getBoolean("isPrivacyPolicyAgreed", false);
            if (!isPrivacyPolicyAgreed) {
                PrivacyPolicyActivity.start(this);
            } else {
                // 用户已经同意，初始化 SDK
                LoginActivity.start(this, "", "");
            }
        }
    }
}
