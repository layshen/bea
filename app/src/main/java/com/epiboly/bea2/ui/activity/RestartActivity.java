package com.epiboly.bea2.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.epiboly.bea2.app.AppActivity;
import com.epiboly.bea2.home.HomeMainActivity;
import com.epiboly.bea2.splash.SplashActivity;
import com.epiboly.bea2.R;

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
//        Intent intent = new Intent(context, SplashAdActivity.class);
//        if (!(context instanceof Activity)) {
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        }
//        context.startActivity(intent);
    }
}