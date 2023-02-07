package com.epiboly.bea.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.epiboly.bea.rich.R;
import com.epiboly.bea.app.AppAdapter;
import com.epiboly.bea.cache.UserHelper;
import com.epiboly.bea.http.api.AllianceActiveListApi;
import com.epiboly.bea.http.glide.GlideApp;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundFrameLayout;

/**
 * @author mao
 * @time 2022/12/4
 * @describe
 */
public class AllianceListAdapter extends AppAdapter<AllianceActiveListApi.Bean> {

    public AllianceListAdapter(@NonNull Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder();
    }

    private final class ViewHolder extends AppAdapter<?>.ViewHolder {


        private ImageView mIvPortrait;
        private TextView mTvNick;
        private TextView mTvPhone;
        private TextView mTvLevel;
        private QMUIRoundFrameLayout mFlLevel;
        private TextView mTvAuthentication;
        private QMUIRoundFrameLayout mFlAuthentication;
        private TextView mTvActive;
        private TextView mTvAllianceActive;
        private TextView mTvTime;
        private View mViewLine;

        private ViewHolder() {
            super(R.layout.item_alliance);
            initView();
        }

        @Override
        public void onBindView(int position) {
            if (position == 0){
                mViewLine.setVisibility(View.GONE);
            }else {
                mViewLine.setVisibility(View.VISIBLE);
            }
            AllianceActiveListApi.Bean info = getItem(position);
            if (info == null){
                return;
            }
            GlideApp.with(getContext())
                    .asBitmap()
                    .load(info.getAvatar())
                    .error(R.drawable.empty_avatar_user)
                    .transform(new CircleCrop())
                    .into(mIvPortrait);
            mTvNick.setText(info.getNickName());
            mTvPhone.setText(info.getPhone());
            mTvLevel.setText(UserHelper.getLevelDesc(info.getLevel()));
            mTvAuthentication.setText(UserHelper.getAuthenticationText(info.getAuthentication()));
            mTvActive.setText("算力："+info.getHashVal() + "");
            mTvTime.setText(info.getCreateTime());
        }

        private void initView() {
            mIvPortrait = (ImageView) findViewById(R.id.iv_portrait);
            mTvNick = (TextView) findViewById(R.id.tv_nick);
            mTvPhone = (TextView) findViewById(R.id.tv_phone);
            mTvLevel = (TextView) findViewById(R.id.tv_level);
            mFlLevel = (QMUIRoundFrameLayout) findViewById(R.id.fl_level);
            mTvAuthentication = (TextView) findViewById(R.id.tv_authentication);
            mFlAuthentication = (QMUIRoundFrameLayout) findViewById(R.id.fl_authentication);
            mTvActive = (TextView) findViewById(R.id.tv_active);
            mTvAllianceActive = (TextView) findViewById(R.id.tv_alliance_active);
            mTvTime = (TextView) findViewById(R.id.tv_time);
            mViewLine = (View) findViewById(R.id.view_line);
        }
    }
}
