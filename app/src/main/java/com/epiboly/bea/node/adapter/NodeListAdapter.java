package com.epiboly.bea.node.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.epiboly.bea.rich.R;
import com.epiboly.bea.app.AppAdapter;
import com.epiboly.bea.http.glide.GlideApp;
import com.epiboly.bea.http.model.NodeBean;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundLinearLayout;

/**
 * @author vemao
 * @time 2023/1/23
 * @describe
 */
public class NodeListAdapter extends AppAdapter<NodeBean> {

    public NodeListAdapter(@NonNull Context context) {
        super(context);
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getType();
    }

    @NonNull
    @Override
    public AppAdapter<?>.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == NodeBean.COMING_TYPE){
            return new ViewHolder2();
        }else {
            return new ViewHolder();
        }
    }

    private final class ViewHolder2 extends AppAdapter<?>.ViewHolder {

        private ViewHolder2() {
            super(R.layout.node_coming_item);
        }


        @Override
        public void onBindView(int position) {

        }
    }

    private final class ViewHolder extends AppAdapter<?>.ViewHolder {

        private TextView mTvName;
        private TextView mTvFc;
        private TextView mTvLimitNum;
        private TextView mTvCycle;
        private QMUIRoundLinearLayout mLlCycle;
        private TextView mTvDiurnalCarbon;
        private QMUIRoundLinearLayout mLlDiurnalCarbon;
        private TextView mTvTotalOutput;
        private QMUIRoundLinearLayout mLlTotalOutput;
        private QMUIRoundButton mBtnPlantTree;
        private ImageView mIvImgTree;

        private ViewHolder() {
            super(R.layout.node_list_item);
            initView();
        }


        @Override
        public void onBindView(int position) {
            NodeBean info = getItem(position);
            int placeholder;
            if (info.getType() == 0){
                placeholder = R.drawable.node_png_01;
            } else if (info.getType() == 1){
                placeholder = R.drawable.node_png_02;
            }else if (info.getType() == 2){
                placeholder = R.drawable.node_png_03;
            }else if (info.getType() == 3){
                placeholder = R.drawable.node_png_04;
            }else if (info.getType() == 4){
                placeholder = R.drawable.node_png_05;
            }else if (info.getType() == 5){
                placeholder = R.drawable.node_png_06;
            }else{
                placeholder = R.drawable.node_png_06;
            }
            GlideApp.with(getContext())
                    .asBitmap()
                    .load(info.getImgUrl())
                    .placeholder(placeholder)
                    .error(placeholder)
                    .into(mIvImgTree);

            mTvName.setText(info.getName());
            mTvFc.setText(info.getPurchasingPower()+"能量");
            mTvLimitNum.setText("限购"+info.getMaxBuy());
            mTvCycle.setText(info.getCycle()+"天");
            mTvDiurnalCarbon.setText(info.getHashVal()+"");
            mTvTotalOutput.setText(info.getTotalOutput()+"");
        }

        private void initView() {
            mTvName = (TextView) findViewById(R.id.tv_name);
            mTvFc = (TextView) findViewById(R.id.tv_fc);
            mTvLimitNum = (TextView) findViewById(R.id.tv_limit_num);
            mTvCycle = (TextView) findViewById(R.id.tv_cycle);
            mLlCycle = (QMUIRoundLinearLayout) findViewById(R.id.ll_cycle);
            mTvDiurnalCarbon = (TextView) findViewById(R.id.tv_diurnal_carbon);
            mLlDiurnalCarbon = (QMUIRoundLinearLayout) findViewById(R.id.ll_diurnal_carbon);
            mTvTotalOutput = (TextView) findViewById(R.id.tv_total_output);
            mLlTotalOutput = (QMUIRoundLinearLayout) findViewById(R.id.ll_total_output);
            mBtnPlantTree = (QMUIRoundButton) findViewById(R.id.btn_plant_tree);
            mIvImgTree = (ImageView) findViewById(R.id.iv_img_tree);
        }
    }
}
