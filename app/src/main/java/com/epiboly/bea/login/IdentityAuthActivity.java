package com.epiboly.bea.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

import androidx.appcompat.widget.AppCompatTextView;

import com.epiboly.bea.aop.Log;
import com.epiboly.bea.app.AppActivity;
import com.epiboly.bea.cache.UserHelper;
import com.epiboly.bea.http.api.IdentityAuthApi;
import com.epiboly.bea.http.model.HttpData;
import com.epiboly.bea.rich.R;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.hjq.widget.view.RegexEditText;
import com.hjq.widget.view.SubmitButton;

import okhttp3.Call;

/**
 * @author vemao
 * @time 2023/1/21
 * @describe
 */
public class IdentityAuthActivity extends AppActivity {
    private SubmitButton mCommitView;
    private AppCompatTextView mTvRegisterTitle;
    private RegexEditText mEtName;
    private RegexEditText mEtPhone;
    private RegexEditText mEtIdCard;

    @Log
    public static void start(Context context) {
        Intent intent = new Intent(context, IdentityAuthActivity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_identity_auth;
    }

    @Override
    protected void initView() {
        mCommitView = findViewById(R.id.btn_auth_commit);
        setOnClickListener(mCommitView);
        mTvRegisterTitle = (AppCompatTextView) findViewById(R.id.tv_register_title);
        mEtName = (RegexEditText) findViewById(R.id.et_name);
        mEtPhone = (RegexEditText) findViewById(R.id.et_phone);
        mEtIdCard = (RegexEditText) findViewById(R.id.et_id_card);
    }

    @Override
    protected void initData() {
        mEtPhone.setText(UserHelper.getInstance().getUser().getPhone());
        mEtPhone.setEnabled(false);
    }

    @Override
    public void onClick(View view) {
        if (view == mCommitView){
            submit();
        }
    }

    private void submit() {
        String name = mEtName.getText().toString();
        if (TextUtils.isEmpty(name)){
            mEtName.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.shake_anim));
            toast("请输入姓名");
            mCommitView.showError(2000);
            return;
        }
        String idCard = mEtIdCard.getText().toString();
        if (TextUtils.isEmpty(idCard)){
            mEtIdCard.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.shake_anim));
            toast("请输入身份证号");
            mCommitView.showError(2000);
            return;
        }
        String phone = mEtPhone.getText().toString();
        EasyHttp.post(this)
                .api(new IdentityAuthApi()
                        .setIdCard(idCard)
                        .setName(name)
                        .setPhone(phone)
                        .setToken(UserHelper.getInstance().getUser().getToken())
                        .setUid(UserHelper.getInstance().getUser().getUid()))
                .request(new HttpCallback<HttpData<Void>>(this) {

                    @Override
                    public void onStart(Call call) {
                        mCommitView.showProgress();
                    }

                    @Override
                    public void onEnd(Call call) {}

                    @Override
                    public void onSucceed(HttpData<Void> data) {
                        if (data == null){
                            postDelayed(() -> {
                                mCommitView.showError(3000);
                            }, 1000);
                            return;
                        }
                        toast(data.getDesc());
                        if (data.getStatus() == 1034){
                            finish();
                        }else {
                            mCommitView.showError(3000);
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
