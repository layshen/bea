package com.epiboly.bea.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.epiboly.bea.cache.SettingsCache;
import com.epiboly.bea.cache.UserHelper;
import com.epiboly.bea.http.api.GetCodeApi;
import com.epiboly.bea.ui.dialog.SafeDialog;
import com.epiboly.bea.ui.dialog.UpdateDialog;
import com.epiboly.bea.ui.dialog.UserDialogManager;
import com.epiboly.bea.rich.R;
import com.epiboly.bea.aop.SingleClick;
import com.epiboly.bea.app.AppActivity;
import com.epiboly.bea.http.api.LogoutApi;
import com.epiboly.bea.http.glide.GlideApp;
import com.epiboly.bea.http.model.HttpData;
import com.epiboly.bea.login.LoginActivity;
import com.epiboly.bea.login.PhoneResetActivity;
import com.epiboly.bea.manager.ActivityManager;
import com.epiboly.bea.manager.CacheDataManager;
import com.epiboly.bea.manager.ThreadPoolManager;
import com.epiboly.bea.other.AppConfig;
import com.hjq.base.BaseDialog;
import com.hjq.http.EasyConfig;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.hjq.widget.layout.SettingBar;
import com.hjq.widget.view.SwitchButton;

public final class SettingActivity extends AppActivity
        implements SwitchButton.OnCheckedChangeListener {

    private SettingBar mPhoneView;
    private SettingBar mPasswordView;
    private SettingBar mCleanCacheView;
    private SwitchButton mAutoSwitchView;
    private SettingBar mSettingNick;
    private UserDialogManager mUserDialogManager;
    private SettingsCache mSettingsCache;

    public static void start(Context context){
        context.startActivity(new Intent(context,SettingActivity.class));
    }
    @Override
    protected int getLayoutId() {
        return R.layout.setting_activity;
    }

    @Override
    protected void initView() {
        mSettingsCache = SettingsCache.create();
        mUserDialogManager = new UserDialogManager(getActivity(),this);
        mPhoneView = findViewById(R.id.sb_setting_phone);
        mPasswordView = findViewById(R.id.sb_setting_password);
        mCleanCacheView = findViewById(R.id.sb_setting_cache);
        mAutoSwitchView = findViewById(R.id.sb_setting_switch);
        mAutoSwitchView.setChecked(mSettingsCache.isAutoLogin());
        mSettingNick = findViewById(R.id.sb_setting_nick);
        // 设置切换按钮的监听
        mAutoSwitchView.setOnCheckedChangeListener(this);

        setOnClickListener(R.id.sb_setting_update, R.id.sb_setting_phone,
                R.id.sb_setting_password, R.id.sb_setting_agreement, R.id.sb_setting_about,
                R.id.sb_setting_cache, R.id.sb_setting_auto, R.id.sb_setting_exit,R.id.sb_setting_nick,R.id.sb_setting_trade_password);
    }

    @Override
    protected void initData() {
        // 获取应用缓存大小
        mCleanCacheView.setRightText(CacheDataManager.getTotalCacheSize(this));
        mPasswordView.setRightText("密码强度较低");
    }

    @SingleClick
    @Override
    public void onClick(View view) {
        int viewId = view.getId();
         if (viewId == R.id.sb_setting_update) {

            // 本地的版本码和服务器的进行比较
            if (20 > AppConfig.getVersionCode()) {
                new UpdateDialog.Builder(this)
                        .setVersionName("2.0")
                        .setForceUpdate(false)
                        .setUpdateLog("修复Bug\n优化用户体验")
                        .setFileMd5("560017dc94e8f9b65f4ca997c7feb326")
                        .show();
            } else {
                toast(R.string.update_no_update);
            }

        } else if (viewId == R.id.sb_setting_phone) {

             new SafeDialog.Builder(this)
                     .setType(GetCodeApi.TYPE_UPDATE_USER)
                     .setListener(new SafeDialog.OnListener() {
                         @Override
                         public void onConfirm(BaseDialog dialog, String phone, String code) {
                             PhoneResetActivity.start(getActivity(), "");
                         }
                     })
                     .show();


        } else if (viewId == R.id.sb_setting_password) {
             new SafeDialog.Builder(this)
                     .setType(GetCodeApi.TYPE_FORGET_PSW)
                     .setListener(new SafeDialog.OnListener() {
                         @Override
                         public void onConfirm(BaseDialog dialog, String phone, String code) {
                             PasswordResetActivity.start(getActivity(),UserHelper.getInstance().getUser().getPhone());
                         }
                     })
                     .show();

        } else if (viewId == R.id.sb_setting_agreement) {

//            BrowserActivity.start(this, "https://www.baidu.com");
             PrivacyAgreementActivity.start(getActivity());
        } else if (viewId == R.id.sb_setting_about) {

            startActivity(AboutActivity.class);

        } else if (viewId == R.id.sb_setting_auto) {

            // 自动登录
            mAutoSwitchView.setChecked(!mAutoSwitchView.isChecked());

        } else if (viewId == R.id.sb_setting_cache) {

            // 清除内存缓存（必须在主线程）
            GlideApp.get(getActivity()).clearMemory();
            ThreadPoolManager.getInstance().execute(() -> {
                CacheDataManager.clearAllCache(this);
                // 清除本地缓存（必须在子线程）
                GlideApp.get(getActivity()).clearDiskCache();
                post(() -> {
                    // 重新获取应用缓存大小
                    mCleanCacheView.setRightText(CacheDataManager.getTotalCacheSize(getActivity()));
                });
            });

        } else if (viewId == R.id.sb_setting_exit) {
            // 退出登录
            EasyHttp.post(this)
                    .api(new LogoutApi()
                            .setToken(UserHelper.getInstance().getUser().getToken())
                            .setUid(UserHelper.getInstance().getUser().getUid()))
                    .request(new HttpCallback<HttpData<Void>>(this) {

                        @Override
                        public void onSucceed(HttpData<Void> data) {
                            EasyConfig.getInstance().addParam("token", "");
                            UserHelper.getInstance().clear();
                            toast("退出成功");
                            startActivity(LoginActivity.class);
                            // 进行内存优化，销毁除登录页之外的所有界面
                            ActivityManager.getInstance().finishAllActivities(LoginActivity.class);
                        }

                        @Override
                        public void onFail(Exception e) {
                            super.onFail(e);
                            EasyConfig.getInstance().addParam("token", "");
                            UserHelper.getInstance().clear();
                            toast("退出成功");
                            startActivity(LoginActivity.class);
                            // 进行内存优化，销毁除登录页之外的所有界面
                            ActivityManager.getInstance().finishAllActivities(LoginActivity.class);
                        }
                    });

        }else if (viewId == R.id.sb_setting_nick){
             mUserDialogManager.showNickUpdateDialog();
         }else if (viewId == R.id.sb_setting_trade_password){
             new SafeDialog.Builder(this)
                     .setType(GetCodeApi.TYPE_FORGET_PSW)
                     .setListener(new SafeDialog.OnListener() {
                         @Override
                         public void onConfirm(BaseDialog dialog, String phone, String code) {
                             TradePasswordResetActivity.start(getActivity(),UserHelper.getInstance().getUser().getPhone());
                         }
                     })
                     .show();
         }
    }

    /**
     * {@link SwitchButton.OnCheckedChangeListener}
     */

    @Override
    public void onCheckedChanged(SwitchButton button, boolean checked) {
        mSettingsCache.setIsAutoLogin(checked);
    }
}