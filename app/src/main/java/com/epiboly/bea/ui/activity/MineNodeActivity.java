package com.epiboly.bea.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.epiboly.bea.rich.R;
import com.epiboly.bea.aop.Log;
import com.epiboly.bea.app.AppActivity;
import com.epiboly.bea.ui.fragment.MyNodeFragment;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.hjq.base.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * @author vemao
 * @time 2023/2/4
 * @describe
 */
public class MineNodeActivity extends AppActivity {
    private CommonTabLayout mTabLayout;
    private ViewPager mViewPager;

    private FragmentPagerAdapter<Fragment> mPagerAdapter;


    @Log
    public static void start(Context context) {
        Intent intent = new Intent(context, MineNodeActivity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_mine_node;
    }

    @Override
    protected void initView() {
        mTabLayout = (CommonTabLayout) findViewById(R.id.tab_layout);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTabLayout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mViewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        mViewPager.setCurrentItem(0);
    }


    @Override
    protected void initData() {
        ArrayList<CustomTabEntity> tabs = new ArrayList<>();
        tabs.add(new Tab("有效节点"));
        tabs.add(new Tab("过期节点"));
        mTabLayout.setTabData(tabs);
        mPagerAdapter = new FragmentPagerAdapter<>(this);
        mPagerAdapter.addFragment(MyNodeFragment.newInstance(MyNodeFragment.VALID_NODE_TYPE));
        mPagerAdapter.addFragment(MyNodeFragment.newInstance(MyNodeFragment.INVALID_NODE_TYPE));
        mViewPager.setAdapter(mPagerAdapter);
    }

    static class Tab implements CustomTabEntity{

        private String tab;

        public Tab(String tab) {
            this.tab = tab;
        }

        @Override
        public String getTabTitle() {
            return tab;
        }

        @Override
        public int getTabSelectedIcon() {
            return 0;
        }

        @Override
        public int getTabUnselectedIcon() {
            return 0;
        }
    }
}
