package com.epiboly.bea2.widget;

import com.epiboly.bea2.R;
import com.epiboly.bea2.app.AppFragment;
import com.epiboly.bea2.app.TitleBarFragment;

/**
 * @author mao
 * @time 2022/11/27
 * @describe
 */
public class EmptyFragment extends TitleBarFragment {
    public static AppFragment<?> newInstance() {
        return new EmptyFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_empty;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }
}
