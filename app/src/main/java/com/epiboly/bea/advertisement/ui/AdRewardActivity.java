package com.epiboly.bea.advertisement.ui;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.epiboly.bea.R;
import com.epiboly.bea.action.StatusAction;
import com.epiboly.bea.app.AppActivity;
import com.epiboly.bea.cache.UserHelper;
import com.epiboly.bea.http.api.FinishAdvertiseWatchApi;
import com.epiboly.bea.http.model.HttpData;
import com.epiboly.bea.http.model.IntegralServer;
import com.epiboly.bea.widget.StatusLayout;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.kc.openset.OSETVideoListener;
import com.kc.openset.ad.OSETRewardVideoCache;

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
        OSETRewardVideoCache.getInstance().setOSETVideoListener(osetVideoListener).showAd(this);
    }

    private final OSETVideoListener osetVideoListener = new OSETVideoListener() {
        @Override
        public void onShow(String key) {
            Log.d(TAG, "onShow");
        }

        @Override
        public void onError(String s, String s1) {
            Log.d(TAG, "onError");
            showError(null);
        }

        @Override
        public void onClick() {
            Log.d(TAG, "onClick");
        }

        @Override
        public void onClose(String s) {
            Log.d(TAG, "onClose" + s);
            finish();
        }

        @Override
        public void onLoad() {
            Log.d(TAG, "onLoad");
        }

        @Override
        public void onVideoStart() {
            Log.d(TAG, "onVideoStart");
            showComplete();
        }

        @Override
        public void onVideoEnd(String s) {
            Log.d(TAG, "onVideoEnd");
        }

        @Override
        public void onReward(String s) {
            OSETRewardVideoCache.getInstance().verify(s, b -> {
                Log.d(TAG, "onReward 服务器验证" + b);
                if (b){
                    //告诉服务器任务完成了
                    adVideoComplete();
                }
            });
        }
    };

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
                        toast("服务器异常");
                        Log.d(TAG, "服务器异常");
                    }
                });
    }

    @Override
    public StatusLayout getStatusLayout() {
        return mStatusLayout;
    }
}
