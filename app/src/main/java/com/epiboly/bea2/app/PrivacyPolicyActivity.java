package com.epiboly.bea2.app;

import static com.epiboly.bea2.PrivacyInfo.PRIVACY_INFO;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.ProcessUtils;
import com.epiboly.bea2.R;
import com.epiboly.bea2.advertisement.AdSdkInit;
import com.epiboly.bea2.aop.Log;
import com.epiboly.bea2.cache.DeviceIdentifierHelper;
import com.epiboly.bea2.cache.UserHelper;
import com.epiboly.bea2.http.model.RequestHandler;
import com.epiboly.bea2.http.model.RequestServer;
import com.epiboly.bea2.login.LoginActivity;
import com.epiboly.bea2.other.AppConfig;
import com.epiboly.bea2.util.NetLogStrategy;
import com.hjq.http.EasyConfig;
import com.hjq.umeng.UmengClient;

import java.net.Proxy;

import okhttp3.OkHttpClient;

public class PrivacyPolicyActivity extends AppCompatActivity {

    private Application application;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_privacy_policy);

        this.application  = (AppApplication) getApplication();
        // 检查用户是否已经同意隐私政策
        boolean isPrivacyPolicyAgreed = getSharedPreferences("MyPrefs", MODE_PRIVATE)
                .getBoolean("isPrivacyPolicyAgreed", false);
        if (!isPrivacyPolicyAgreed) {
            showPrivacyPolicyDialog();
        } else {
            // 用户已经同意，初始化 SDK
            initSDK(application);
        }
    }

    @Log
    public static void start(Context context) {
        Intent intent = new Intent(context, PrivacyPolicyActivity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    private void showPrivacyPolicyDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_privacy_policy, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        Button btnDisagree = dialogView.findViewById(R.id.btn_disagree);
        Button btnAgree = dialogView.findViewById(R.id.btn_agree);
        btnDisagree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 用户不同意，退出应用
                finish();
            }
        });
        btnAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 用户同意，保存同意状态
                getSharedPreferences("MyPrefs", MODE_PRIVATE).edit()
                        .putBoolean("isPrivacyPolicyAgreed", true).apply();
                dialog.dismiss();
                // 初始化 SDK
                initSDK(application);

                Intent intent = new Intent(application, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        dialog.show();
    }

    private void initSDK(Application application) {
        DeviceIdentifierHelper.getInstance().init(application);
        android.util.Log.d("privatyPolicyAcitivity", "initSDK: 初始化设备信息");
        // 网络请求框架初始化
        EasyConfig.getInstance()
                .setInterceptor((api, params, headers) -> {
                    headers.put("device_unique_id",DeviceIdentifierHelper.getInstance().getDeviceUniqueId(application));
                });
        // 初始化 SDK 并获取用户信息和设备信息
        // 获取用户信息可能需要调用 SDK 的相关方法
        // ...
        // 友盟统计、登录、分享 SDK
        UmengClient.init(application, AppConfig.isLogEnable());
        AdSdkInit.init(ProcessUtils.getCurrentProcessName(),application);
    }
}
