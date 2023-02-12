package com.epiboly.bea.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.epiboly.bea.app.AppActivity;
import com.epiboly.bea.cache.UserHelper;
import com.epiboly.bea.http.api.UserInfoApi;
import com.epiboly.bea.http.model.HttpData;
import com.epiboly.bea.http.model.User;
import com.epiboly.bea.rich.R;
import com.epiboly.bea.util.MathUtil;
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
    }

    @Override
    protected void initData() {
        setupView();
    }

    private void setupView() {
        User user = UserHelper.getInstance().getUser();
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
