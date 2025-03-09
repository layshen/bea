//package com.epiboly.bea2.ui.fragment;
//
//import android.animation.Animator;
//import android.animation.AnimatorSet;
//import android.animation.ObjectAnimator;
//import android.content.DialogInterface;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.ImageView;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AlertDialog;
//import androidx.recyclerview.widget.GridLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.epiboly.bea2.R;
//import com.epiboly.bea2.action.StatusAction;
//import com.epiboly.bea2.advertisement.ui.AdRewardActivity;
//import com.epiboly.bea2.app.AppFragment;
//import com.epiboly.bea2.app.TitleBarFragment;
//import com.epiboly.bea2.cache.UserHelper;
//import com.epiboly.bea2.http.api.QueryProductApi;
//import com.epiboly.bea2.http.api.ExchangeProductApi;
//import com.epiboly.bea2.http.model.HttpData;
//import com.epiboly.bea2.http.model.HttpList2Data;
//import com.epiboly.bea2.http.model.IntegralServer;
//import com.epiboly.bea2.http.model.Product;
//import com.epiboly.bea2.http.model.User;
//import com.epiboly.bea2.ui.activity.ProductListActivity;
//import com.epiboly.bea2.ui.adapter.ProductAdapter;
//import com.epiboly.bea2.widget.StatusLayout;
//import com.hjq.base.BaseAdapter;
//import com.hjq.http.EasyHttp;
//import com.hjq.http.listener.HttpCallback;
//import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
//import com.scwang.smart.refresh.layout.SmartRefreshLayout;
//import com.scwang.smart.refresh.layout.api.RefreshLayout;
//import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
//import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
//
//import java.math.BigDecimal;
//import java.util.Arrays;
//import java.util.List;
//
///**
// * @author mao
// * @time 2022/12/4
// * @describe 每日任务
// */
//public class ProductListFragment extends TitleBarFragment<ProductListActivity> implements StatusAction {
//
//    private RecyclerView mRecyclerView;
//    private ProductAdapter productAdapter;
//    private StatusLayout mStatusLayout;
//    private SmartRefreshLayout mSmartRefresh;
//    private QMUIRoundButton btnExchnange;
//    private int pageNum = 1;
//
//    public static AppFragment<?> newInstance() {
//        ProductListFragment fragment = new ProductListFragment();
//        Bundle bundle = new Bundle();
//        fragment.setArguments(bundle);
//        return fragment;
//    }
//
//    @Override
//    protected int getLayoutId() {
//        return R.layout.fragment_product_list;
//    }
//
//    @Override
//    protected void initView() {
//        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
//        productAdapter = new ProductAdapter(getContext());
//        btnExchnange = findViewById(R.id.btn_exchange);
//        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
//        productAdapter.setOnChildClickListener(R.id.btn_exchange, new BaseAdapter.OnChildClickListener() {
//            @Override
//            public void onChildClick(RecyclerView recyclerView, View childView, int position) {
//                Product item = productAdapter.getItem(position);
//                showExchangeAlert(item);
//            }
//        });
//        mRecyclerView.setAdapter(productAdapter);
//        mStatusLayout = (StatusLayout) findViewById(R.id.status_layout);
//        mSmartRefresh = (SmartRefreshLayout) findViewById(R.id.smart_refresh);
//        mSmartRefresh.setEnableLoadMore(true);
//        mSmartRefresh.setEnableAutoLoadMore(true);
//        mSmartRefresh.setOnRefreshListener(new OnRefreshListener() {
//            @Override
//            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
//                refresh();
//            }
//        });
//        mSmartRefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
//                if (productAdapter.getCount() > 0){
//                    Product item = productAdapter.getItem(productAdapter.getCount() - 1);
//                    getMoreData(item.getId());
//                }
//            }
//        });
//
//    }
//
//    private void showExchangeAlert(Product product) {
//        new AlertDialog.Builder(getContext())
//                .setTitle("兑换提示")
//                .setMessage("请确认是否兑换商品：" + product.getName())
//                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        exchangeProduct(product.getId());
//                    }
//                })
//                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                })
//                .show();
//    }
//
//    @Override
//    protected void initData() {
//
//    }
//
//    @Override
//    protected void onFragmentResume(boolean first) {
//        if (first) {
//            showLoading();
//        }
//        refresh();
//    }
//
//    private void refresh() {
//        pageNum = 1;
//        EasyHttp.get(this)
//                .server(new IntegralServer())
//                .api(new QueryProductApi()
//                        .setToken(UserHelper.getInstance().getToken()).setPageNum(pageNum))
//                .request(new HttpCallback<HttpList2Data<Product>>(this) {
//                    @Override
//                    public void onSucceed(HttpList2Data<Product> result) {
//                        if (result != null && result.isRequestSucceed()) {
//                            HttpList2Data.ListBean<Product> list = result.getData();
//                            List<Product> products = list.getList();
//                            if (products == null || products.size() == 0)  {
//                                showEmpty(R.string.status_layout_no_task);
//                            }else {
//                                setupContent(products);
//                                showComplete();
//                            }
//                        } else {
//                            String desc = "请求出错，请重试";
//                            if (result != null){
//                                desc = result.getDesc();
//                            }
//                            showError(desc,new StatusLayout.OnRetryListener() {
//                                @Override
//                                public void onRetry(StatusLayout layout) {
//                                    showLoading();
//                                    refresh();
//                                }
//                            });
//                        }
//                        mSmartRefresh.finishRefresh(true);
//
////                        if (result != null && result.isRequestSucceed()) {
////                            if (result.getData() == null) {
////                                showEmpty(R.string.status_layout_no_task);
////                            } else {
////                                setupContent(result.getData());
////                                showComplete();
////                            }
////                        } else {
////                            String desc = "请求出错，请重试";
////                            if (result != null){
////                                desc = result.getDesc();
////                            }
////                            showError(desc,new StatusLayout.OnRetryListener() {
////                                @Override
////                                public void onRetry(StatusLayout layout) {
////                                    showLoading();
////                                    queryUserFinishStatusApi();
////                                }
////                            });
////                        }
//                    }
//
//                    @Override
//                    public void onFail(Exception e) {
//                        Product product = new Product("https://api.zzzmh.cn/v2/bz/v3/getUrl/37aa5dc36d88444786b6b543845963ba20","测试商品", BigDecimal.TEN,13);
//                        setupContent(Arrays.asList(product,product,product));
//
//                        showComplete();
////                        showError(new StatusLayout.OnRetryListener() {
////                            @Override
////                            public void onRetry(StatusLayout layout) {
////                                showLoading();
////                                queryUserFinishStatusApi();
////                            }
////                        });
//                    }
//                });
//    }
//
//    private void getMoreData(long id) {
//        pageNum++;
//        EasyHttp.get(this)
//                .server(new IntegralServer())
//                .api(new QueryProductApi()
//                        .setToken(UserHelper.getInstance().getToken()).setPageNum(pageNum))
//                .request(new HttpCallback<HttpList2Data<Product>>(this) {
//
//                    @Override
//                    public void onSucceed(HttpList2Data<Product> result) {
//                        if (result == null || result.getData() == null){
//                            mSmartRefresh.finishLoadMore(false);
//                            mSmartRefresh.setNoMoreData(true);
//                            return;
//                        }
//                        if (result.isRequestSucceed()) {
//                            mSmartRefresh.finishLoadMore(true);
//                            if (result.getData() == null || result.getData().getList().size() == 0){
//                                mSmartRefresh.setNoMoreData(true);
//                                return;
//                            }
//                            productAdapter.addData(result.getData().getList());
//                        }else {
//                            mSmartRefresh.finishLoadMore(false);
//                        }
//                    }
//
//                    @Override
//                    public void onFail(Exception e) {
//                        showError(null);
//                    }
//                });
//    }
//
////    @Override
////    public void onClick(View view) {
////        if (view == btnExchnange){
////           doItemClick(btnExchnange,0);
////        }
////    }
//
//    private void exchangeProduct(Integer productId) {
//        User user = UserHelper.getInstance().getUser();
//        EasyHttp.get(this)
//                .server(new IntegralServer())
//                .api(new ExchangeProductApi()
//                        .setProductId(productId)
//                        .setUid(UserHelper.getInstance().getUser().getUid()))
//                .request(new HttpCallback<HttpData>(this) {
//
//                    @Override
//                    public void onSucceed(HttpData result) {
//                        if (result != null && result.isRequestSucceed()){
//                            toast("兑换成功");
//                        } else {
//                            toast("服务异常，请稍后再试");
//                        }
//                    }
//
//                    @Override
//                    public void onFail(Exception e) {
//                        showError(null);
//                    }
//                });
//    }
//
//    private void doItemClick(ImageView view, int i) {
////        if (isAnimatorExecuting) {
////            return;
////        }
////        if (bean.isFinishTask(i)){
////            toast("当前任务已完成");
////            return;
////        }
//        final AnimatorSet animatorSet = new AnimatorSet();
//        view.setPivotX(view.getWidth() / 2);
//        view.setPivotY(view.getHeight() / 2);
//        animatorSet.playTogether(
//                ObjectAnimator.ofFloat(view, "scaleX", 1, 1.1f, 1).setDuration(1000),
//                ObjectAnimator.ofFloat(view, "scaleY", 1, 1.1f, 1).setDuration(1000));
////        isAnimatorExecuting = true;
//        animatorSet.addListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                AdRewardActivity.start(getActivity(),i+"");
////                isAnimatorExecuting = false;
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animation) {
//
//            }
//        });
//        animatorSet.start();
//    }
//
//    private void setupContent(List<Product> bean) {
//        productAdapter.setData(bean);
//
//    }
//
//    @Override
//    protected void onActivityResume() {
////        queryUserFinishStatusApi();
//    }
//
//    @Override
//    public boolean isStatusBarEnabled() {
//        return true;
//    }
//
//    @Override
//    public StatusLayout getStatusLayout() {
//        return mStatusLayout;
//    }
//}
