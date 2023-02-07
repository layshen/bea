package com.epiboly.bea.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import com.epiboly.bea.rich.R;
import com.epiboly.bea.app.AppActivity;
import com.zzhoujay.richtext.RichText;

/**
 * @author maoweiyi
 * @time 2023/2/6
 * @describe
 */
public class SystemNoticeDetailActivity extends AppActivity {

    private TextView mTvText;

    public static String text = "";

    public static void start(Context context) {
        Intent intent = new Intent(context, SystemNoticeDetailActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sys_notice_detail;
    }

    @Override
    protected void initView() {
        mTvText = (TextView) findViewById(R.id.tv_text);
    }

    @Override
    protected void initData() {
        RichText.from(text).into(mTvText);
    }
}
