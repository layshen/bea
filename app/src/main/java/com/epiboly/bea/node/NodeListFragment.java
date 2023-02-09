package com.epiboly.bea.node;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.epiboly.bea.http.api.NodeListPurchaseNodeInfo;
import com.epiboly.bea.rich.R;
import com.epiboly.bea.action.StatusAction;
import com.epiboly.bea.app.TitleBarFragment;
import com.epiboly.bea.cache.UserHelper;
import com.epiboly.bea.home.HomeMainActivity;
import com.epiboly.bea.http.api.NodeExchangeApi;
import com.epiboly.bea.http.api.NodeListApi;
import com.epiboly.bea.http.model.HttpData;
import com.epiboly.bea.http.model.NodeBean;
import com.epiboly.bea.http.model.IntegralServer;
import com.epiboly.bea.node.adapter.NodeListAdapter;
import com.epiboly.bea.ui.dialog.TipsDialog;
import com.epiboly.bea.widget.StatusLayout;
import com.hjq.base.BaseAdapter;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopups;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mao
 * @time 2023/1/23
 * @describe
 */
public class NodeListFragment extends TitleBarFragment<HomeMainActivity> implements StatusAction {

    private RecyclerView mRecyclerView;
    private NodeListAdapter mAdapter;
    private StatusLayout mStatusLayout;
    private SmartRefreshLayout mSmartRefresh;
    private int mSupportNodeType;

    public static NodeListFragment newInstance() {
        return new NodeListFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_node_list;
    }

    @Override
    protected void initView() {
        mRecyclerView = findViewById(R.id.recycler_view);
        mStatusLayout = findViewById(R.id.status_layout);
        mAdapter = new NodeListAdapter(getContext());
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayout.VERTICAL));
        mAdapter.setOnChildClickListener(R.id.btn_plant_tree, new BaseAdapter.OnChildClickListener() {
            @Override
            public void onChildClick(RecyclerView recyclerView, View childView, int position) {
                NodeBean item = mAdapter.getItem(position);
                if (item.getType() == NodeBean.COMING_TYPE){
                    return;
                }
                if (UserHelper.getInstance().getUser().getAuthentication() == 0) {
                    toast("请先进行身份认证");
                    return;
                }
                exchangeNode(item.getId());
            }
        });
        mAdapter.setOnChildClickListener(R.id.ll_cycle, new BaseAdapter.OnChildClickListener() {
            @Override
            public void onChildClick(RecyclerView recyclerView, View childView, int position) {
                NodeBean item = mAdapter.getItem(position);
                if (item == null){
                    return;
                }
                TextView textView = new TextView(getContext());
                textView.setTextColor(Color.WHITE);
                textView.setLineSpacing(QMUIDisplayHelper.dp2px(getContext(), 4), 1.0f);
                int padding = QMUIDisplayHelper.dp2px(getContext(), 8);
                textView.setPadding(padding, padding, padding, padding);
                textView.setText(item.getDescription());

                QMUIPopups.popup(getContext(), QMUIDisplayHelper.dp2px(getContext(), 160))
                        .preferredDirection(QMUIPopup.DIRECTION_TOP)
                        .view(textView)
                        .shadow(true)
                        .arrowSize(QMUIDisplayHelper.dp2px(getContext(), 10),padding)
                        .bgColor(ContextCompat.getColor(getContext(),R.color.card_color))
                        .radius(QMUIDisplayHelper.dp2px(getContext(), 5))
                        .arrow(true)
                        .animStyle(QMUIPopup.ANIM_GROW_FROM_CENTER)
                        .show(childView);

            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mSmartRefresh = (SmartRefreshLayout) findViewById(R.id.smart_refresh);
        mSmartRefresh.setEnableAutoLoadMore(false);
        mSmartRefresh.setEnableLoadMore(false);
        mSmartRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                requestData();
            }
        });
    }

    private void exchangeNode(int id) {
        EasyHttp.post(this)
                .server(new IntegralServer())
                .api(new NodeExchangeApi()
                        .setNid(id + "")
                        .setUid(UserHelper.getInstance().getUser().getUid()))
                .request(new HttpCallback<HttpData<List<NodeBean>>>(this) {


                    @Override
                    public void onSucceed(HttpData<List<NodeBean>> data) {
                        if (data != null) {
                            if (data.isRequestSucceed()) {
                                new TipsDialog.Builder(getContext())
                                        .setIcon(TipsDialog.ICON_FINISH)
                                        .setMessage("兑换成功")
                                        .show();
                            } else {
                                new TipsDialog.Builder(getContext())
                                        .setIcon(TipsDialog.ICON_FINISH)
                                        .setMessage(data.getDesc())
                                        .show();
                            }
                        } else {
                            new TipsDialog.Builder(getContext())
                                    .setIcon(TipsDialog.ICON_FINISH)
                                    .setMessage("兑换失败")
                                    .show();
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                        new TipsDialog.Builder(getContext())
                                .setIcon(TipsDialog.ICON_FINISH)
                                .setMessage("兑换失败")
                                .show();
                    }
                });
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void onFragmentResume(boolean first) {
        super.onFragmentResume(first);
        if (first){
            showLoading();
        }
        requestData();
    }

    private void requestData() {
        EasyHttp.post(this)
                .server(new IntegralServer())
                .api(new NodeListPurchaseNodeInfo()
                        .setUid(UserHelper.getInstance().getUser().getUid())
                        .setToken(UserHelper.getInstance().getToken()))
                .request(new HttpCallback<HttpData<NodeListPurchaseNodeInfo.Bean>>(this) {


                    @Override
                    public void onSucceed(HttpData<NodeListPurchaseNodeInfo.Bean> data) {
                        if (data == null || data.getData() == null){
                            showError(listener -> {
                                showLoading();
                                requestData();
                            });
                            mSmartRefresh.finishRefresh(false);
                            return;
                        }
                        if (data.isRequestSucceed()) {
                            mSupportNodeType = data.getData().nodeType;
                            mSmartRefresh.finishRefresh(true);
                            requestNodeList();
                        } else {
                            showError(listener -> {
                                showLoading();
                                requestData();
                            });
                            mSmartRefresh.finishRefresh(false);
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                        mSmartRefresh.finishRefresh(false);
                        showError(listener -> {
                            showLoading();
                            requestData();
                        });
                    }
                });
    }

    private void requestNodeList() {
        EasyHttp.post(this)
                .server(new IntegralServer())
                .api(new NodeListApi()
                        .setToken(UserHelper.getInstance().getToken()))
                .request(new HttpCallback<HttpData<List<NodeBean>>>(this) {


                    @Override
                    public void onSucceed(HttpData<List<NodeBean>> data) {
                        if (data == null){
                            showError(listener -> {
                                showLoading();
                                requestData();
                            });
                            mSmartRefresh.finishRefresh(false);
                            return;
                        }
                        if (data.isRequestSucceed()) {
                            List<NodeBean> list = new ArrayList<>();
                            list.addAll(data.getData());
                            NodeBean nodeBean = new NodeBean();
                            nodeBean.setType(NodeBean.COMING_TYPE);
                            list.add(nodeBean);
                            mAdapter.setSupportType(mSupportNodeType);
                            mAdapter.setData(list);
                            showComplete();
                            mSmartRefresh.finishRefresh(true);
                        } else {
                            showError(listener -> {
                                showLoading();
                                requestData();
                            });
                            mSmartRefresh.finishRefresh(false);
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                        mSmartRefresh.finishRefresh(false);
                        showError(listener -> {
                            showLoading();
                            requestData();
                        });
                    }
                });
    }

    @Override
    public boolean isStatusBarEnabled() {
        return true;
    }

    @Override
    public StatusLayout getStatusLayout() {
        return mStatusLayout;
    }
}
