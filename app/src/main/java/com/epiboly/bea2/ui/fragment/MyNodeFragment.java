package com.epiboly.bea2.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.epiboly.bea2.action.StatusAction;
import com.epiboly.bea2.app.AppAdapter;
import com.epiboly.bea2.cache.UserHelper;
import com.epiboly.bea2.http.api.UserBuyNodeInfoApi;
import com.epiboly.bea2.http.glide.GlideApp;
import com.epiboly.bea2.http.model.HttpData;
import com.epiboly.bea2.http.model.IntegralServer;
import com.epiboly.bea2.http.model.MyNodeBean;
import com.epiboly.bea2.R;
import com.epiboly.bea2.ui.activity.MineNodeActivity;
import com.epiboly.bea2.util.NodeHelper;
import com.epiboly.bea2.util.TimeUtil;
import com.epiboly.bea2.widget.StatusLayout;
import com.hjq.base.BaseAdapter;
import com.hjq.base.BaseFragment;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;

/**
 * @author vemao
 * @time 2023/2/4
 * @describe 我的节点
 */
public class MyNodeFragment extends BaseFragment<MineNodeActivity> implements StatusAction {

    private RecyclerView mRecyclerView;
    private MyNodeAdapter mMyNodeAdapter;
    private StatusLayout mStatusLayout;
    private SmartRefreshLayout mSmartRefresh;

    public static MyNodeFragment newInstance() {
        MyNodeFragment fragment = new MyNodeFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_node;
    }

    @Override
    protected void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mMyNodeAdapter = new MyNodeAdapter(getContext());
        mRecyclerView.setAdapter(mMyNodeAdapter);
        mStatusLayout = (StatusLayout) findViewById(R.id.status_layout);
        mSmartRefresh = (SmartRefreshLayout) findViewById(R.id.smart_refresh);
        mSmartRefresh.setEnableLoadMore(false);
        mSmartRefresh.setEnableAutoLoadMore(false);
        mSmartRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refresh();
            }
        });
    }

    @Override
    protected void initData() {
        showLoading();
        refresh();
    }

    private void refresh() {
        EasyHttp.post(this)
                .server(new IntegralServer())
                .api(new UserBuyNodeInfoApi()
                        .setToken(UserHelper.getInstance().getToken())
                        .setUid(UserHelper.getInstance().getUser().getUid()))
                .request(new HttpCallback<HttpData<UserBuyNodeInfoApi.Wrapper>>(null) {

                    @Override
                    public void onSucceed(HttpData<UserBuyNodeInfoApi.Wrapper> result) {
                        if (result != null && result.isRequestSucceed()) {
                            mSmartRefresh.finishRefresh(true);
                            if (result.getData() == null) {
                                showError(null);
                                return;
                            }
                            ArrayList<MyNodeBean> userNodeRefDTOList = result.getData().getUserNodeRefDTOList();
                            if (userNodeRefDTOList == null || userNodeRefDTOList.isEmpty()) {
                                showEmpty();
                            } else {
                                mMyNodeAdapter.setData(userNodeRefDTOList);
                                showComplete();
                            }
                        } else {
                            mSmartRefresh.finishRefresh(false);
                            showError(null);
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        mSmartRefresh.finishRefresh(false);
                        showError(null);
                    }
                });
    }

    @Override
    public StatusLayout getStatusLayout() {
        return mStatusLayout;
    }

    static class MyNodeAdapter extends AppAdapter<MyNodeBean> {

        public MyNodeAdapter(@NonNull Context context) {
            super(context);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder();
        }

        private final class ViewHolder extends AppAdapter<?>.ViewHolder {

            private ImageView mIvImgTree;
            private TextView mTvName;
            private TextView mTvNum;
            private TextView mTvDate;
            private TextView mTvProducedValue;
            private TextView mTvDaysRemainingValue;
            private TextView mTvTotalOutputValue;
            private TextView mTvCycle;
            private TextView mTvCycleValue;

            private ViewHolder() {
                super(R.layout.node_my_item);
                initView();
            }

            @Override
            public void onBindView(int position) {
                MyNodeBean info = getItem(position);
                GlideApp.with(getContext())
                        .asBitmap()
                        .load(NodeHelper.getDrawableByNodeType(info.getNid()))
                        .into(mIvImgTree);
                mTvName.setText(NodeHelper.getNameByType(info.getNid()));
                mTvNum.setText("日产出:" + info.getDayOut());
                try {
                    mTvDate.setText(TimeUtil.longToYMDHMS(Long.parseLong(info.getCreateTime())));
                }catch (Exception e){

                }
                mTvProducedValue.setText(info.getProduced());
                mTvDaysRemainingValue.setText(info.getDaysRemaining());
                mTvTotalOutputValue.setText(info.getTotalOutput());
                mTvCycleValue.setText(info.getCycle());
            }

            private void initView() {
                mIvImgTree = (ImageView) findViewById(R.id.iv_img_tree);
                mTvName = (TextView) findViewById(R.id.tv_name);
                mTvNum = (TextView) findViewById(R.id.tv_num);
                mTvDate = (TextView) findViewById(R.id.tv_date);
                mTvProducedValue = (TextView) findViewById(R.id.tv_produced_value);
                mTvDaysRemainingValue = (TextView) findViewById(R.id.tv_daysRemaining_value);
                mTvTotalOutputValue = (TextView) findViewById(R.id.tv_totalOutput_value);
                mTvCycle = (TextView) findViewById(R.id.tv_cycle);
                mTvCycleValue = (TextView) findViewById(R.id.tv_cycle_value);
            }
        }
    }
}
