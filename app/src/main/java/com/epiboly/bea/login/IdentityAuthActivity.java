package com.epiboly.bea.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import com.epiboly.bea.rich.R;
import com.epiboly.bea.aop.Log;
import com.epiboly.bea.app.AppActivity;
import com.epiboly.bea.cache.UserHelper;
import com.epiboly.bea.http.api.IdentityAuthApi;
import com.epiboly.bea.http.model.HttpData;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.hjq.widget.view.SubmitButton;

import okhttp3.Call;

/**
 * @author vemao
 * @time 2023/1/21
 * @describe
 */
public class IdentityAuthActivity extends AppActivity {
    private EditText mAuthIdCard;
    private SubmitButton mCommitView;

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
       mAuthIdCard =  findViewById(R.id.et_auth_id_card);
        mCommitView = findViewById(R.id.btn_auth_commit);
       setOnClickListener(mCommitView);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View view) {
        if (view == mCommitView){
            submit();
        }
    }

    private void submit() {
        EasyHttp.post(this)
                .api(new IdentityAuthApi()
                        .setIdCard(mAuthIdCard.getText().toString())
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
                        if (data.isRequestSucceed()){
                            postDelayed(() -> {
                                mCommitView.showSucceed();
                                postDelayed(() -> {
                                    toast("认证成功");
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
