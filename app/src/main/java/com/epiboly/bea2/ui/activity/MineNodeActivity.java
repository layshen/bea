package com.epiboly.bea2.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.epiboly.bea2.R;
import com.epiboly.bea2.aop.Log;
import com.epiboly.bea2.app.AppActivity;
import com.epiboly.bea2.ui.fragment.MyNodeFragment;
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
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_content,new MyNodeFragment()).commitAllowingStateLoss();
    }


    @Override
    protected void initData() {

    }
}
