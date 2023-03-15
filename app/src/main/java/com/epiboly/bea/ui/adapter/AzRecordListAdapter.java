package com.epiboly.bea.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.epiboly.bea.R;
import com.epiboly.bea.app.AppAdapter;
import com.epiboly.bea.http.api.AzRecordListApi;
import com.epiboly.bea.util.TimeUtil;

/**
 * @author vemao
 * @time 2023/2/6
 * @describe
 */
public class AzRecordListAdapter extends AppAdapter<AzRecordListApi.Bean> {

    public AzRecordListAdapter(@NonNull Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder();
    }

    private final class ViewHolder extends AppAdapter<?>.ViewHolder {
        private TextView mTvNum;
        private TextView mTvSource;
        private TextView mTvCreateTime;

        private ViewHolder() {
            super(R.layout.item_az_record);
            initView();
        }

        @Override
        public void onBindView(int position) {
            AzRecordListApi.Bean item = getItem(position);
            mTvNum.setText(item.getSignDesc() + item.getAz());
            mTvSource.setText(item.getSourceDesc()+"");
            mTvCreateTime.setText(TimeUtil.longToYMDHMS(item.getTimestamp()));
        }

        private void initView() {
            mTvNum = (TextView) findViewById(R.id.tv_num);
            mTvSource = (TextView) findViewById(R.id.tv_source);
            mTvCreateTime = (TextView) findViewById(R.id.tv_create_time);
        }
    }
}
