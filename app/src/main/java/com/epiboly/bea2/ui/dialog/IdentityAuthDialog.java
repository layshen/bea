package com.epiboly.bea2.ui.dialog;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.epiboly.bea2.R;
import com.epiboly.bea2.aop.SingleClick;
import com.epiboly.bea2.http.api.GetCodeApi;
import com.epiboly.bea2.http.api.VerifyCodeApi;
import com.epiboly.bea2.http.model.HttpData;
import com.hjq.base.BaseDialog;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.OnHttpListener;
import com.hjq.toast.ToastUtils;
import com.hjq.widget.view.CountdownView;

/**
 * 身份认证
 */
public final class IdentityAuthDialog {

    public static final class Builder
            extends CommonDialog.Builder<Builder> {

        private final TextView mPhoneView;
        private final EditText mCodeView;
        private final CountdownView mCountdownView;

        @Nullable
        private OnListener mListener;

        /** 当前手机号 */
        private final String mPhoneNumber;

        public Builder(Context context) {
            super(context);
            setTitle("身份认证");
            setCustomView(R.layout.input_dialog);
            mPhoneView = findViewById(R.id.tv_safe_phone);
            mCodeView = findViewById(R.id.et_safe_code);
            mCountdownView = findViewById(R.id.cv_safe_countdown);
            setOnClickListener(mCountdownView);

            mPhoneNumber = "18100001413";
            // 为了保护用户的隐私，不明文显示中间四个数字
            mPhoneView.setText(String.format("%s****%s", mPhoneNumber.substring(0, 3), mPhoneNumber.substring(mPhoneNumber.length() - 4)));
        }

        public Builder setCode(String code) {
            mCodeView.setText(code);
            return this;
        }

        public Builder setListener(OnListener listener) {
            mListener = listener;
            return this;
        }

        @SingleClick
        @Override
        public void onClick(View view) {
            int viewId = view.getId();
            if (viewId == R.id.cv_safe_countdown) {
                if (true) {
                    ToastUtils.show(R.string.common_code_send_hint);
                    mCountdownView.start();
                    setCancelable(false);
                    return;
                }

                // 获取验证码
                EasyHttp.post(getDialog())
                        .api(new GetCodeApi()
                                .setPhone(mPhoneNumber))
                        .request(new OnHttpListener<HttpData<Void>>() {

                            @Override
                            public void onSucceed(HttpData<Void> data) {
                                ToastUtils.show(R.string.common_code_send_hint);
                                mCountdownView.start();
                                setCancelable(false);
                            }

                            @Override
                            public void onFail(Exception e) {
                                ToastUtils.show(e.getMessage());
                            }
                        });
            } else if (viewId == R.id.tv_ui_confirm) {
                if (mCodeView.getText().toString().length() != getResources().getInteger(R.integer.sms_code_length)) {
                    ToastUtils.show(R.string.common_code_error_hint);
                    return;
                }

                if (true) {
                    autoDismiss();
                    if (mListener == null) {
                        return;
                    }
                    mListener.onConfirm(getDialog(), mPhoneNumber, mCodeView.getText().toString());
                    return;
                }

                // 验证码校验
                EasyHttp.post(getDialog())
                        .api(new VerifyCodeApi()
                                .setPhone(mPhoneNumber)
                                .setCode(mCodeView.getText().toString()))
                        .request(new OnHttpListener<HttpData<Void>>() {

                            @Override
                            public void onSucceed(HttpData<Void> data) {
                                autoDismiss();
                                if (mListener == null) {
                                    return;
                                }
                                mListener.onConfirm(getDialog(), mPhoneNumber, mCodeView.getText().toString());
                            }

                            @Override
                            public void onFail(Exception e) {
                                ToastUtils.show(e.getMessage());
                            }
                        });
            } else if (viewId == R.id.tv_ui_cancel) {
                autoDismiss();
                if (mListener == null) {
                    return;
                }
                mListener.onCancel(getDialog());
            }
        }
    }

    public interface OnListener {

        /**
         * 点击确定时回调
         */
        void onConfirm(BaseDialog dialog, String phone, String code);

        /**
         * 点击取消时回调
         */
        default void onCancel(BaseDialog dialog) {}
    }
}