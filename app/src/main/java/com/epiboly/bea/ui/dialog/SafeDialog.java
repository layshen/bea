package com.epiboly.bea.ui.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.epiboly.bea.cache.UserHelper;
import com.epiboly.bea.http.api.ValidCodeApi;
import com.hjq.base.BaseDialog;
import com.epiboly.bea.rich.R;
import com.epiboly.bea.aop.SingleClick;
import com.epiboly.bea.http.api.GetCodeApi;
import com.epiboly.bea.http.model.HttpData;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.OnHttpListener;
import com.hjq.toast.ToastUtils;
import com.hjq.widget.view.CountdownView;


public final class SafeDialog {

    public static final class Builder
            extends CommonDialog.Builder<Builder> {

        private final TextView mPhoneView;
        private final EditText mCodeView;
        private final CountdownView mCountdownView;

        @Nullable
        private OnListener mListener;

        /** 当前手机号 */
        private final String mPhoneNumber;
        private int mType;

        public Builder(Context context) {
            super(context);
            setTitle(R.string.safe_title);
            setCustomView(R.layout.safe_dialog);
            mPhoneView = findViewById(R.id.tv_safe_phone);
            mCodeView = findViewById(R.id.et_safe_code);
            mCountdownView = findViewById(R.id.cv_safe_countdown);
            setOnClickListener(mCountdownView);

            mPhoneNumber = UserHelper.getInstance().getUser().getPhone();
            // 为了保护用户的隐私，不明文显示中间四个数字
            mPhoneView.setText(String.format("%s****%s", mPhoneNumber.substring(0, 3), mPhoneNumber.substring(mPhoneNumber.length() - 4)));
        }

        public Builder setCode(String code) {
            mCodeView.setText(code);
            return this;
        }

        public Builder setType(int type) {
            mType = type;
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

                // 获取验证码
                EasyHttp.post(getDialog())
                        .api(new GetCodeApi()
                                .setType(mType)
                                .setPhone(mPhoneNumber))
                        .request(new OnHttpListener<HttpData<Void>>() {

                            @Override
                            public void onSucceed(HttpData<Void> data) {
                                if (data != null && GetCodeApi.isSendSuccess(data.getStatus())){
                                    ToastUtils.show(R.string.common_code_send_hint);
                                    mCountdownView.success();
                                    setCancelable(false);
                                }
                            }

                            @Override
                            public void onFail(Exception e) {
                                ToastUtils.show(e.getMessage());
                            }
                        });
            } else if (viewId == R.id.tv_ui_confirm) {
                if (TextUtils.isEmpty(mCodeView.getText().toString())) {
                    ToastUtils.show(R.string.common_code_error_hint);
                    return;
                }

//                if (true) {
//                    autoDismiss();
//                    if (mListener == null) {
//                        return;
//                    }
//                    mListener.onConfirm(getDialog(), mPhoneNumber, mCodeView.getText().toString());
//                    return;
//                }

                // 验证码校验
                EasyHttp.post(getDialog())
                        .api(new ValidCodeApi()
                                .setPhone(mPhoneNumber)
                                .setType(mType)
                                .setValidCode(mCodeView.getText().toString()))
                        .request(new OnHttpListener<HttpData<Void>>() {

                            @Override
                            public void onSucceed(HttpData<Void> data) {
                                autoDismiss();
                                ToastUtils.show("验证成功");
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