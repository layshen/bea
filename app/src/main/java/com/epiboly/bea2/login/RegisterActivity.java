package com.epiboly.bea2.login;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.epiboly.bea2.app.AppActivity;
import com.epiboly.bea2.http.model.HttpData;
import com.epiboly.bea2.manager.InputTextManager;
import com.gyf.immersionbar.ImmersionBar;
import com.hjq.base.BaseActivity;
import com.epiboly.bea2.R;
import com.epiboly.bea2.aop.Log;
import com.epiboly.bea2.aop.SingleClick;
import com.epiboly.bea2.http.api.GetCodeApi;
import com.epiboly.bea2.http.api.RegisterApi;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.hjq.widget.view.CountdownView;
import com.hjq.widget.view.SubmitButton;

import okhttp3.Call;

/**
 * @author mao
 * @time 2022/11/27
 * @describe 注册页面
 */
public final class RegisterActivity extends AppActivity
        implements TextView.OnEditorActionListener {

    private static final String INTENT_KEY_PHONE = "phone";
    private static final String INTENT_KEY_PASSWORD = "password";

    @Log
    public static void start(BaseActivity activity, String phone, String password, OnRegisterListener listener) {
        Intent intent = new Intent(activity, RegisterActivity.class);
        intent.putExtra(INTENT_KEY_PHONE, phone);
        intent.putExtra(INTENT_KEY_PASSWORD, password);
//        activity.startActivityForResult(intent, (resultCode, data) -> {
//
//            if (listener == null || data == null) {
//                return;
//            }
//
//            if (resultCode == RESULT_OK) {
//                listener.onSucceed(data.getStringExtra(INTENT_KEY_PHONE), data.getStringExtra(INTENT_KEY_PASSWORD));
//            } else {
//                listener.onCancel();
//            }
//        });
    }

    private EditText mPhoneView;
    private EditText mNickView;
    private CountdownView mCountdownView;

    private EditText mCodeView;

    private EditText mFirstPassword;
    private EditText mSecondPassword;

    private SubmitButton mCommitView;
    private EditText mRecommendUidView;
    private CheckBox cbAgreement;


    @Override
    protected int getLayoutId() {
        return R.layout.register_activity;
    }

    @Override
    protected void initView() {
        mPhoneView = findViewById(R.id.et_register_phone);
//        mCountdownView = findViewById(R.id.cv_register_countdown);
        mCodeView = findViewById(R.id.et_register_code);
        mFirstPassword = findViewById(R.id.et_register_password1);
        mSecondPassword = findViewById(R.id.et_register_password2);
        mCommitView = findViewById(R.id.btn_register_commit);
        mNickView = findViewById(R.id.et_nick_name);
//        mRecommendUidView = findViewById(R.id.et_recommend_uid);
        setOnClickListener(mCommitView);
        cbAgreement = findViewById(R.id.cb_agreement);

        mSecondPassword.setOnEditorActionListener(this);

        // 给这个 View 设置沉浸式，避免状态栏遮挡
        ImmersionBar.setTitleBar(this, findViewById(R.id.tv_register_title));

        InputTextManager.with(this)
                .addView(mPhoneView)
                .addView(mCodeView)
                .addView(mFirstPassword)
                .addView(mSecondPassword)
                .setMain(mCommitView)
                .build();
    }

    @Override
    protected void initData() {
        // 自动填充手机号和密码
        mPhoneView.setText(getString(INTENT_KEY_PHONE));
        mFirstPassword.setText(getString(INTENT_KEY_PASSWORD));
        mSecondPassword.setText(getString(INTENT_KEY_PASSWORD));
    }

    @SingleClick
    @Override
    public void onClick(View view) {
        if (!cbAgreement.isChecked()) {
            mCommitView.showError(3000);
            showAgreementAlert();
            return;
        }
        if (view == mCountdownView) {
            if (mPhoneView.getText().toString().length() != 11) {
                mPhoneView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.shake_anim));
                toast(R.string.common_phone_input_error);
                return;
            }

//            // 获取验证码
//            EasyHttp.post(this)
//                    .api(new GetCodeApi()
//                            .setType(GetCodeApi.TYPE_REGISTER)
//                            .setPhone(mPhoneView.getText().toString()))
//                    .request(new HttpCallback<HttpData<Void>>(this) {
//
//                        @Override
//                        public void onSucceed(HttpData<Void> data) {
//                            if (data != null){
//                                if (data.getStatus() == 1027){
//                                    toast(data.getDesc());
//                                    mCountdownView.success();
//                                }else {
//                                    toast(data.getDesc());
//                                }
//                            }
//                        }
//                    });
        } else if (view == mCommitView) {

            if (mPhoneView.getText().toString().length() != 11) {
                mPhoneView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.shake_anim));
                mCommitView.showError(3000);
                toast(R.string.common_phone_input_error);
                return;
            }

//            if (mCodeView.getText().toString().length() != getResources().getInteger(R.integer.sms_code_length)) {
//                mCodeView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.shake_anim));
//                mCommitView.showError(3000);
//                toast(R.string.common_code_error_hint);
//                return;
//            }

            if (!mFirstPassword.getText().toString().equals(mSecondPassword.getText().toString())) {
                mFirstPassword.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.shake_anim));
                mSecondPassword.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.shake_anim));
                mCommitView.showError(3000);
                toast(R.string.common_password_input_unlike);
                return;
            }

            // 隐藏软键盘
            hideKeyboard(getCurrentFocus());

//            if (true) {
//                mCommitView.showProgress();
//                postDelayed(() -> {
//                    mCommitView.showSucceed();
//                    postDelayed(() -> {
//                        setResult(RESULT_OK, new Intent()
//                                .putExtra(INTENT_KEY_PHONE, mPhoneView.getText().toString())
//                                .putExtra(INTENT_KEY_PASSWORD, mFirstPassword.getText().toString()));
//                        finish();
//                    }, 1000);
//                }, 2000);
//                return;
//            }

            mCommitView.showSucceed();
            postDelayed(() -> {
                setResult(RESULT_OK, new Intent()
                        .putExtra(INTENT_KEY_PHONE, mPhoneView.getText().toString())
                        .putExtra(INTENT_KEY_PASSWORD, mFirstPassword.getText().toString()));
                finish();
            }, 1000);
            // 提交注册
//            EasyHttp.post(this)
//                    .api(new RegisterApi()
//                            .setPhone(mPhoneView.getText().toString())
////                            .setCode(mCodeView.getText().toString())
//                            .setNick(mNickView.getText().toString())
//                            .setRecommendUid(mRecommendUidView.getText().toString())
//                            .setPassword(mFirstPassword.getText().toString()))
//                    .request(new HttpCallback<HttpData<RegisterApi.Bean>>(this) {
//
//                        @Override
//                        public void onStart(Call call) {
//                            mCommitView.showProgress();
//                        }
//
//                        @Override
//                        public void onEnd(Call call) {}
//
//                        @Override
//                        public void onSucceed(HttpData<RegisterApi.Bean> data) {
//                            if (data.isRequestSucceed()){
//                                postDelayed(() -> {
//                                    mCommitView.showSucceed();
//                                    postDelayed(() -> {
//                                        setResult(RESULT_OK, new Intent()
//                                                .putExtra(INTENT_KEY_PHONE, mPhoneView.getText().toString())
//                                                .putExtra(INTENT_KEY_PASSWORD, mFirstPassword.getText().toString()));
//                                        finish();
//                                    }, 1000);
//                                }, 1000);
//                            }else {
//                                postDelayed(() -> {
//                                    toast(data.getDesc());
//                                    mCommitView.showError(3000);
//                                }, 1000);
//                            }
//                        }
//
//                        @Override
//                        public void onFail(Exception e) {
//                            super.onFail(e);
//                            postDelayed(() -> {
//                                mCommitView.showError(3000);
//                            }, 1000);
//                        }
//                    });
        }
    }

    @NonNull
    @Override
    protected ImmersionBar createStatusBarConfig() {
        return super.createStatusBarConfig()
                // 指定导航栏背景颜色
                .navigationBarColor(R.color.white)
                // 不要把整个布局顶上去
                .keyboardEnable(true);
    }

    /**
     * {@link TextView.OnEditorActionListener}
     */
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE && mCommitView.isEnabled()) {
            // 模拟点击注册按钮
            onClick(mCommitView);
            return true;
        }
        return false;
    }

    private void showAgreementAlert() {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("请先详细阅读并勾选下方《隐私政策》和《用户协议》")
                .setPositiveButton("确定", null)
                .show();
    }

    /**
     * 注册监听
     */
    public interface OnRegisterListener {

        /**
         * 注册成功
         *
         * @param phone             手机号
         * @param password          密码
         */
        void onSucceed(String phone, String password);

        /**
         * 取消注册
         */
        default void onCancel() {}
    }
}