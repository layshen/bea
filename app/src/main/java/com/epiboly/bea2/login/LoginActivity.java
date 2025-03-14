package com.epiboly.bea2.login;

import static com.epiboly.bea2.PrivacyInfo.PRIVACY_INFO;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.epiboly.bea2.cache.UserHelper;
import com.epiboly.bea2.home.HomeMainActivity;
import com.epiboly.bea2.http.api.GetCodeApi;
import com.epiboly.bea2.http.api.ValidCodeApi;
import com.epiboly.bea2.http.model.User;
import com.epiboly.bea2.other.AppConfig;
//import com.epiboly.bea2.ui.activity.MainActivity;
import com.epiboly.bea2.ui.activity.HomeActivity;
import com.epiboly.bea2.ui.activity.PasswordResetActivity;
import com.epiboly.bea2.ui.dialog.SafeDialog;
import com.epiboly.bea2.ui.dialog.UpdateDialog;
import com.epiboly.bea2.util.CheckUpdateHelper;
import com.gyf.immersionbar.ImmersionBar;
import com.epiboly.bea2.R;
import com.epiboly.bea2.aop.Log;
import com.epiboly.bea2.aop.SingleClick;
import com.epiboly.bea2.app.AppActivity;
import com.epiboly.bea2.http.api.LoginApi;
import com.epiboly.bea2.http.glide.GlideApp;
import com.epiboly.bea2.http.model.HttpData;
import com.epiboly.bea2.manager.InputTextManager;
import com.epiboly.bea2.other.KeyboardWatcher;
import com.epiboly.bea2.ui.activity.PasswordForgetActivity;
import com.epiboly.bea2.ui.fragment.MineFragment;
import com.epiboly.bea2.wxapi.WXEntryActivity;
import com.hjq.base.BaseDialog;
import com.hjq.http.EasyConfig;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.hjq.http.listener.OnHttpListener;
import com.hjq.toast.ToastUtils;
import com.hjq.umeng.Platform;
import com.hjq.umeng.UmengClient;
import com.hjq.umeng.UmengLogin;
import com.hjq.widget.view.SubmitButton;

import okhttp3.Call;

/**
 * @author mao
 * @time 2022/11/27
 * @describe
 */

public final class LoginActivity extends AppActivity
        implements UmengLogin.OnLoginListener,
        KeyboardWatcher.SoftKeyboardStateListener,
        TextView.OnEditorActionListener {

    private static final String INTENT_KEY_IN_PHONE = "phone";
    private static final String INTENT_KEY_IN_PASSWORD = "password";

    @Log
    public static void start(Context context, String phone, String password) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra(INTENT_KEY_IN_PHONE, phone);
        intent.putExtra(INTENT_KEY_IN_PASSWORD, password);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    private ImageView mLogoView;

    private ViewGroup mBodyLayout;
    private EditText mPhoneView;
    private EditText mPasswordView;

    private View mRegistView;
    private SubmitButton mCommitView;

    private View mOtherView;
    private View mQQView;
    private View mWeChatView;

    private View mTopPrivacyView;
    private View mPrivacyView;
    private View mPrivacyAnd;
    private View mPrivacyService;
    private CheckBox cbAgreement;
    private WebView privacyH5View;

    private View mLoginVerficationCode;

    /** logo 缩放比例 */
    private final float mLogoScale = 0.8f;
    /** 动画时间 */
    private final int mAnimTime = 300;

    @Override
    protected int getLayoutId() {
        return R.layout.login_activity;
    }

    @Override
    protected void initView() {
        mLogoView = findViewById(R.id.iv_login_logo);
        mBodyLayout = findViewById(R.id.ll_login_body);
        mPhoneView = findViewById(R.id.et_login_phone);
        mPasswordView = findViewById(R.id.et_login_password);
        mRegistView = findViewById(R.id.tv_register);
//        mLoginVerficationCode = findViewById(R.id.tv_login_verfication_code);
        mCommitView = findViewById(R.id.btn_login_commit);
        mOtherView = findViewById(R.id.ll_login_other);
        mQQView = findViewById(R.id.iv_login_qq);
        mWeChatView = findViewById(R.id.iv_login_wechat);

        mTopPrivacyView = findViewById(R.id.layout_agreement);
        mPrivacyView = findViewById(R.id.tv_agreement_view);
        cbAgreement = findViewById(R.id.cb_agreement);
        mPrivacyAnd = findViewById(R.id.tv_agreement_and);
        mPrivacyService = findViewById(R.id.tv_agreement_service);
        privacyH5View = findViewById(R.id.webview_h5);


        setOnClickListener( mCommitView, mQQView, mWeChatView, mPrivacyView, mPrivacyService, mRegistView);

        mPasswordView.setOnEditorActionListener(this);

        InputTextManager.with(this)
                .addView(mPhoneView)
                .addView(mPasswordView)
                .setMain(mCommitView)
                .build();

    }

    @Override
    protected void initData() {
        postDelayed(() -> {
            KeyboardWatcher.with(LoginActivity.this)
                    .setListener(LoginActivity.this);
        }, 500);

//        showAgreementDetails();

        // 判断用户当前有没有安装 QQ
        if (!UmengClient.isAppInstalled(this, Platform.QQ)) {
            mQQView.setVisibility(View.GONE);
        }

        // 判断用户当前有没有安装微信
        if (!UmengClient.isAppInstalled(this, Platform.WECHAT)) {
            mWeChatView.setVisibility(View.GONE);
        }

        // 如果这两个都没有安装就隐藏提示
        if (mQQView.getVisibility() == View.GONE && mWeChatView.getVisibility() == View.GONE) {
            mOtherView.setVisibility(View.GONE);
        }

        // 自动填充手机号和密码
        mPhoneView.setText(getString(INTENT_KEY_IN_PHONE));
        mPasswordView.setText(getString(INTENT_KEY_IN_PASSWORD));

        if (CheckUpdateHelper.getInstance().isNeedUpdate(AppConfig.getVersionCode(),AppConfig.getVersionName())) {
            new UpdateDialog.Builder(this)
                    .setVersionName(CheckUpdateHelper.getInstance().getVersionInfo().getVersionName())
                    .setForceUpdate(CheckUpdateHelper.getInstance().getVersionInfo().isFocusUpdate())
                    .setDownloadUrl(CheckUpdateHelper.getInstance().getVersionInfo().getUrl())
                    .setUpdateLog(CheckUpdateHelper.getInstance().getVersionInfo().getUpdateLog())
                    .show();
        }
    }

    @Override
    public void onRightClick(View view) {
        // 跳转到注册界面
        RegisterActivity.start(this, mPhoneView.getText().toString(),
                mPasswordView.getText().toString(), (phone, password) -> {
            // 如果已经注册成功，就执行登录操作
            mPhoneView.setText(phone);
            mPasswordView.setText(password);
            mPasswordView.requestFocus();
            mPasswordView.setSelection(mPasswordView.getText().length());
            onClick(mCommitView);
        });

        HomeActivity.start(getActivity());
    }

    @SingleClick
    @Override
    public void onClick(View view) {
        if (view == mRegistView) {
            startActivity(RegisterActivity.class);
            return;
        }
//        if (view == mLoginVerficationCode){
//            VerificationCodeLoginActivity.start(getActivity());
//            return;
//        }

        if (view == mPrivacyView) {
            // 配置 WebView
            WebSettings webSettings = privacyH5View.getSettings();
            webSettings.setJavaScriptEnabled(true); // 启用 JavaScript
            privacyH5View.setWebViewClient(new CustomWebViewClient()); // 设置 WebViewClient，使页面在 WebView 内加载
            // 显示 WebView
            privacyH5View.setVisibility(View.VISIBLE);
            // 加载指定的 H5 页面，将 URL 替换为实际的 H5 页面地址
            privacyH5View.loadUrl("https://layshen.fun/wechat/privacy/aggrement");
        }

        if (view == mPrivacyService) {
            // 配置 WebView
            WebSettings webSettings = privacyH5View.getSettings();
            webSettings.setJavaScriptEnabled(true); // 启用 JavaScript
            privacyH5View.setWebViewClient(new CustomWebViewClient()); // 设置 WebViewClient，使页面在 WebView 内加载
            // 显示 WebView
            privacyH5View.setVisibility(View.VISIBLE);
            // 加载指定的 H5 页面，将 URL 替换为实际的 H5 页面地址
            privacyH5View.loadUrl("https://layshen.fun/wechat/privacy/service");
        }

        if (view == mCommitView) {
//            if (mPhoneView.getText().toString().length() != 11) {
//                mPhoneView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.shake_anim));
//                mCommitView.showError(3000);
//                toast(R.string.common_phone_input_error);
//                return;
//            }

            // 隐藏软键盘
            hideKeyboard(getCurrentFocus());

//            if (true) {
//                mCommitView.showProgress();
//                postDelayed(() -> {
//                    mCommitView.showSucceed();
//                    postDelayed(() -> {
//                        HomeMainActivity.start(getContext(), MineFragment.class);
//                        finish();
//                    }, 1000);
//                }, 2000);
//                return;
//            }
            if (!cbAgreement.isChecked()) {
                mCommitView.showError(3000);
                showAgreementAlert();
                return;
            }

            login();
            return;
        }

        if (view == mQQView || view == mWeChatView) {
            toast("记得改好第三方 AppID 和 Secret，否则会调不起来哦");
            Platform platform;
            if (view == mQQView) {
                platform = Platform.QQ;
            } else if (view == mWeChatView) {
                platform = Platform.WECHAT;
                toast("也别忘了改微信 " + WXEntryActivity.class.getSimpleName() + " 类所在的包名哦");
            } else {
                throw new IllegalStateException("are you ok?");
            }
            UmengClient.login(this, platform, this);
        }
    }

    private void login() {
        if (mPhoneView == null || mPasswordView == null){
            return;
        }
        EasyHttp.post(this)
                .api(new LoginApi()
                        .setPhone(mPhoneView.getText().toString().trim())
                        .setPassword(mPasswordView.getText().toString()))
                .request(new HttpCallback<HttpData<User>>(this) {

                    @Override
                    public void onStart(Call call) {
                        mCommitView.showProgress();
                    }

                    @Override
                    public void onEnd(Call call) {}

                    @Override
                    public void onSucceed(HttpData<User> data) {
                        if (data == null){
                            toast("请求失败");
                            mCommitView.showError(3000);
                            return;
                        }
                        if (data.isRequestSucceed()){
                            // 更新 Token
                            UserHelper.getInstance().setFocusIsLogin(true);
                            EasyConfig.getInstance().addParam("token", data.getData().getToken());
                            UserHelper.getInstance().saveUserInfo(data.getData());
                            postDelayed(() -> {
                                mCommitView.showSucceed();
                                postDelayed(() -> {
                                    // 跳转到首页
                                    HomeMainActivity.start(getContext());
                                    finish();
                                }, 1000);
                            }, 1000);
                        }else {
                            postDelayed(() -> {
                                toast(data.getDesc());
                                mCommitView.showError(3000);
                            }, 1000);
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        postDelayed(() -> {
                            mCommitView.showError(3000);
                        }, 1000);
                    }
                });
    }

    private void checkVerificationCode() {
        // 验证码校验
        new SafeDialog.Builder(this)
                .setType(GetCodeApi.TYPE_LOGON)
                .setPhoneNumber(mPhoneView.getText().toString().trim())
                .setListener(new SafeDialog.OnListener() {
                    @Override
                    public void onConfirm(BaseDialog dialog, String phone, String code) {
                       login();
                    }

                    @Override
                    public void onCancel(BaseDialog dialog) {
                        mCommitView.showError(2000);
                        UserHelper.getInstance().clear();
                    }
                })
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 友盟回调
        UmengClient.onActivityResult(this, requestCode, resultCode, data);
    }

    /**
     * {@link UmengLogin.OnLoginListener}
     */

    /**
     * 授权成功的回调
     *
     * @param platform      平台名称
     * @param data          用户资料返回
     */
    @Override
    public void onSucceed(Platform platform, UmengLogin.LoginData data) {
        if (isFinishing() || isDestroyed()) {
            // Glide：You cannot start a load for a destroyed activity
            return;
        }

        // 判断第三方登录的平台
        switch (platform) {
            case QQ:
                break;
            case WECHAT:
                break;
            default:
                break;
        }

        GlideApp.with(this)
                .load(data.getAvatar())
                .circleCrop()
                .into(mLogoView);

        toast("昵称：" + data.getName() + "\n" +
                "性别：" + data.getSex() + "\n" +
                "id：" + data.getId() + "\n" +
                "token：" + data.getToken());
    }

    /**
     * 授权失败的回调
     *
     * @param platform      平台名称
     * @param t             错误原因
     */
    @Override
    public void onError(Platform platform, Throwable t) {
        toast("第三方登录出错：" + t.getMessage());
    }

    /**
     * {@link KeyboardWatcher.SoftKeyboardStateListener}
     */

    @Override
    public void onSoftKeyboardOpened(int keyboardHeight) {
        // 执行位移动画
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mBodyLayout, "translationY", 0, -mCommitView.getHeight());
        objectAnimator.setDuration(mAnimTime);
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        objectAnimator.start();

        // 执行缩小动画
        mLogoView.setPivotX(mLogoView.getWidth() / 2f);
        mLogoView.setPivotY(mLogoView.getHeight());
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(mLogoView, "scaleX", 1f, mLogoScale);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(mLogoView, "scaleY", 1f, mLogoScale);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(mLogoView, "translationY", 0f, -mCommitView.getHeight());
        animatorSet.play(translationY).with(scaleX).with(scaleY);
        animatorSet.setDuration(mAnimTime);
        animatorSet.start();
    }

    @Override
    public void onSoftKeyboardClosed() {
        // 执行位移动画
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mBodyLayout, "translationY", mBodyLayout.getTranslationY(), 0f);
        objectAnimator.setDuration(mAnimTime);
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        objectAnimator.start();

        if (mLogoView.getTranslationY() == 0) {
            return;
        }

        // 执行放大动画
        mLogoView.setPivotX(mLogoView.getWidth() / 2f);
        mLogoView.setPivotY(mLogoView.getHeight());
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(mLogoView, "scaleX", mLogoScale, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(mLogoView, "scaleY", mLogoScale, 1f);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(mLogoView, "translationY", mLogoView.getTranslationY(), 0f);
        animatorSet.play(translationY).with(scaleX).with(scaleY);
        animatorSet.setDuration(mAnimTime);
        animatorSet.start();
    }

    /**
     * {@link TextView.OnEditorActionListener}
     */
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE && mCommitView.isEnabled()) {
            // 模拟点击登录按钮
            onClick(mCommitView);
            return true;
        }
        return false;
    }

    @NonNull
    @Override
    protected ImmersionBar createStatusBarConfig() {
        return super.createStatusBarConfig()
                // 指定导航栏背景颜色
                .navigationBarColor(R.color.white);
    }

    private void showAgreementAlert() {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("请先详细阅读并勾选下方《隐私政策》和《用户协议》")
                .setPositiveButton("确定", null)
                .show();
    }

//    private void showAgreementDetails() {
//        SharedPreferences privacyInfo = getSharedPreferences("privacyInfo", MODE_PRIVATE);
//        boolean status = privacyInfo.getBoolean("privacyStatus", false);
//        if (!status) {
//            showPrivacy();
//        }
//
//    }
//
//    private void showPrivacy() {
//        new AlertDialog.Builder(this)
//                .setTitle("BEA隐私政策")
//                .setMessage(PRIVACY_INFO)
//                .setPositiveButton("同意", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        SharedPreferences privacyInfo = getSharedPreferences("privacyInfo", MODE_PRIVATE);
//                        SharedPreferences.Editor edit = privacyInfo.edit();
//                        edit.putBoolean("privacyStatus", true);
//                        edit.apply();
////                        cbAgreement.setChecked(true);
//                        dialog.dismiss();
//                    }
//                })
//                .setNegativeButton("不同意", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        // 点击不同意时关闭 App
//                        finishAffinity();
//                    }
//                })
//                .show();
//    }

    private class CustomWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("app://back")) {
               privacyH5View.setVisibility(View.GONE);
                return true;
            }
            return false;
        }
    }
}