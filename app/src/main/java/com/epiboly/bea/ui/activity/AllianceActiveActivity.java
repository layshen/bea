package com.epiboly.bea.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.epiboly.bea.rich.R;
import com.epiboly.bea.action.StatusAction;
import com.epiboly.bea.app.AppActivity;
import com.epiboly.bea.cache.UserHelper;
import com.epiboly.bea.http.api.AllianceActiveListApi;
import com.epiboly.bea.http.api.AllianceActiveTopApi;
import com.epiboly.bea.http.model.HttpData;
import com.epiboly.bea.http.model.IntegralServer;
import com.epiboly.bea.ui.adapter.AllianceListAdapter;
import com.epiboly.bea.widget.StatusLayout;
import com.hjq.base.BaseAdapter;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.List;

/**
 * @author vemao
 * @time 2023/2/2
 * @describe 联盟活跃度
 */
public class AllianceActiveActivity extends AppActivity implements StatusAction {

    private TextView mTvLlianceActive;
    private TextView mTvLlianceActiveName;
    private TextView mTvMyFans;
    private TextView mTvValidFans;
    private LinearLayout mLlFans;
    private RecyclerView mRecyclerView;
    private AllianceListAdapter mAllianceListAdapter;
    private int page = 1;
    private int pageSize = 20;
    private String mUid;
    private StatusLayout mStatusLayout;
    private TextView mTvMyFansDesc;
    private SmartRefreshLayout mSmartRefresh;

    public static void start(Context context, String uid) {
        Intent intent = new Intent(context, AllianceActiveActivity.class);
        intent.putExtra("uid", uid);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_alliance_active;
    }

    @Override
    protected void initView() {
        mTvLlianceActive = (TextView) findViewById(R.id.tv_lliance_active);
        mTvLlianceActiveName = (TextView) findViewById(R.id.tv_lliance_active_name);
        mTvMyFans = (TextView) findViewById(R.id.tv_my_fans);
        mTvMyFansDesc = (TextView) findViewById(R.id.tv_my_fans_desc);
        mTvValidFans = (TextView) findViewById(R.id.tv_valid_fans);
        mLlFans = (LinearLayout) findViewById(R.id.ll_fans);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAllianceListAdapter = new AllianceListAdapter(getContext());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAllianceListAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                AllianceActiveListApi.Bean item = mAllianceListAdapter.getItem(position);
                AllianceActiveActivity.start(getActivity(), item.getUid());
            }
        });
        mRecyclerView.setAdapter(mAllianceListAdapter);

        mStatusLayout = (StatusLayout) findViewById(R.id.status_layout);
        mSmartRefresh = (SmartRefreshLayout) findViewById(R.id.smart_refresh);

        mSmartRefresh.setEnableRefresh(true);
        mSmartRefresh.setEnableLoadMore(true);
        mSmartRefresh.setEnableAutoLoadMore(true);
        mSmartRefresh.setEnableFooterTranslationContent(true);
        mSmartRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                getTopInfo();
                requestHttp();
            }
        });
        mSmartRefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                requestHttp();
            }
        });
    }

    @Override
    protected void initData() {
        mUid = getString("uid");
        showLoading();
        if (TextUtils.equals(mUid, UserHelper.getInstance().getUser().getUid())) {
            mTvMyFansDesc.setText("我的粉丝");
            setTitle("我的联盟");
        } else {
            mTvMyFansDesc.setText("Ta的粉丝");
            setTitle("Ta的联盟");
        }
        page = 1;

        getTopInfo();
        EasyHttp.post(this)
                .api(new AllianceActiveListApi()
                        .setPage(page)
                        .setUid(mUid)
                        .setToken(UserHelper.getInstance().getToken()))
                .request(new HttpCallback<HttpData<List<AllianceActiveListApi.Bean>>>(this) {

                    @Override
                    public void onSucceed(HttpData<List<AllianceActiveListApi.Bean>> data) {
                        if (data.isRequestSucceed()) {
                            if (data.getData() == null || data.getData().size() == 0) {
                                showEmpty(R.string.status_layout_no_fans);
                            } else {
                                showComplete();
                                mAllianceListAdapter.setData(data.getData());
                            }
                        }
                    }
                });
    }

    private void getTopInfo() {
        EasyHttp.post(this)
                .server(new IntegralServer())
                .api(new AllianceActiveTopApi()
                        .setToken(UserHelper.getInstance().getUser().getToken())
                        .setUid(mUid))
                .request(new HttpCallback<HttpData<AllianceActiveTopApi.Bean>>(this) {

                    @Override
                    public void onSucceed(HttpData<AllianceActiveTopApi.Bean> data) {
                        if (data.isRequestSucceed()) {
                            if (data.getData() == null) {
                                showTopDefaultText();
                            } else {
                                mTvMyFans.setText(data.getData().getTotalFans());
                                mTvValidFans.setText(data.getData().getTotalValid());
                                mTvLlianceActive.setText(data.getData().getTotalHashVal());
                            }
                        }else {
                            showTopDefaultText();
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        showTopDefaultText();
                    }
                });
    }

    private void showTopDefaultText() {
        mTvMyFans.setText("--");
        mTvMyFans.setText("--");
        mTvLlianceActive.setText("--");
    }

    private void requestHttp() {
        EasyHttp.post(this)
                .api(new AllianceActiveListApi()
                        .setPage(page)
                        .setUid(mUid)
                        .setToken(UserHelper.getInstance().getToken()))
                .request(new HttpCallback<HttpData<List<AllianceActiveListApi.Bean>>>(this) {

                    @Override
                    public void onSucceed(HttpData<List<AllianceActiveListApi.Bean>> data) {
                        if (page == 1){
                            mSmartRefresh.finishRefresh();
                        }else {
                            mSmartRefresh.finishLoadMore();
                        }
                        if (data.isRequestSucceed()) {
                            if (data.getData() == null || data.getData().size() == 0) {
                                if (mAllianceListAdapter.getCount() == 0){
                                    showEmpty(R.string.status_layout_no_fans);
                                }
                                mSmartRefresh.setNoMoreData(true);
                            } else {
                                if (page == 1){
                                    mAllianceListAdapter.setData(data.getData());
                                }else {
                                    mAllianceListAdapter.addData(data.getData());
                                }
                            }
                        }
                    }
                });
    }

    @Override
    public StatusLayout getStatusLayout() {
        return mStatusLayout;
    }
}
