package com.epiboly.bea2.ui.activity;

import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.epiboly.bea2.app.AppActivity;
import com.epiboly.bea2.http.api.ValidCodeApi;
import com.epiboly.bea2.http.model.HttpData;
import com.epiboly.bea2.manager.InputTextManager;
import com.epiboly.bea2.R;
import com.epiboly.bea2.aop.SingleClick;
import com.epiboly.bea2.http.api.GetCodeApi;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.hjq.widget.view.CountdownView;


/**
 *    desc   : 忘记密码
 */
public final class PasswordForgetActivity extends AppActivity
        implements TextView.OnEditorActionListener {

    private EditText mPhoneView;
    private EditText mCodeView;
    private CountdownView mCountdownView;
    private Button mCommitView;

    @Override
    protected int getLayoutId() {
        return R.layout.password_forget_activity;
    }

    @Override
    protected void initView() {
        mPhoneView = findViewById(R.id.et_password_forget_phone);
        mCodeView = findViewById(R.id.et_password_forget_code);
        mCountdownView = findViewById(R.id.cv_password_forget_countdown);
        mCommitView = findViewById(R.id.btn_password_forget_commit);

        setOnClickListener(mCountdownView, mCommitView);

        mCodeView.setOnEditorActionListener(this);

        InputTextManager.with(this)
                .addView(mPhoneView)
                .addView(mCodeView)
                .setMain(mCommitView)
                .build();
    }

    @Override
    protected void initData() {

    }

    @SingleClick
    @Override
    public void onClick(View view) {
        if (view == mCountdownView) {
            if (mPhoneView.getText().toString().length() != 11) {
                mPhoneView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.shake_anim));
                toast(R.string.common_phone_input_error);
                return;
            }

            // 隐藏软键盘
            hideKeyboard(getCurrentFocus());

            // 获取验证码
            EasyHttp.post(this)
                    .api(new GetCodeApi()
                            .setType(GetCodeApi.TYPE_FORGET_PSW)
                            .setPhone(mPhoneView.getText().toString()))
                    .request(new HttpCallback<HttpData<Void>>(this) {

                        @Override
                        public void onSucceed(HttpData<Void> data) {
                            if (data == null){
                                mCountdownView.fail();
                                return;
                            }
                            toast(data.getDesc());
                            if ( GetCodeApi.isSendSuccess(data.getStatus())){
                                mCountdownView.success();
                            }
                        }
                    });
        } else if (view == mCommitView) {

            if (mPhoneView.getText().toString().length() != 11) {
                mPhoneView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.shake_anim));
                toast(R.string.common_phone_input_error);
                return;
            }

            if (mCodeView.getText().toString().length() != getResources().getInteger(R.integer.sms_code_length)) {
                mCodeView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.shake_anim));
                toast(R.string.common_code_error_hint);
                return;
            }

            // 验证码校验
            EasyHttp.post(this)
                    .api(new ValidCodeApi()
                            .setPhone(mPhoneView.getText().toString())
                            .setType(GetCodeApi.TYPE_FORGET_PSW)
                            .setValidCode(mCodeView.getText().toString()))
                    .request(new HttpCallback<HttpData<Void>>(this) {

                        @Override
                        public void onSucceed(HttpData<Void> data) {
                            if (data == null){
                                toast("验证失败");
                                return;
                            }
                            if (data.isRequestSucceed()){
                                PasswordResetActivity.start(getActivity(),mPhoneView.getText().toString());
                                finish();
                            }else {
                                toast(data.getDesc());
                            }
                        }
                    });
        }
    }

    /**
     * {@link TextView.OnEditorActionListener}
     */
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE && mCommitView.isEnabled()) {
            // 模拟点击下一步按钮
            onClick(mCommitView);
            return true;
        }
        return false;
    }
}