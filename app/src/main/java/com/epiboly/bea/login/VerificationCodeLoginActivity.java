package com.epiboly.bea.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

import com.epiboly.bea.http.model.User;
import com.epiboly.bea.rich.R;
import com.epiboly.bea.aop.Log;
import com.epiboly.bea.app.AppActivity;
import com.epiboly.bea.cache.UserHelper;
import com.epiboly.bea.home.HomeMainActivity;
import com.epiboly.bea.http.api.GetCodeApi;
import com.epiboly.bea.http.api.LoginByCodeApi;
import com.epiboly.bea.http.api.ValidCodeApi;
import com.epiboly.bea.http.model.HttpData;
import com.epiboly.bea.manager.InputTextManager;
import com.epiboly.bea.ui.fragment.MineFragment;
import com.hjq.http.EasyConfig;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.hjq.widget.view.CountdownView;
import com.hjq.widget.view.RegexEditText;
import com.hjq.widget.view.SubmitButton;

import okhttp3.Call;

/**
 * @author mao
 * @time 2023/1/16
 * @describe 验证码登录
 */
public class VerificationCodeLoginActivity extends AppActivity{

    private CountdownView mCountdownView;
    private EditText mPhoneView;
    private RegexEditText mLoginCodeView;
    private SubmitButton mCommitView;
    private boolean isClickCountdownView = false;

    @Log
    public static void start(Context context) {
        Intent intent = new Intent(context, VerificationCodeLoginActivity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.verification_code_login_activity;
    }

    @Override
    protected void initView() {
        mPhoneView = findViewById(R.id.et_register_phone);
        mCountdownView = findViewById(R.id.cv_register_countdown);
        mLoginCodeView = findViewById(R.id.et_login_code);
        mCommitView = findViewById(R.id.btn_login_commit);

        setOnClickListener(mCommitView,mLoginCodeView,mCountdownView);

        InputTextManager.with(this)
                .addView(mPhoneView)
                .addView(mLoginCodeView)
                .setMain(mCommitView)
                .build();
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View view) {

        if (view == mCommitView) {
            if (mPhoneView.getText().toString().length() != 11) {
                mPhoneView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.shake_anim));
                mCommitView.showError(3000);
                toast(R.string.common_phone_input_error);
                return;
            }

            if (!isClickCountdownView){
                mCountdownView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.shake_anim));
                mCommitView.showError(3000);
                toast("请点击发送验证码");
                return;
            }
            // 隐藏软键盘
            hideKeyboard(getCurrentFocus());

            String phone = mPhoneView.getText().toString();
            String code = mLoginCodeView.getText().toString();
            EasyHttp.post(this)
                    .api(new ValidCodeApi()
                            .setType(GetCodeApi.TYPE_LOGON)
                            .setValidCode(code)
                            .setPhone(phone))
                    .request(new HttpCallback<HttpData<Void>>(this) {

                        @Override
                        public void onStart(Call call) {
                            mCommitView.showProgress();
                        }

                        @Override
                        public void onSucceed(HttpData<Void> data) {
                            if (data != null && data.isRequestSucceed()) {
                                loginByCodeApi(phone,code);
                            }else {
                                if (data != null){
                                    toast(data.getDesc());
                                }
                                mCommitView.showError(3000);
                            }
                        }

                        @Override
                        public void onFail(Exception e) {
                            super.onFail(e);
                            mCommitView.showError(3000);
                        }
                    });
        }else if (view == mCountdownView) {
            if (mPhoneView.getText().toString().length() != 11) {
                mPhoneView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.shake_anim));
                toast(R.string.common_phone_input_error);
                return;
            }
            isClickCountdownView = true;
            // 隐藏软键盘
            hideKeyboard(getCurrentFocus());
            // 获取验证码
            EasyHttp.post(this)
                    .api(new GetCodeApi()
                            .setType(GetCodeApi.TYPE_LOGON)
                            .setPhone(mPhoneView.getText().toString()))
                    .request(new HttpCallback<HttpData<Void>>(this) {

                        @Override
                        public void onSucceed(HttpData<Void> data) {
                            if (data != null) {
                                toast(data.getDesc());
                                if (GetCodeApi.isSendSuccess(data.getStatus())){
                                    mCountdownView.success();
                                }
                            }
                        }
                    });
        }
    }

    private void loginByCodeApi(String phone, String code) {
        EasyHttp.post(this)
                .api(new LoginByCodeApi()
                        .setPhone(phone)
                        .setValidCode(code))
                .request(new HttpCallback<HttpData<User>>(this) {

                    @Override
                    public void onStart(Call call) {
                    }

                    @Override
                    public void onEnd(Call call) {}

                    @Override
                    public void onSucceed(HttpData<User> data) {
                        if (data.isRequestSucceed()){
                            // 更新 Token
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
                        super.onFail(e);
                        postDelayed(() -> {
                            mCommitView.showError(3000);
                        }, 1000);
                    }
                });
    }
}
