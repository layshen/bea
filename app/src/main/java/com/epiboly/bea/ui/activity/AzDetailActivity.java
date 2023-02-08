package com.epiboly.bea.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.epiboly.bea.app.AppActivity;
import com.epiboly.bea.rich.R;
import com.hjq.widget.layout.SettingBar;

/**
 * @author vemao
 * @time 2023/2/8
 * @describe
 */
public class AzDetailActivity extends AppActivity {

    private TextView mTvAzValue;
    private SettingBar mSbExchange;
    private SettingBar mSbRecord;

    public static void start(Context context, String value) {
        Intent intent = new Intent(context, AzDetailActivity.class);
        intent.putExtra("az",value);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_az_detail;
    }

    @Override
    protected void initView() {
        mTvAzValue = (TextView) findViewById(R.id.tv_az_value);
        mSbExchange = (SettingBar) findViewById(R.id.sb_exchange);
        mSbRecord = (SettingBar) findViewById(R.id.sb_record);
        setOnClickListener(mSbExchange,mSbRecord);
    }

    @Override
    protected void initData() {
        mTvAzValue.setText("算力："+getString("az"));
    }

    @Override
    public void onClick(View view) {
        if (view == mSbExchange){
            AzExchangeActivity.start(getActivity());
        }else if (view == mSbRecord){
            AzRecordListActivity.start(getActivity());
        }
    }
}
