package com.epiboly.bea.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.epiboly.bea.app.AppAdapter;
import com.epiboly.bea.R;
import com.epiboly.bea.http.glide.GlideApp;
import com.epiboly.bea.http.model.TaskBean;
import com.hjq.shape.view.ShapeTextView;
import com.qmuiteam.qmui.widget.QMUIProgressBar;

/**
 * @author mao
 * @time 2022/12/4
 * @describe
 */
public class TaskListAdapter extends AppAdapter<TaskBean> {

    public TaskListAdapter(@NonNull Context context) {
        super(context);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder();
    }

    private final class ViewHolder extends AppAdapter<?>.ViewHolder {

        private ImageView mIvIcon;
        private TextView mTvDesc;
        private QMUIProgressBar mItemProgressBar;
        private TextView mTvProgressBar;
        private ShapeTextView mTvStatus;

        private ViewHolder() {
            super(R.layout.item_task);

            mIvIcon = (ImageView) findViewById(R.id.iv_icon);
            mTvDesc = (TextView) findViewById(R.id.tv_desc);
            mItemProgressBar = (QMUIProgressBar) findViewById(R.id.item_progress_bar);
            mItemProgressBar.setType(QMUIProgressBar.TYPE_ROUND_RECT);
            mTvProgressBar = (TextView) findViewById(R.id.tv_progress_bar);
            mTvStatus = (ShapeTextView) findViewById(R.id.tv_status);
        }

        @Override
        public void onBindView(int position) {
            TaskBean info = getItem(position);

            GlideApp.with(getContext())
                    .asBitmap()
                    .load(info.getImage())
                    .transform(new CircleCrop())
                    .into(mIvIcon);

            mItemProgressBar.setProgress(info.getProgress(),true);
            mItemProgressBar.setMaxValue(info.getTotal());
            mTvProgressBar.setText(info.getProgress() + "/" + info.getTotal());
            mTvDesc.setText(info.getName());
            if (info.getProgress() == info.getTotal()){
                mTvStatus.setText("已完成");
                mTvDesc.setText(info.getName());
            }else {
                mTvStatus.setText("未完成");
            }
        }
    }
}
