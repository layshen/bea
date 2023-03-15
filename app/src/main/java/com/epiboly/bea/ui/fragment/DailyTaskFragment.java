package com.epiboly.bea.ui.fragment;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.widget.ImageView;

import androidx.constraintlayout.widget.Group;

import com.epiboly.bea.advertisement.ui.RewardProActivity;
import com.epiboly.bea.R;
import com.epiboly.bea.action.StatusAction;
import com.epiboly.bea.app.AppFragment;
import com.epiboly.bea.app.TitleBarFragment;
import com.epiboly.bea.cache.UserHelper;
import com.epiboly.bea.home.HomeMainActivity;
import com.epiboly.bea.http.api.QueryUserFinishStatusApi;
import com.epiboly.bea.http.model.HttpData;
import com.epiboly.bea.http.model.IntegralServer;
import com.epiboly.bea.widget.StatusLayout;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;

/**
 * @author mao
 * @time 2022/12/4
 * @describe 每日任务
 */
public class DailyTaskFragment extends TitleBarFragment<HomeMainActivity> implements StatusAction {

    private ImageView iv_task_center;
    private Group group_top;
    private Group group_top_right;
    private Group group_bottom;
    private Group group_bottom_right;
    private Group group_bottom_left;
    private Group group_top_left;
    private StatusLayout status_layout;
    private boolean isAnimatorExecuting;
    private ImageView mIvTopTaskLine;
    private ImageView mIvTopRightTaskLine;
    private ImageView mIvBottomRightTaskLine;
    private ImageView mIvBottomLeftTaskLine;
    private ImageView mIvBottomTaskLine;
    private ImageView mIvTopLeftTaskLine;
    private ImageView mIvTopTask;
    private ImageView mIvTopRightTask;
    private ImageView mIvBottomRightTask;
    private ImageView mIvBottomTask;
    private ImageView mIvBottomLeftTask;
    private ImageView mIvTopLeftTask;
    private QueryUserFinishStatusApi.Bean bean;

    public static AppFragment<?> newInstance() {
        return new DailyTaskFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_daily_task;
    }

    @Override
    protected void initView() {
        status_layout = (StatusLayout) findViewById(R.id.status_layout);
        iv_task_center = (ImageView) findViewById(R.id.iv_task_center);
        group_top = (Group) findViewById(R.id.group_top);
        group_top.setReferencedIds(new int[]{R.id.iv_top_task, R.id.iv_top_task_line});
        group_top_right = (Group) findViewById(R.id.group_top_right);
        group_top_right.setReferencedIds(new int[]{R.id.iv_top_right_task, R.id.iv_top_right_task_line});
        group_bottom_right = (Group) findViewById(R.id.group_bottom_right);
        group_bottom_right.setReferencedIds(new int[]{R.id.iv_bottom_right_task, R.id.iv_bottom_right_task_line});
        group_bottom = (Group) findViewById(R.id.group_bottom);
        group_bottom.setReferencedIds(new int[]{R.id.iv_bottom_task, R.id.iv_bottom_task_line});
        group_bottom_left = (Group) findViewById(R.id.group_bottom_left);
        group_bottom_left.setReferencedIds(new int[]{R.id.iv_bottom_left_task, R.id.iv_bottom_left_task_line});
        group_top_left = (Group) findViewById(R.id.group_top_left);
        group_top_left.setReferencedIds(new int[]{R.id.iv_top_left_task, R.id.iv_top_left_task_line});

        mIvTopTaskLine = (ImageView) findViewById(R.id.iv_top_task_line);
        mIvTopRightTaskLine = (ImageView) findViewById(R.id.iv_top_right_task_line);
        mIvBottomRightTaskLine = (ImageView) findViewById(R.id.iv_bottom_right_task_line);
        mIvBottomLeftTaskLine = (ImageView) findViewById(R.id.iv_bottom_left_task_line);
        mIvBottomTaskLine = (ImageView) findViewById(R.id.iv_bottom_task_line);
        mIvTopLeftTaskLine = (ImageView) findViewById(R.id.iv_top_left_task_line);
        mIvTopTask = (ImageView) findViewById(R.id.iv_top_task);
        mIvTopRightTask = (ImageView) findViewById(R.id.iv_top_right_task);
        mIvBottomRightTask = (ImageView) findViewById(R.id.iv_bottom_right_task);
        mIvBottomTask = (ImageView) findViewById(R.id.iv_bottom_task);
        mIvBottomLeftTask = (ImageView) findViewById(R.id.iv_bottom_left_task);
        mIvTopLeftTask = (ImageView) findViewById(R.id.iv_top_left_task);

        setOnClickListener(mIvTopTask,mIvTopRightTask,mIvBottomRightTask,mIvBottomTask,mIvBottomLeftTask,mIvTopLeftTask);

//        iv_task_center.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onFragmentResume(boolean first) {
        if (first) {
            showLoading();
        }
        queryUserFinishStatusApi();
    }

    private void queryUserFinishStatusApi() {
        EasyHttp.post(this)
                .server(new IntegralServer())
                .api(new QueryUserFinishStatusApi().setToken(UserHelper.getInstance().getToken()))
                .request(new HttpCallback<HttpData<QueryUserFinishStatusApi.Bean>>(this) {
                    @Override
                    public void onSucceed(HttpData<QueryUserFinishStatusApi.Bean> result) {
                        if (result != null && result.isRequestSucceed()) {
                            if (result.getData() == null) {
                                showEmpty(R.string.status_layout_no_task);
                            } else {
                                setupContent(result.getData());
                                showComplete();
                            }
                        } else {
                            String desc = "请求出错，请重试";
                            if (result != null){
                                desc = result.getDesc();
                            }
                            showError(desc,new StatusLayout.OnRetryListener() {
                                @Override
                                public void onRetry(StatusLayout layout) {
                                    showLoading();
                                    queryUserFinishStatusApi();
                                }
                            });
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        showError(new StatusLayout.OnRetryListener() {
                            @Override
                            public void onRetry(StatusLayout layout) {
                                showLoading();
                                queryUserFinishStatusApi();
                            }
                        });
                    }
                });
    }

    @Override
    public void onClick(View view) {
        if (view == mIvTopTask){
           doItemClick(mIvTopTask,0);
        }else if (view == mIvTopRightTask){
            doItemClick(mIvTopRightTask,1);
        }else if (view == mIvBottomRightTask){
            doItemClick(mIvBottomRightTask,2);
        }else if (view == mIvBottomTask){
            doItemClick(mIvBottomTask,3);
        }else if (view == mIvBottomLeftTask){
            doItemClick(mIvBottomLeftTask,4);
        }else if (view == mIvTopLeftTask){
            doItemClick(mIvTopLeftTask,5);
        }
    }

    private void doItemClick(ImageView view, int i) {
        if (isAnimatorExecuting) {
            return;
        }
        if (bean == null){
            return;
        }
        if (bean.isFinishTask(i)){
            toast("当前任务已完成");
            return;
        }
        final AnimatorSet animatorSet = new AnimatorSet();
        view.setPivotX(view.getWidth() / 2);
        view.setPivotY(view.getHeight() / 2);
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(view, "scaleX", 1, 1.1f, 1).setDuration(1000),
                ObjectAnimator.ofFloat(view, "scaleY", 1, 1.1f, 1).setDuration(1000));
        isAnimatorExecuting = true;
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                RewardProActivity.Companion.action(getActivity(),i);
                isAnimatorExecuting = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.start();
    }

    private void setupContent(QueryUserFinishStatusApi.Bean bean) {
        this.bean = bean;
        int finishedCount = bean.getFinishedCount();
        int totalCount = bean.getTotalCount();

        //default
        mIvTopTaskLine.setImageResource(R.drawable.task_top_off);
        mIvTopRightTaskLine.setImageResource(R.drawable.task_topright_off);
        mIvBottomRightTaskLine.setImageResource(R.drawable.task_downright_off);
        mIvBottomTaskLine.setImageResource(R.drawable.task_down_off);
        mIvBottomLeftTaskLine.setImageResource(R.drawable.task_downleft_off);
        mIvTopLeftTaskLine.setImageResource(R.drawable.task_topleft_off);
        mIvTopTask.setImageResource(R.drawable.task_sub_off);
        mIvTopRightTask.setImageResource(R.drawable.task_sub_off);
        mIvBottomRightTask.setImageResource(R.drawable.task_sub_off);
        mIvBottomTask.setImageResource(R.drawable.task_sub_off);
        mIvBottomLeftTask.setImageResource(R.drawable.task_sub_off);
        mIvTopLeftTask.setImageResource(R.drawable.task_sub_off);

        if (bean.isFinishTask(0)){
            mIvTopTaskLine.setImageResource(R.drawable.task_top);
            mIvTopTask.setImageResource(R.drawable.task_sub);
        }
        if (bean.isFinishTask(1)){
            mIvTopRightTaskLine.setImageResource(R.drawable.task_topright);
            mIvTopRightTask.setImageResource(R.drawable.task_sub);
        }
        if (bean.isFinishTask(2)){
            mIvBottomRightTaskLine.setImageResource(R.drawable.task_downright);
            mIvBottomRightTask.setImageResource(R.drawable.task_sub);
        }
        if (bean.isFinishTask(3)){
            mIvBottomTaskLine.setImageResource(R.drawable.task_down);
            mIvBottomTask.setImageResource(R.drawable.task_sub);
        }
        if (bean.isFinishTask(4)){
            mIvBottomLeftTaskLine.setImageResource(R.drawable.task_downleft);
            mIvBottomLeftTask.setImageResource(R.drawable.task_sub);
        }
        if (bean.isFinishTask(5)){
            mIvTopLeftTaskLine.setImageResource(R.drawable.task_topleft);
            mIvTopLeftTask.setImageResource(R.drawable.task_sub);
        }

        if (finishedCount == totalCount){
            iv_task_center.setImageResource(R.drawable.task_main);
        }else {
            iv_task_center.setImageResource(R.drawable.task_main_off);
        }


//        if (finishedCount == 0) {
//
//        } else if (finishedCount == 1) {
//            mIvTopTaskLine.setImageResource(R.drawable.task_top);
//            mIvTopRightTaskLine.setImageResource(R.drawable.task_topright_off);
//            mIvBottomRightTaskLine.setImageResource(R.drawable.task_downright_off);
//            mIvBottomTaskLine.setImageResource(R.drawable.task_down_off);
//            mIvBottomLeftTaskLine.setImageResource(R.drawable.task_downleft_off);
//            mIvTopLeftTaskLine.setImageResource(R.drawable.task_topleft_off);
//            mIvTopTask.setImageResource(R.drawable.task_sub);
//            mIvTopRightTask.setImageResource(R.drawable.task_sub_off);
//            mIvBottomRightTask.setImageResource(R.drawable.task_sub_off);
//            mIvBottomTask.setImageResource(R.drawable.task_sub_off);
//            mIvBottomLeftTask.setImageResource(R.drawable.task_sub_off);
//            mIvTopLeftTask.setImageResource(R.drawable.task_sub_off);
//        } else if (finishedCount == 2) {
//            mIvTopTaskLine.setImageResource(R.drawable.task_top);
//            mIvTopRightTaskLine.setImageResource(R.drawable.task_topright);
//            mIvBottomRightTaskLine.setImageResource(R.drawable.task_downright_off);
//            mIvBottomTaskLine.setImageResource(R.drawable.task_down_off);
//            mIvBottomLeftTaskLine.setImageResource(R.drawable.task_downleft_off);
//            mIvTopLeftTaskLine.setImageResource(R.drawable.task_topleft_off);
//            mIvTopTask.setImageResource(R.drawable.task_sub);
//            mIvTopRightTask.setImageResource(R.drawable.task_sub);
//            mIvBottomRightTask.setImageResource(R.drawable.task_sub_off);
//            mIvBottomTask.setImageResource(R.drawable.task_sub_off);
//            mIvBottomLeftTask.setImageResource(R.drawable.task_sub_off);
//            mIvTopLeftTask.setImageResource(R.drawable.task_sub_off);
//        } else if (finishedCount == 3) {
//            mIvTopTaskLine.setImageResource(R.drawable.task_top);
//            mIvTopRightTaskLine.setImageResource(R.drawable.task_topright);
//            mIvBottomRightTaskLine.setImageResource(R.drawable.task_downright);
//            mIvBottomTaskLine.setImageResource(R.drawable.task_down_off);
//            mIvBottomLeftTaskLine.setImageResource(R.drawable.task_downleft_off);
//            mIvTopLeftTaskLine.setImageResource(R.drawable.task_topleft_off);
//            mIvTopTask.setImageResource(R.drawable.task_sub);
//            mIvTopRightTask.setImageResource(R.drawable.task_sub);
//            mIvBottomRightTask.setImageResource(R.drawable.task_sub);
//            mIvBottomTask.setImageResource(R.drawable.task_sub_off);
//            mIvBottomLeftTask.setImageResource(R.drawable.task_sub_off);
//            mIvTopLeftTask.setImageResource(R.drawable.task_sub_off);
//        } else if (finishedCount == 4) {
//            mIvTopTaskLine.setImageResource(R.drawable.task_top_off);
//            mIvTopRightTaskLine.setImageResource(R.drawable.task_topright);
//            mIvBottomRightTaskLine.setImageResource(R.drawable.task_downright);
//            mIvBottomTaskLine.setImageResource(R.drawable.task_down);
//            mIvBottomLeftTaskLine.setImageResource(R.drawable.task_downleft_off);
//            mIvTopLeftTaskLine.setImageResource(R.drawable.task_topleft_off);
//            mIvTopTask.setImageResource(R.drawable.task_sub);
//            mIvTopRightTask.setImageResource(R.drawable.task_sub);
//            mIvBottomRightTask.setImageResource(R.drawable.task_sub);
//            mIvBottomTask.setImageResource(R.drawable.task_sub);
//            mIvBottomLeftTask.setImageResource(R.drawable.task_sub_off);
//            mIvTopLeftTask.setImageResource(R.drawable.task_sub_off);
//        } else if (finishedCount == 5) {
//            mIvTopTaskLine.setImageResource(R.drawable.task_top);
//            mIvTopRightTaskLine.setImageResource(R.drawable.task_topright);
//            mIvBottomRightTaskLine.setImageResource(R.drawable.task_downright);
//            mIvBottomTaskLine.setImageResource(R.drawable.task_down);
//            mIvBottomLeftTaskLine.setImageResource(R.drawable.task_downleft);
//            mIvTopLeftTaskLine.setImageResource(R.drawable.task_topleft_off);
//            mIvTopTask.setImageResource(R.drawable.task_sub);
//            mIvTopRightTask.setImageResource(R.drawable.task_sub);
//            mIvBottomRightTask.setImageResource(R.drawable.task_sub);
//            mIvBottomTask.setImageResource(R.drawable.task_sub);
//            mIvBottomLeftTask.setImageResource(R.drawable.task_sub);
//            mIvTopLeftTask.setImageResource(R.drawable.task_sub_off);
//        } else if (finishedCount >= 6) {
//            mIvTopTaskLine.setImageResource(R.drawable.task_top);
//            mIvTopRightTaskLine.setImageResource(R.drawable.task_topright);
//            mIvBottomRightTaskLine.setImageResource(R.drawable.task_downright);
//            mIvBottomTaskLine.setImageResource(R.drawable.task_down);
//            mIvBottomLeftTaskLine.setImageResource(R.drawable.task_downleft);
//            mIvTopLeftTaskLine.setImageResource(R.drawable.task_topleft);
//            mIvTopTask.setImageResource(R.drawable.task_sub);
//            mIvTopRightTask.setImageResource(R.drawable.task_sub);
//            mIvBottomRightTask.setImageResource(R.drawable.task_sub);
//            mIvBottomTask.setImageResource(R.drawable.task_sub);
//            mIvBottomLeftTask.setImageResource(R.drawable.task_sub);
//            mIvTopLeftTask.setImageResource(R.drawable.task_sub);
//        }
    }

    @Override
    protected void onActivityResume() {
        queryUserFinishStatusApi();
    }

    @Override
    public boolean isStatusBarEnabled() {
        return true;
    }

    @Override
    public StatusLayout getStatusLayout() {
        return status_layout;
    }
}
