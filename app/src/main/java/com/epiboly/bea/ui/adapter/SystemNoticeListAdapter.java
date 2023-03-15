package com.epiboly.bea.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.epiboly.bea.R;
import com.epiboly.bea.app.AppAdapter;
import com.epiboly.bea.http.api.SystemNoticeListApi;
import com.epiboly.bea.util.TimeUtil;
import com.zzhoujay.richtext.RichText;

/**
 * @author vemao
 * @time 2023/2/6
 * @describe
 */
public class SystemNoticeListAdapter extends AppAdapter<SystemNoticeListApi.Bean> {

    public SystemNoticeListAdapter(@NonNull Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder();
    }

    private final class ViewHolder extends AppAdapter<?>.ViewHolder {
        private TextView mTvNotice;
        private TextView mTvCreateTime;

        private ViewHolder() {
            super(R.layout.item_system_notice);
            initView();
        }

        @Override
        public void onBindView(int position) {
            SystemNoticeListApi.Bean item = getItem(position);
            RichText.from(item.getNotice()).clickable(false).into(mTvNotice);
            mTvCreateTime.setText(TimeUtil.longToYMDHMS(item.getTimestamp()));
        }

        private void initView() {
            mTvNotice = (TextView) findViewById(R.id.tv_notice);
            mTvCreateTime = (TextView) findViewById(R.id.tv_create_time);
        }
    }
}
