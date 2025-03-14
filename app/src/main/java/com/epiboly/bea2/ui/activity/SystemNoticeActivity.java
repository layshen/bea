package com.epiboly.bea2.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.epiboly.bea2.R;
import com.epiboly.bea2.action.StatusAction;
import com.epiboly.bea2.app.AppActivity;
import com.epiboly.bea2.cache.UserHelper;
import com.epiboly.bea2.http.api.SystemNoticeListApi;
import com.epiboly.bea2.http.model.HttpList2Data;
import com.epiboly.bea2.http.model.IntegralServer;
import com.epiboly.bea2.ui.adapter.SystemNoticeListAdapter;
import com.epiboly.bea2.widget.StatusLayout;
import com.hjq.base.BaseAdapter;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

/**
 * @author vemao
 * @time 2023/2/6
 * @describe
 */
public class SystemNoticeActivity extends AppActivity implements StatusAction {
    private RecyclerView mRecyclerView;
    private SmartRefreshLayout mSmartRefresh;
    private StatusLayout mStatusLayout;
    private SystemNoticeListAdapter mAdapter;
    private int mPage = 1;

    public static void start(Context context) {
        Intent intent = new Intent(context, SystemNoticeActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sys_notice;
    }

    @Override
    protected void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mSmartRefresh = (SmartRefreshLayout) findViewById(R.id.smart_refresh);
        mStatusLayout = (StatusLayout) findViewById(R.id.status_layout);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new SystemNoticeListAdapter(getContext());
        mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                SystemNoticeListApi.Bean item = mAdapter.getItem(position);
                SystemNoticeDetailActivity.text = item.getNotice();
                SystemNoticeDetailActivity.start(getActivity());
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mSmartRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refresh();
            }
        });
        mSmartRefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (mAdapter.getCount() > 0){
                    SystemNoticeListApi.Bean item = mAdapter.getItem(mAdapter.getCount() - 1);
                    getMoreData(item.getId());
                }
            }
        });
    }

    @Override
    protected void initData() {
        refresh();
    }

    private void refresh() {
        mPage = 1;
        EasyHttp.post(this)
                .server(new IntegralServer())
                .api(new SystemNoticeListApi()
                        .setPage(mPage)
                        .setToken(UserHelper.getInstance().getUser().getToken())
                        .setUid(UserHelper.getInstance().getUser().getUid()))
                .request(new HttpCallback<HttpList2Data<SystemNoticeListApi.Bean>>(this) {

                    @Override
                    public void onSucceed(HttpList2Data<SystemNoticeListApi.Bean> data) {
                        if (data == null || data.getData() == null){
                            mSmartRefresh.finishRefresh(false);
                            return;
                        }
                        if (data.isRequestSucceed()) {
                            mSmartRefresh.finishRefresh(true);
                            mAdapter.setData(data.getData().getList());
                            if (mAdapter.getCount() == 0){
                                showEmpty(R.string.status_layout_no_record);
                            }else {
                                showComplete();
                            }
                        }else {
                            mSmartRefresh.finishRefresh(false);
                            showError(null);
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        showError(null);
                    }
                });
    }


    private void getMoreData(long id) {
        mPage++;
        EasyHttp.post(this)
                .server(new IntegralServer())
                .api(new SystemNoticeListApi()
                        .setPage(mPage)
                        .setToken(UserHelper.getInstance().getUser().getToken())
                        .setUid(UserHelper.getInstance().getUser().getUid()))
                .request(new HttpCallback<HttpList2Data<SystemNoticeListApi.Bean>>(this) {

                    @Override
                    public void onSucceed(HttpList2Data<SystemNoticeListApi.Bean> data) {
                        if (data == null || data.getData() == null){
                            mSmartRefresh.finishLoadMore(false);
                            mSmartRefresh.setNoMoreData(true);
                            return;
                        }
                        if (data.isRequestSucceed()) {
                            mSmartRefresh.finishLoadMore(true);
                            if (data.getData() == null || data.getData().getList().size() == 0){
                                mSmartRefresh.setNoMoreData(true);
                                return;
                            }
                            mAdapter.addData(data.getData().getList());
                        }else {
                            mSmartRefresh.finishLoadMore(false);
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        showError(null);
                    }
                });
    }
    @Override
    public StatusLayout getStatusLayout() {
        return mStatusLayout;
    }
}
