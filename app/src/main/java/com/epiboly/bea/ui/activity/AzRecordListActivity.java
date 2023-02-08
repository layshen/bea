package com.epiboly.bea.ui.activity;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.epiboly.bea.rich.R;
import com.epiboly.bea.action.StatusAction;
import com.epiboly.bea.app.AppActivity;
import com.epiboly.bea.cache.UserHelper;
import com.epiboly.bea.http.api.AzRecordListApi;
import com.epiboly.bea.http.model.HttpData;
import com.epiboly.bea.http.model.IntegralServer;
import com.epiboly.bea.ui.adapter.AzRecordListAdapter;
import com.epiboly.bea.widget.StatusLayout;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;

/**
 * @author vemao
 * @time 2023/2/6
 * @describe
 */
public class AzRecordListActivity extends AppActivity implements StatusAction {
    private RecyclerView mRecyclerView;
    private SmartRefreshLayout mSmartRefresh;
    private StatusLayout mStatusLayout;
    private AzRecordListAdapter mAdapter;

    public static void start(Context context) {
        Intent intent = new Intent(context, AzRecordListActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_az_record_list;
    }

    @Override
    protected void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mSmartRefresh = (SmartRefreshLayout) findViewById(R.id.smart_refresh);
        mStatusLayout = (StatusLayout) findViewById(R.id.status_layout);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new AzRecordListAdapter(getContext());
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
                    AzRecordListApi.Bean item = mAdapter.getItem(mAdapter.getCount() - 1);
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
        EasyHttp.post(this)
                .server(new IntegralServer())
                .api(new AzRecordListApi()
                        .setUserExchangeId(0)
                        .setToken(UserHelper.getInstance().getUser().getToken())
                        .setUid(UserHelper.getInstance().getUser().getUid()))
                .request(new HttpCallback<HttpData<ArrayList<AzRecordListApi.Bean>>>(this) {

                    @Override
                    public void onSucceed(HttpData<ArrayList<AzRecordListApi.Bean>> data) {
                        if (data == null){
                            mSmartRefresh.finishRefresh(false);
                            return;
                        }
                        if (data.isRequestSucceed()) {
                            mSmartRefresh.finishRefresh(true);
                            mAdapter.setData(data.getData());
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
        EasyHttp.post(this)
                .server(new IntegralServer())
                .api(new AzRecordListApi()
                        .setUserExchangeId(id)
                        .setToken(UserHelper.getInstance().getUser().getToken())
                        .setUid(UserHelper.getInstance().getUser().getUid()))
                .request(new HttpCallback<HttpData<ArrayList<AzRecordListApi.Bean>>>(this) {

                    @Override
                    public void onSucceed(HttpData<ArrayList<AzRecordListApi.Bean>> data) {
                        if (data == null){
                            mSmartRefresh.finishLoadMore(false);
                            mSmartRefresh.setNoMoreData(true);
                            return;
                        }
                        if (data.isRequestSucceed()) {
                            mSmartRefresh.finishLoadMore(true);
                            if (data.getData() == null || data.getData().size() == 0){
                                mSmartRefresh.setNoMoreData(true);
                                return;
                            }
                            mAdapter.addData(data.getData());
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
