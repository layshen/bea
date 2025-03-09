package com.epiboly.bea2.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.epiboly.bea2.app.AppActivity;
import com.epiboly.bea2.cache.UserHelper;
import com.epiboly.bea2.http.api.UserInfoApi;
import com.epiboly.bea2.http.model.HttpData;
import com.epiboly.bea2.http.model.User;
import com.epiboly.bea2.R;
import com.epiboly.bea2.util.MathUtil;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.hjq.widget.layout.SettingBar;

/**
 * @author vemao
 * @time 2023/2/8
 * @describe
 */
public class AzDetailActivity extends AppActivity {

    private SettingBar mSbExchange;
    private SettingBar mSbRecord;
    private TextView mTvTotalAzValue;
    private TextView mTvLookupAz;

    public static void start(Context context) {
        Intent intent = new Intent(context, AzDetailActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_az_detail;
    }

    @Override
    protected void initView() {
        mSbExchange = (SettingBar) findViewById(R.id.sb_exchange);
        mSbRecord = (SettingBar) findViewById(R.id.sb_record);
        setOnClickListener(mSbExchange, mSbRecord);
        mTvTotalAzValue = (TextView) findViewById(R.id.tv_total_az_value);
        mTvLookupAz = (TextView) findViewById(R.id.tv_lookup_az);
    }

    @Override
    protected void initData() {
        setupView();
    }

    private void setupView() {
        User user = UserHelper.getInstance().getUser();
        Double lockupAz = user.getLockupAz();
        String lookUpAzStr;
        if (lockupAz == null){
            lookUpAzStr = "0.00";
        }else {
            lookUpAzStr = MathUtil.format4(user.getLockupAz());
        }
        mTvLookupAz.setText("锁仓："+lookUpAzStr);
        mTvTotalAzValue.setText(MathUtil.format4(user.getIntegralAz()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserInfo();
    }

    @Override
    public void onClick(View view) {
        if (view == mSbExchange){
            AzExchangeActivity.start(getActivity());
        }else if (view == mSbRecord){
            AzRecordListActivity.start(getActivity());
        }
    }

    private void getUserInfo() {
        EasyHttp.post(this)
                .api(new UserInfoApi()
                        .setToken(UserHelper.getInstance().getUser().getToken())
                        .setUid(UserHelper.getInstance().getUser().getUid()))
                .request(new HttpCallback<HttpData<User>>(this) {

                    @Override
                    public void onSucceed(HttpData<User> data) {
                        if (data!=null && data.isRequestSucceed()){
                            if (TextUtils.isEmpty(data.getData().getToken())){
                                data.getData().setToken(UserHelper.getInstance().getUser().getToken());
                            }
                            UserHelper.getInstance().saveUserInfo(data.getData());
                            setupView();
                        }
                    }
                });
    }
}
