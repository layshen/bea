package com.epiboly.bea.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.epiboly.bea.advertisement.ui.SplashAdActivity;
import com.epiboly.bea.app.AppActivity;
import com.epiboly.bea.home.HomeMainActivity;
import com.epiboly.bea.splash.SplashActivity;
import com.epiboly.bea.rich.R;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject
 *    time   : 2020/11/29
 *    desc   : 重启应用
 */
public final class RestartActivity extends AppActivity {

    public static void start(Context context) {
        Intent intent = new Intent(context, RestartActivity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void initView() {}

    @Override
    protected void initData() {
        restart(this);
        finish();
        toast(R.string.common_crash_hint);
    }

    public static void restart(Context context) {
        Intent intent = new Intent(context, SplashAdActivity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }
}