package com.epiboly.bea.ui.fragment;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.epiboly.bea.action.TitleBarAction;
import com.epiboly.bea.action.ToastAction;
import com.epiboly.bea.app.TitleBarFragment;
import com.epiboly.bea.cache.UserHelper;
import com.epiboly.bea.http.api.AzQrExchangeApi;
import com.epiboly.bea.http.api.EditPasswordApi;
import com.epiboly.bea.http.api.QrReChargePicAPI;
import com.epiboly.bea.http.glide.GlideApp;
import com.epiboly.bea.http.model.HttpData;
import com.epiboly.bea.http.model.MyNodeServer;
import com.epiboly.bea.http.model.RequestServer;
import com.epiboly.bea.http.model.User;
import com.epiboly.bea.R;
import com.epiboly.bea.ui.activity.EmptyActivity;
import com.epiboly.bea.ui.dialog.TipsDialog;
import com.epiboly.bea.util.BitmapHelper;
import com.epiboly.bea.util.ImageUtil;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.hjq.base.BaseFragment;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.qmuiteam.qmui.widget.QMUILoadingView;

/**
 * @author mao
 * @time 2023/3/8
 * @describe 收款码
 */
public class MyCollectionQRCodeFragment extends TitleBarFragment<EmptyActivity> implements ToastAction {

    private ImageView mIvHead;
    private TextView mTvName;
    private ImageView mIvQrCode;
    private QMUILoadingView mLoadView;
    private TextView mTvSaveQrCode;
    private TitleBar mTitleBar;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_collection_qr_code;
    }

    @Override
    protected void initView() {
        mTitleBar = (TitleBar) findViewById(R.id.title_bar);
        mIvHead = (ImageView) findViewById(R.id.iv_head);
        mTvName = (TextView) findViewById(R.id.tv_name);
        mIvQrCode = (ImageView) findViewById(R.id.iv_qr_code);
        mLoadView = (QMUILoadingView) findViewById(R.id.load_view);
        mTvSaveQrCode = (TextView) findViewById(R.id.tv_save_qr_code);
        setOnClickListener(mTvSaveQrCode);

        getAttachActivity()
                .getStatusBarConfig()
                .statusBarDarkFont(true)
                .titleBar(mTitleBar)
                .statusBarColor(R.color.common_accent_color)
                .init();
    }

    @Override
    protected void initData() {
        User user = UserHelper.getInstance().getUser();
        GlideApp.with(this)
                .load(user.getAvatar())
                .placeholder(R.drawable.empty_avatar_user)
                .circleCrop()
                .error(R.drawable.empty_avatar_user)
                .into(mIvHead);
        mTvName.setText(user.getNickName() + "的收款码");
        EasyHttp.post(this)
                .api(new QrReChargePicAPI()
                        .setToken(user.getToken())
                        .setUid(user.getUid()))
                .request(new HttpCallback<HttpData<String>>(null) {

                    @Override
                    public void onSucceed(HttpData<String> data) {
                        if (data == null || TextUtils.isEmpty(data.getData())){
                            new TipsDialog.Builder(getActivity())
                                    .setIcon(TipsDialog.ICON_ERROR)
                                    .setMessage(R.string.password_reset_fail)
                                    .setDuration(2000)
                                    .show();
                            return;
                        }
                        if (data.isRequestSucceed()){
                            mLoadView.stop();
                            mLoadView.setVisibility(View.GONE);
                            GlideApp.with(MyCollectionQRCodeFragment.this)
                                    .load(data.getData())
                                    .into(mIvQrCode);
                        }else {
                            new TipsDialog.Builder(getActivity())
                                    .setIcon(TipsDialog.ICON_ERROR)
                                    .setMessage(data.getDesc())
                                    .setDuration(2000)
                                    .show();
                        }
                    }
                });

    }

    @Override
    public void onClick(View view) {
        if (view == mTvSaveQrCode) {
            Bitmap bitmapFromView = BitmapHelper.createBitmapFromView(mIvQrCode);
            ImageUtil.saveBitmapToAlbum(getContext().getApplicationContext(), bitmapFromView, "fba_" + System.currentTimeMillis() + "_qr_code", "image/jpeg", Bitmap.CompressFormat.JPEG);
            toast("保存成功");
        }
    }

    @Nullable
    @Override
    public TitleBar getTitleBar() {
        return mTitleBar;
    }

    @Override
    public void onLeftClick(View view) {
        getAttachActivity().finish();
    }
}
