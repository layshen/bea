package com.epiboly.bea2.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.epiboly.bea2.R;
import com.epiboly.bea2.aop.Log;
import com.epiboly.bea2.app.AppActivity;
import com.epiboly.bea2.cache.UserHelper;
import com.epiboly.bea2.http.api.UpdateImageApi;
import com.epiboly.bea2.http.api.UploadImageApi;
import com.epiboly.bea2.http.model.HttpData;
import com.epiboly.bea2.ui.adapter.ImagePreviewAdapter;
import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;
import com.hjq.base.BaseAdapter;
import com.hjq.base.RecyclerPagerAdapter;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.hjq.http.model.FileContentResolver;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

/**
 *    desc   : 查看头像大图
 */
public final class HeadPicturePreviewActivity extends AppActivity
        implements ViewPager.OnPageChangeListener,
        BaseAdapter.OnItemClickListener {

    private static final String INTENT_KEY_IN_IMAGE_LIST = "imageList";
    private static final String INTENT_KEY_IN_IMAGE_INDEX = "imageIndex";

    public static void start(Context context, String url) {
        ArrayList<String> images = new ArrayList<>(1);
        images.add(url);
        start(context, images);
    }

    public static void start(Context context, List<String> urls) {
        start(context, urls, 0);
    }

    @Log
    public static void start(Context context, List<String> urls, int index) {
        if (urls == null || urls.isEmpty()) {
            return;
        }
        Intent intent = new Intent(context, HeadPicturePreviewActivity.class);
        if (urls.size() > 2000) {
            // 请注意：如果传输的数据量过大，会抛出此异常，并且这种异常是不能被捕获的
            // 所以当图片数量过多的时候，我们应当只显示一张，这种一般是手机图片过多导致的
            // 经过测试，传入 3121 张图片集合的时候会抛出此异常，所以保险值应当是 2000
            // android.os.TransactionTooLargeException: data parcel size 521984 bytes
            urls = Collections.singletonList(urls.get(index));
        }

        if (urls instanceof ArrayList) {
            intent.putExtra(INTENT_KEY_IN_IMAGE_LIST, (ArrayList<String>) urls);
        } else {
            intent.putExtra(INTENT_KEY_IN_IMAGE_LIST, new ArrayList<>(urls));
        }
        intent.putExtra(INTENT_KEY_IN_IMAGE_INDEX, index);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    private ViewPager mViewPager;
    private ImagePreviewAdapter mAdapter;

    /** 圆圈指示器 */
    private CircleIndicator mCircleIndicatorView;
    /** 文本指示器 */
    private TextView mTextIndicatorView;

    @Override
    protected int getLayoutId() {
        return R.layout.head_image_preview_activity;
    }

    @Override
    protected void initView() {
        mViewPager = findViewById(R.id.vp_image_preview_pager);
        mCircleIndicatorView = findViewById(R.id.ci_image_preview_indicator);
        mTextIndicatorView = findViewById(R.id.tv_image_preview_indicator);
        setOnClickListener(R.id.fl_update_image);
    }

    @Override
    protected void initData() {
        ArrayList<String> images = getStringArrayList(INTENT_KEY_IN_IMAGE_LIST);
        if (images == null || images.isEmpty()) {
            finish();
            return;
        }
        mAdapter = new ImagePreviewAdapter(this);
        mAdapter.setData(images);
        mAdapter.setOnItemClickListener(this);
        mViewPager.setAdapter(new RecyclerPagerAdapter(mAdapter));
        if (images.size() != 1) {
            if (images.size() < 10) {
                // 如果是 10 张以内的图片，那么就显示圆圈指示器
                mCircleIndicatorView.setVisibility(View.GONE);
                mCircleIndicatorView.setViewPager(mViewPager);
            } else {
                // 如果超过 10 张图片，那么就显示文字指示器
                mTextIndicatorView.setVisibility(View.GONE);
                mViewPager.addOnPageChangeListener(this);
            }

            int index = getInt(INTENT_KEY_IN_IMAGE_INDEX);
            if (index < images.size()) {
                mViewPager.setCurrentItem(index);
                onPageSelected(index);
            }
        }
    }

    @NonNull
    @Override
    protected ImmersionBar createStatusBarConfig() {
        return super.createStatusBarConfig()
                // 隐藏状态栏和导航栏
                .hideBar(BarHide.FLAG_HIDE_BAR);
    }

    @Override
    public boolean isStatusBarDarkFont() {
        return false;
    }

    /**
     * {@link ViewPager.OnPageChangeListener}
     */

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @SuppressLint("SetTextI18n")
    @Override
    public void onPageSelected(int position) {
        mTextIndicatorView.setText((position + 1) + "/" + mAdapter.getCount());
    }

    @Override
    public void onPageScrollStateChanged(int state) {}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewPager.removeOnPageChangeListener(this);
    }

    /**
     * {@link BaseAdapter.OnItemClickListener}
     * @param recyclerView      RecyclerView 对象
     * @param itemView          被点击的条目对象
     * @param position          被点击的条目位置
     */
    @Override
    public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
        if (isFinishing() || isDestroyed()) {
            return;
        }
        // 单击图片退出当前的 Activity
        finish();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fl_update_image){
            ImageSelectActivity.start(this, data -> {
                // 裁剪头像
                cropImageFile(new File(data.get(0)));
            });
        }
    }


    /**
     * 裁剪图片
     */
    private void cropImageFile(File sourceFile) {
        ImageCropActivity.start(this, sourceFile, 1, 1, new ImageCropActivity.OnCropListener() {

            @Override
            public void onSucceed(Uri fileUri, String fileName) {
                File outputFile;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    outputFile = new FileContentResolver(getActivity(), fileUri, fileName);
                } else {
                    try {
                        outputFile = new File(new URI(fileUri.toString()));
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                        outputFile = new File(fileUri.toString());
                    }
                }
                updateCropImage(outputFile, true);
            }

            @Override
            public void onError(String details) {
                // 没有的话就不裁剪，直接上传原图片
                // 但是这种情况极其少见，可以忽略不计
                updateCropImage(sourceFile, false);
            }
        });
    }


    /**
     * 上传裁剪后的图片
     */
    private void updateCropImage(File file, boolean deleteFile) {
        EasyHttp.post(this)
                .api(new UploadImageApi()
                        .setToken(UserHelper.getInstance().getUser().getToken())
                        .setImage(file))
                .request(new HttpCallback<HttpData<String>>(this) {

                    @Override
                    public void onSucceed(HttpData<String> data) {
                        if (data != null && data.isRequestSucceed()){
                            updateUserInfo(data.getData());
                        }


                    }
                });
    }

    private void updateUserInfo(String avatar) {
        EasyHttp.post(this)
                .api(new UpdateImageApi()
                        .setAvatar(avatar)
                        .setToken(UserHelper.getInstance().getUser().getToken())
                        .setUid(UserHelper.getInstance().getUser().getUid()))
                .request(new HttpCallback<HttpData<String>>(this) {

                    @Override
                    public void onSucceed(HttpData<String> data) {
                        if (data == null){
                            toast("修改失败");
                            return;
                        }
                        if (data.isRequestSucceed()){
                            toast("修改成功");
                            UserHelper.getInstance().getUser().setAvatar(avatar);
                            UserHelper.getInstance().saveUserInfo(UserHelper.getInstance().getUser());
                            finish();
                        }else {
                            toast(data.getDesc());
                        }
                    }
                });
    }
}