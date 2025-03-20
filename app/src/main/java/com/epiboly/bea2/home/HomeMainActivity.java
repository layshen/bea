package com.epiboly.bea2.home;

import static com.epiboly.bea2.advertisement.AdCons.POS_ID_RewardVideo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.epiboly.bea2.advertisement.AdCons;
import com.epiboly.bea2.app.AppFragment;
import com.epiboly.bea2.cache.UserHelper;
import com.epiboly.bea2.http.model.User;
import com.epiboly.bea2.node.NodeListFragment;
import com.epiboly.bea2.other.AppConfig;
import com.epiboly.bea2.ui.adapter.ProductAdapter;
import com.epiboly.bea2.ui.dialog.UpdateDialog;
//import com.epiboly.bea2.ui.fragment.ProductListFragment;
import com.epiboly.bea2.util.CheckUpdateHelper;
import com.gyf.immersionbar.ImmersionBar;
import com.hjq.base.FragmentPagerAdapter;
import com.epiboly.bea2.R;
import com.epiboly.bea2.app.AppActivity;
import com.epiboly.bea2.manager.ActivityManager;
import com.epiboly.bea2.other.DoubleClickHelper;
import com.epiboly.bea2.ui.adapter.NavigationAdapter;
import com.epiboly.bea2.ui.fragment.DailyTaskFragment;
import com.epiboly.bea2.ui.fragment.HomeFragment;
import com.epiboly.bea2.ui.fragment.MeFragment;
//import com.kc.openset.ad.OSETRewardVideoCache;
import com.kc.openset.ad.reward.OSETRewardVideo;

/**
 * @author mao
 * @time 2022/11/27
 * @describe
 */
public final class HomeMainActivity extends AppActivity
        implements NavigationAdapter.OnNavigationListener {

    private static final String INTENT_KEY_IN_FRAGMENT_INDEX = "fragmentIndex";
    private static final String INTENT_KEY_IN_FRAGMENT_CLASS = "fragmentClass";

    private ViewPager mViewPager;
    private RecyclerView mNavigationView;

    private NavigationAdapter mNavigationAdapter;
    private FragmentPagerAdapter<AppFragment<?>> mPagerAdapter;

    public static void start(Context context) {
        start(context, DailyTaskFragment.class);
    }

    public static void start(Context context, Class<? extends AppFragment<?>> fragmentClass) {
        Intent intent = new Intent(context, HomeMainActivity.class);
        intent.putExtra(INTENT_KEY_IN_FRAGMENT_CLASS, DailyTaskFragment.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //这一步建议在首页进行初始化并开启缓存,减少第一次展示广告的时间。并且在首页onDestroy里面调用destroy()方法释放资源
        User user = UserHelper.getInstance().getUser();
        OSETRewardVideo.getInstance()
                .setContext(this)
                .setPosId(POS_ID_RewardVideo)
                .setUserId(user.getUid())
                .startLoad();
        //这一步建议在首页进行初始化并开启缓存,减少第一次展示广告的时间。并且在首页onDestroy里面调用destroy()方法释放资源
//        OSETRewardVideoCache.getInstance()
//                .setContext(this)
//                .setVerify(true)
//                .setServiceReward(true)
//                .setPosId(AdCons.POS_ID_RewardVideo)
//                .setUserId(UserHelper.getInstance().getUser().getUid())
//                .startLoad();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.home_main_activity;
    }

    @Override
    protected void initView() {
        mViewPager = findViewById(R.id.vp_home_pager);
        mNavigationView = findViewById(R.id.rv_home_navigation);

        mNavigationAdapter = new NavigationAdapter(this);
        mNavigationAdapter.addItem(new NavigationAdapter.MenuItem("任务",
                ContextCompat.getDrawable(this, R.drawable.home_home_selector)));
//        mNavigationAdapter.addItem(new NavigationAdapter.MenuItem("节点",
//                ContextCompat.getDrawable(this, R.drawable.home_found_selector)));
//        mNavigationAdapter.addItem(new NavigationAdapter.MenuItem("心愿墙",
//                ContextCompat.getDrawable(this, R.drawable.status_order_ic)));
        mNavigationAdapter.addItem(new NavigationAdapter.MenuItem(getString(R.string.home_nav_me),
                ContextCompat.getDrawable(this, R.drawable.home_me_selector)));
        mNavigationAdapter.setOnNavigationListener(this);
        mNavigationView.setAdapter(mNavigationAdapter);
    }

    @Override
    protected void initData() {
        mPagerAdapter = new FragmentPagerAdapter<>(this);
        mPagerAdapter.addFragment(DailyTaskFragment.newInstance());
//        mPagerAdapter.addFragment(ProductListFragment.newInstance());
//        mPagerAdapter.addFragment(EmptyFragment.newInstance());
        mPagerAdapter.addFragment(MeFragment.newInstance());
        mViewPager.setAdapter(mPagerAdapter);

        onNewIntent(getIntent());

        if (CheckUpdateHelper.getInstance().isNeedUpdate(AppConfig.getVersionCode(),AppConfig.getVersionName())) {
            new UpdateDialog.Builder(this)
                    .setVersionName(CheckUpdateHelper.getInstance().getVersionInfo().getVersionName())
                    .setForceUpdate(CheckUpdateHelper.getInstance().getVersionInfo().isFocusUpdate())
                    .setDownloadUrl(CheckUpdateHelper.getInstance().getVersionInfo().getUrl())
                    .setUpdateLog(CheckUpdateHelper.getInstance().getVersionInfo().getUpdateLog())
                    .show();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        switchFragment(mPagerAdapter.getFragmentIndex(getSerializable(INTENT_KEY_IN_FRAGMENT_CLASS)));
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // 保存当前 Fragment 索引位置
        outState.putInt(INTENT_KEY_IN_FRAGMENT_INDEX, mViewPager.getCurrentItem());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // 恢复当前 Fragment 索引位置
        switchFragment(savedInstanceState.getInt(INTENT_KEY_IN_FRAGMENT_INDEX));
    }

    private void switchFragment(int fragmentIndex) {
        if (fragmentIndex == -1) {
            return;
        }

        switch (fragmentIndex) {
            case 0:
            case 1:
            case 2:
            case 3:
                mViewPager.setCurrentItem(fragmentIndex);
                mNavigationAdapter.setSelectedPosition(fragmentIndex);
                break;
            default:
                break;
        }
    }

    /**
     * {@link NavigationAdapter.OnNavigationListener}
     */

    @Override
    public boolean onNavigationItemSelected(int position) {
        switch (position) {
            case 0:
            case 1:
            case 2:
            case 3:
                mViewPager.setCurrentItem(position);
                return true;
            default:
                return false;
        }
    }

    @NonNull
    @Override
    protected ImmersionBar createStatusBarConfig() {
        return super.createStatusBarConfig()
                // 指定导航栏背景颜色
                .navigationBarColor(R.color.white);
    }

    @Override
    public void onBackPressed() {
        if (!DoubleClickHelper.isOnDoubleClick()) {
            toast(R.string.home_exit_hint);
            return;
        }

        // 移动到上一个任务栈，避免侧滑引起的不良反应
        moveTaskToBack(false);
        postDelayed(() -> {
            // 进行内存优化，销毁掉所有的界面
            ActivityManager.getInstance().finishAllActivities();
            // 销毁进程（注意：调用此 API 可能导致当前 Activity onDestroy 方法无法正常回调）
            // System.exit(0);
        }, 300);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewPager.setAdapter(null);
        mNavigationView.setAdapter(null);
        mNavigationAdapter.setOnNavigationListener(null);
        //在OnDestroy中调用destroy
//        OSETRewardVideoCache.getInstance().destroy();
//        OSETRewardVideo.getInstance().destroy();
    }
}