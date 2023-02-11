package com.epiboly.bea.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.epiboly.bea.app.AppActivity;
import com.epiboly.bea.cache.UserHelper;
import com.epiboly.bea.http.api.AzExchangeApi;
import com.epiboly.bea.http.api.AzRecordListApi;
import com.epiboly.bea.http.model.HttpData;
import com.epiboly.bea.http.model.IntegralServer;
import com.epiboly.bea.rich.R;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.hjq.widget.view.RegexEditText;
import com.hjq.widget.view.SubmitButton;

import java.util.ArrayList;

/**
 * @author vemao
 * @time 2023/2/8
 * @describe
 */
public class AzExchangeActivity extends AppActivity {

    private RegexEditText mInputAz;
    private SubmitButton mBtnCommit;

    public static void start(Context context) {
        Intent intent = new Intent(context, AzExchangeActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_az_exchange;
    }

    @Override
    protected void initView() {
        mInputAz = (RegexEditText) findViewById(R.id.input_az);
        mBtnCommit = (SubmitButton) findViewById(R.id.btn_commit);
    }

    @Override
    protected void initData() {
        setOnClickListener(mBtnCommit);
    }

    @Override
    public void onClick(View view) {
        if (view == mBtnCommit){
            String s = mInputAz.getText().toString();
            if (TextUtils.isEmpty(s)){
                toast("请输入要转出的数值");
                return;
            }
            float num = Float.parseFloat(s);
            if (num <= 0){
                toast("数值必须大于0");
                return;
            }
            EasyHttp.post(this)
                    .server(new IntegralServer())
                    .api(new AzExchangeApi()
                            .setAz(num)
                            .setToken(UserHelper.getInstance().getUser().getToken())
                            .setUid(UserHelper.getInstance().getUser().getUid())
                            .setToken(UserHelper.getInstance().getUser().getToken())
                            .setUid(UserHelper.getInstance().getUser().getUid()))
                    .request(new HttpCallback<HttpData<ArrayList<AzRecordListApi.Bean>>>(this) {

                        @Override
                        public void onSucceed(HttpData<ArrayList<AzRecordListApi.Bean>> data) {
                            if (data == null){
                                return;
                            }
                            if (data.isRequestSucceed()) {
                                toast("转出成功");
                                mBtnCommit.showSucceed();
                            }else {
                                toast(data.getDesc());
                                mBtnCommit.showError();
                            }
                        }

                        @Override
                        public void onFail(Exception e) {
                            toast("请求出错");
                            mBtnCommit.showError();
                        }
                    });
        }
    }
}
