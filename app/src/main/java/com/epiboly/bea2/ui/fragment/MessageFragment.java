package com.epiboly.bea2.ui.fragment;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.epiboly.bea2.ui.activity.HomeActivity;
import com.epiboly.bea2.R;
import com.epiboly.bea2.aop.Permissions;
import com.epiboly.bea2.aop.SingleClick;
import com.epiboly.bea2.app.TitleBarFragment;
import com.epiboly.bea2.http.glide.GlideApp;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject
 *    time   : 2018/10/18
 *    desc   : 消息 Fragment
 */
public final class MessageFragment extends TitleBarFragment<HomeActivity> {

    private ImageView mImageView;

    public static MessageFragment newInstance() {
        return new MessageFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.message_fragment;
    }

    @Override
    protected void initView() {
        mImageView = findViewById(R.id.iv_message_image);
        setOnClickListener(R.id.btn_message_image1, R.id.btn_message_image2, R.id.btn_message_image3,
                R.id.btn_message_toast, R.id.btn_message_permission, R.id.btn_message_setting,
                R.id.btn_message_black, R.id.btn_message_white, R.id.btn_message_tab);
    }

    @Override
    protected void initData() {

    }

    @Override
    public boolean isStatusBarEnabled() {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled();
    }

    @SingleClick
    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.btn_message_image1) {

            mImageView.setVisibility(View.VISIBLE);
            GlideApp.with(this)
                    .load("https://www.baidu.com/img/bd_logo.png")
                    .into(mImageView);

        } else if (viewId == R.id.btn_message_image2) {

            mImageView.setVisibility(View.VISIBLE);
            GlideApp.with(this)
                    .load("https://www.baidu.com/img/bd_logo.png")
                    .circleCrop()
                    .into(mImageView);

        } else if (viewId == R.id.btn_message_image3) {

            mImageView.setVisibility(View.VISIBLE);
            GlideApp.with(this)
                    .load("https://www.baidu.com/img/bd_logo.png")
                    .transform(new RoundedCorners((int) getResources().getDimension(R.dimen.dp_20)))
                    .into(mImageView);

        } else if (viewId == R.id.btn_message_toast) {

            toast("我是吐司");

        } else if (viewId == R.id.btn_message_permission) {

            requestPermission();

        } else if (viewId == R.id.btn_message_setting) {

            XXPermissions.startPermissionActivity(this);

        } else if (viewId == R.id.btn_message_black) {

            getAttachActivity()
                    .getStatusBarConfig()
                    .statusBarDarkFont(true)
                    .init();

        } else if (viewId == R.id.btn_message_white) {

            getAttachActivity()
                    .getStatusBarConfig()
                    .statusBarDarkFont(false)
                    .init();

        } else if (viewId == R.id.btn_message_tab) {

            HomeActivity.start(getActivity(), HomeFragment.class);
        }
    }

    @Permissions(Permission.CAMERA)
    private void requestPermission() {
        toast("获取摄像头权限成功");
    }
}