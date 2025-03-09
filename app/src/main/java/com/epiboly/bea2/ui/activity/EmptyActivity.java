package com.epiboly.bea2.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.epiboly.bea2.app.AppActivity;
import com.epiboly.bea2.R;
import com.gyf.immersionbar.ImmersionBar;

/**
 * @author vemao
 * @time 2023/3/8
 * @describe
 */
public class EmptyActivity extends AppActivity {

    private FrameLayout mFragmentContainer;

    public static void start(Context context ,Class f){
        Intent intent = new Intent(context,EmptyActivity.class);
        intent.putExtra("fragment",f.getName());
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_empty;
    }

    @Override
    protected void initView() {
        mFragmentContainer = (FrameLayout) findViewById(R.id.fragment_container);
        try {
            Class<? extends Fragment> clz = (Class<? extends Fragment>) Class.forName(getString("fragment"));
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,clz,null).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initData() {

    }
}
