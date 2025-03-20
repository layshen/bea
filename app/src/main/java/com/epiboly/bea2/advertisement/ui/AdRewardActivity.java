package com.epiboly.bea2.advertisement.ui;

import static com.epiboly.bea2.advertisement.AdCons.POS_ID_RewardVideo;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.blankj.utilcode.util.ActivityUtils;
import com.epiboly.bea2.R;
import com.epiboly.bea2.action.StatusAction;
import com.epiboly.bea2.advertisement.AdSdkInit;
import com.epiboly.bea2.app.AppActivity;
import com.epiboly.bea2.cache.UserHelper;
import com.epiboly.bea2.http.api.FinishAdvertiseWatchApi;
import com.epiboly.bea2.http.model.HttpData;
import com.epiboly.bea2.http.model.IntegralServer;
import com.epiboly.bea2.http.model.User;
import com.epiboly.bea2.widget.StatusLayout;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.kc.openset.OSETVideoListener;
import com.kc.openset.ad.listener.OSETRewardAdLoadListener;
import com.kc.openset.ad.listener.OSETRewardListener;
import com.kc.openset.ad.reward.OSETRewardAd;
import com.kc.openset.ad.reward.OSETRewardVideo;

import java.lang.ref.WeakReference;

public class AdRewardActivity extends AppActivity implements StatusAction {

    public static final String TYPE_SORT = "TYPE_SORT";
    private StatusLayout mStatusLayout;

    public static void start(Context context,String type){
        Intent intent = new Intent(context,AdRewardActivity.class);
        intent.putExtra(TYPE_SORT,type);
        context.startActivity(intent);
    }
    private static final String TAG = "ssad-AdRewardActivity";

    @Override
    public int getLayoutId() {
        return R.layout.activity_reward;
    }

    @Override
    public void initView() {
        mStatusLayout = findViewById(R.id.status_layout);
    }

    @Override
    protected void initData() {
        showLoading();
        AdSdkInit.exeMaybeInit(new AdSdkInit.Callback() {
            @Override
            public void onSuccess() {
                Log.d(TAG,"showAd");
                showAd();
            }

            @Override
            public void onError() {
                if (ActivityUtils.isActivityAlive(AdRewardActivity.this)){
                    AdRewardActivity.this.showError(null);
                }
            }
        });
    }

    private void showAd() {
        if (!ActivityUtils.isActivityAlive(AdRewardActivity.this)){
            return;
        }
//        OSETRewardVideoCache.getInstance()
//                .setOSETVideoListener(new AdListener(this))
//                .showAd(this);
        showComplete();
        OSETRewardVideo.getInstance().loadAd(new OSETRewardAdLoadListener() {
            @Override
            public void onLoadSuccess(OSETRewardAd osetRewardAd) {
                int ecpm = osetRewardAd.getECPM();
                boolean usable = osetRewardAd.isUsable();
                Log.d(TAG, "激励广告 usable = " + usable);
                if (!usable) {
                    showAd();
                    return;
                }
                Log.d(TAG, "激励广告 ecpm = " + ecpm);
                osetRewardAd.showAd(AdRewardActivity.this, osetVideoListener);
            }

            @Override
            public void onLoadFail(String s, String s1) {

            }
        });
    }

    private final OSETRewardListener osetVideoListener = new OSETRewardListener() {
        @Override
        public void onShow() {
            Log.d(TAG, "onShow");
        }

        @Override
        public void onError(String s, String s1) {
            Log.d(TAG, "onError");

        }

        @Override
        public void onClick() {
            Log.d(TAG, "onClick");
        }

        @Override
        public void onClose() {
            Log.d(TAG, "onClose");
            finish();
        }

        @Override
        public void onVideoEnd() {
            Log.d(TAG, "onVideoEnd");
        }

        @Override
        public void onVideoStart() {
            Log.d(TAG, "onVideoStart");
        }

        @Override
        public void onReward() {
            Log.d(TAG, "onReward");
        }

        @Override
        public void onServiceResponse(int i) {

        }
    };

//    private static class AdListener implements OSETVideoListener {
//        WeakReference<AdRewardActivity> activityWeakRef;
//
//        public AdListener(AdRewardActivity activity){
//            activityWeakRef = new WeakReference<>(activity);
//        }
//
//        @Override
//        public void onShow(String key) {
//            Log.d(TAG, "onShow " + Thread.currentThread().getName());
//        }
//
//        @Override
//        public void onError(String s, String s1) {
//            if (ActivityUtils.isActivityAlive(activityWeakRef.get())){
//                activityWeakRef.get().showError(null);
//            }
//        }
//
//        @Override
//        public void onClick() {
//        }
//
//        @Override
//        public void onClose(String s) {
//            Log.d(TAG, "onClose "+ Thread.currentThread().getName());
//            if (ActivityUtils.isActivityAlive(activityWeakRef.get())){
//                activityWeakRef.get().finish();
//            }
//        }
//
//        @Override
//        public void onLoad() {
//            Log.d(TAG, "onLoad "+ Thread.currentThread().getName());
//        }
//
//        @Override
//        public void onVideoStart() {
//            if (ActivityUtils.isActivityAlive(activityWeakRef.get())){
//                activityWeakRef.get().showComplete();
//            }
//        }
//
//        @Override
//        public void onVideoEnd(String s) {
//            Log.d(TAG, "onVideoEnd");
//        }
//
//        @Override
//        public void onReward(String s) {
////            OSETRewardVideoCache.getInstance().verify(s, b -> {
////                Log.d(TAG, "onReward: " + s);
////                if (b){
////                    //告诉服务器任务完成了
////                    if (ActivityUtils.isActivityAlive(activityWeakRef.get())){
////                        activityWeakRef.get().adVideoComplete();
////                    }
////                }
////            });
//        }
//    };

    private void adVideoComplete() {
        EasyHttp.post(this)
            .server(new IntegralServer())
                .api(new FinishAdvertiseWatchApi()
                        .setToken(UserHelper.getInstance().getToken())
                        .setSort(getString(TYPE_SORT)))
                .request(new HttpCallback<HttpData<Void>>(null) {
                    @Override
                    public void onSucceed(HttpData<Void> data) {
                        super.onSucceed(data);
                        if (data == null){
                            finish();
                            return;
                        }
                        if (data.isRequestSucceed()){
                            toast("任务完成");
                        }else {
                            toast(data.getDesc());
                        }
                        finish();
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
//                        toast("服务器异常");
//                        Log.d(TAG, "服务器异常");
                    }
                });
    }

    @Override
    public StatusLayout getStatusLayout() {
        return mStatusLayout;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
