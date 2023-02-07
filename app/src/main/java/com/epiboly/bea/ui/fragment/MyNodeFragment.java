package com.epiboly.bea.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.epiboly.bea.rich.R;
import com.epiboly.bea.action.StatusAction;
import com.epiboly.bea.app.AppAdapter;
import com.epiboly.bea.cache.UserHelper;
import com.epiboly.bea.http.api.UserBuyNodeInfoApi;
import com.epiboly.bea.http.glide.GlideApp;
import com.epiboly.bea.http.model.HttpData;
import com.epiboly.bea.http.model.MyNodeBean;
import com.epiboly.bea.http.model.NodeServer;
import com.epiboly.bea.ui.activity.MineNodeActivity;
import com.epiboly.bea.widget.StatusLayout;
import com.hjq.base.BaseFragment;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;

import java.util.ArrayList;

/**
 * @author vemao
 * @time 2023/2/4
 * @describe 我的节点
 */
public class MyNodeFragment extends BaseFragment<MineNodeActivity> implements StatusAction {

    public static final int VALID_NODE_TYPE = 0;
    public static final int INVALID_NODE_TYPE = 1;

    private RecyclerView mRecyclerView;
    private int mType;
    private MyNodeAdapter mMyNodeAdapter;
    private StatusLayout mStatusLayout;

    public static MyNodeFragment newInstance(int type) {
        MyNodeFragment fragment = new MyNodeFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
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
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayout.VERTICAL));
        mStatusLayout = (StatusLayout) findViewById(R.id.status_layout);
    }

    @Override
    protected void initData() {
        mType = getInt("type");
        showLoading();
        EasyHttp.post(this)
                .server(new NodeServer())
                .api(new UserBuyNodeInfoApi()
                        .setToken(UserHelper.getInstance().getToken())
                        .setUid(UserHelper.getInstance().getUser().getUid()))
                .request(new HttpCallback<HttpData<UserBuyNodeInfoApi.Bean>>(null) {

                    @Override
                    public void onSucceed(HttpData<UserBuyNodeInfoApi.Bean> result) {
                        if (result != null && result.isRequestSucceed()) {
                            UserBuyNodeInfoApi.Bean data = result.getData();
                            if (data == null){
                                showError(null);
                                return;
                            }
                            if (mType == VALID_NODE_TYPE) {
                                ArrayList<MyNodeBean> list = new ArrayList();
                                if (data.getValidTNum() > 0){
                                    list.add(new MyNodeBean(R.drawable.node_png_01, "T级节点", data.getValidTNum()));
                                }
                                if (data.getValidANum() > 0){
                                    list.add(new MyNodeBean(R.drawable.node_png_02, "A级节点", data.getValidANum()));
                                }
                                if (data.getValidBNum() > 0){
                                    list.add(new MyNodeBean(R.drawable.node_png_03, "B级节点", data.getValidBNum()));
                                }
                                if (data.getValidCNum() > 0){
                                    list.add(new MyNodeBean(R.drawable.node_png_04, "C级节点", data.getValidCNum()));
                                }
                                if (data.getValidDNum() > 0){
                                    list.add(new MyNodeBean(R.drawable.node_png_05, "D级节点", data.getValidDNum()));
                                }
                                if (data.getValidENum() > 0){
                                    list.add(new MyNodeBean(R.drawable.node_png_06, "E级节点", data.getValidENum()));
                                }
                                if (data.getValidFNum() > 0){
                                    list.add(new MyNodeBean(R.drawable.node_png_01, "F级节点", data.getValidFNum()));
                                }
                                if (list.size() == 0){
                                    showEmpty();
                                }else {
                                    mMyNodeAdapter.setData(list);
                                    showComplete();
                                }
                            } else {
                                ArrayList<MyNodeBean> list = new ArrayList();
                                if (data.getInvalidTNum() > 0){
                                    list.add(new MyNodeBean(R.drawable.node_png_01, "T级节点", data.getInvalidTNum()));
                                }
                                if (data.getInvalidANum() > 0){
                                    list.add(new MyNodeBean(R.drawable.node_png_02, "A级节点", data.getInvalidANum()));
                                }
                                if (data.getInvalidBNum() > 0){
                                    list.add(new MyNodeBean(R.drawable.node_png_03, "B级节点", data.getInvalidBNum()));
                                }
                                if (data.getInvalidCNum() > 0){
                                    list.add(new MyNodeBean(R.drawable.node_png_04, "C级节点", data.getInvalidCNum()));
                                }
                                if (data.getInvalidDNum() > 0){
                                    list.add(new MyNodeBean(R.drawable.node_png_05, "D级节点", data.getInvalidDNum()));
                                }
                                if (data.getInvalidENum() > 0){
                                    list.add(new MyNodeBean(R.drawable.node_png_06, "E级节点", data.getInvalidENum()));
                                }
                                if (data.getInvalidFNum() > 0){
                                    list.add(new MyNodeBean(R.drawable.node_png_01, "F级节点", data.getInvalidFNum()));
                                }
                                if (list.size() == 0){
                                    showEmpty();
                                }else {
                                    mMyNodeAdapter.setData(list);
                                    showComplete();
                                }
                            }
                        } else {
                            showError(null);
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

            private ViewHolder() {
                super(R.layout.node_my_item);
                initView();
            }

            @Override
            public void onBindView(int position) {
                MyNodeBean info = getItem(position);
                GlideApp.with(getContext())
                        .asBitmap()
                        .load(info.getDrawable())
                        .into(mIvImgTree);

                mTvName.setText(info.getName() + "");
                mTvNum.setText("数量:"+info.getNum() + "");

            }

            private void initView() {
                mIvImgTree = (ImageView) findViewById(R.id.iv_img_tree);
                mTvName = (TextView) findViewById(R.id.tv_name);
                mTvNum = (TextView) findViewById(R.id.tv_num);
            }
        }
    }
}
