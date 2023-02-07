package com.epiboly.bea.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.epiboly.bea.app.AppActivity;
import com.epiboly.bea.http.api.EditPasswordApi;
import com.epiboly.bea.http.model.HttpData;
import com.epiboly.bea.manager.InputTextManager;
import com.epiboly.bea.rich.R;
import com.epiboly.bea.aop.Log;
import com.epiboly.bea.aop.SingleClick;
import com.epiboly.bea.ui.dialog.TipsDialog;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;


public final class PasswordResetActivity extends AppActivity
        implements TextView.OnEditorActionListener {

    @Log
    public static void start(Context context, String phone) {
        Intent intent = new Intent(context, PasswordResetActivity.class);
        intent.putExtra("phone",phone);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    private EditText mFirstPassword;
    private EditText mSecondPassword;
    private Button mCommitView;

    @Override
    protected int getLayoutId() {
        return R.layout.password_reset_activity;
    }

    @Override
    protected void initView() {
        mFirstPassword = findViewById(R.id.et_password_reset_password1);
        mSecondPassword = findViewById(R.id.et_password_reset_password2);
        mCommitView = findViewById(R.id.btn_password_reset_commit);

        setOnClickListener(mCommitView);

        mSecondPassword.setOnEditorActionListener(this);

        InputTextManager.with(this)
                .addView(mFirstPassword)
                .addView(mSecondPassword)
                .setMain(mCommitView)
                .build();
    }

    @Override
    protected void initData() {

    }

    @SingleClick
    @Override
    public void onClick(View view) {
        if (view == mCommitView) {

            if (!mFirstPassword.getText().toString().equals(mSecondPassword.getText().toString())) {
                mFirstPassword.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.shake_anim));
                mSecondPassword.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.shake_anim));
                toast(R.string.common_password_input_unlike);
                return;
            }

            // 隐藏软键盘
            hideKeyboard(getCurrentFocus());

            // 重置密码
            EasyHttp.post(this)
                    .api(new EditPasswordApi()
                            .setPhone(getString("phone"))
                            .setNewPassword(mFirstPassword.getText().toString()))
                    .request(new HttpCallback<HttpData<Void>>(this) {

                        @Override
                        public void onSucceed(HttpData<Void> data) {
                            if (data!= null &&data.isRequestSucceed()){
                                new TipsDialog.Builder(getActivity())
                                        .setIcon(TipsDialog.ICON_FINISH)
                                        .setMessage(R.string.password_reset_success)
                                        .setDuration(2000)
                                        .addOnDismissListener(dialog -> finish())
                                        .show();
                            }else {
                                new TipsDialog.Builder(getActivity())
                                        .setIcon(TipsDialog.ICON_ERROR)
                                        .setMessage(R.string.password_reset_fail)
                                        .setDuration(2000)
                                        .show();
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
        return false;
    }
}