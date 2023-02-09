package com.epiboly.bea.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.epiboly.bea.app.AppAdapter;
import com.epiboly.bea.http.BZRRecordListApi;
import com.epiboly.bea.http.api.AzRecordListApi;
import com.epiboly.bea.rich.R;
import com.epiboly.bea.util.TimeUtil;

/**
 * @author vemao
 * @time 2023/2/6
 * @describe
 */
public class BZRRecordListAdapter extends AppAdapter<BZRRecordListApi.Bean> {

    public BZRRecordListAdapter(@NonNull Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder();
    }

    private final class ViewHolder extends AppAdapter<?>.ViewHolder {
        private TextView mTvNum;
        private TextView mTvCreateTime;

        private ViewHolder() {
            super(R.layout.item_bzr_record);
            initView();
        }

        @Override
        public void onBindView(int position) {
            BZRRecordListApi.Bean item = getItem(position);
            if (item == null){
                return;
            }
            mTvNum.setText(item.getBzr());
            mTvCreateTime.setText(TimeUtil.longToYMDHMS(item.getTimestamp()));
        }

        private void initView() {
            mTvNum = (TextView) findViewById(R.id.tv_num);
            mTvCreateTime = (TextView) findViewById(R.id.tv_create_time);
        }
    }
}
