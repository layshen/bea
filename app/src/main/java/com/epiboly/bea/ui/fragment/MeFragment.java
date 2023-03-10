package com.epiboly.bea.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.epiboly.bea.aop.Permissions;
import com.epiboly.bea.aop.PermissionsAspect;
import com.epiboly.bea.http.model.User;
import com.epiboly.bea.login.IdentityAuthActivity;
import com.epiboly.bea.rich.R;
import com.epiboly.bea.app.AppFragment;
import com.epiboly.bea.app.TitleBarFragment;
import com.epiboly.bea.cache.UserHelper;
import com.epiboly.bea.home.HomeMainActivity;
import com.epiboly.bea.http.api.UserInfoApi;
import com.epiboly.bea.http.glide.GlideApp;
import com.epiboly.bea.http.model.HttpData;
import com.epiboly.bea.login.LoginActivity;
import com.epiboly.bea.ui.activity.AllianceActiveActivity;
import com.epiboly.bea.ui.activity.AzDetailActivity;
import com.epiboly.bea.ui.activity.BZRRecordListActivity;
import com.epiboly.bea.ui.activity.EmptyActivity;
import com.epiboly.bea.ui.activity.HeadPicturePreviewActivity;
import com.epiboly.bea.ui.activity.MineNodeActivity;
import com.epiboly.bea.ui.activity.SettingActivity;
import com.epiboly.bea.ui.activity.SystemNoticeActivity;
import com.epiboly.bea.ui.dialog.UserDialogManager;
import com.epiboly.bea.util.BitmapHelper;
import com.epiboly.bea.util.ImageUtil;
import com.epiboly.bea.util.MathUtil;
import com.epiboly.bea.widget.wave.WaveView;
import com.hjq.base.BaseDialog;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.hjq.permissions.Permission;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

/**
 * @author mao
 * @time 2022/12/5
 * @describe 我的
 */
public class MeFragment extends TitleBarFragment<HomeMainActivity> {

    private WaveView mWaveView;
    private View mIvSettings;
    private View mInviteFriendView;
    private View mIdentityAuthView;
    private UserDialogManager mUserDialogManager;
    private TextView tvUserName;
    private TextView tvVip;
    private ImageView ivHead;
    private TextView tv_active_value;
    private TextView tv_az_value;
    private TextView tv_bzi_value;
    private TextView tvLevel;
    private SmartRefreshLayout mSmartRefresh;

    public static AppFragment<?> newInstance() {
        return new MeFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_me;
    }

    @Override
    protected void initView() {
        mSmartRefresh = (SmartRefreshLayout) findViewById(R.id.smart_refresh);
        tvUserName = findViewById(R.id.tvUserName);
        tv_active_value = findViewById(R.id.tv_active_value);
        tv_az_value = findViewById(R.id.tv_az_value);
        tv_bzi_value = findViewById(R.id.tv_bzi_value);
        ivHead = findViewById(R.id.ivHead);
        tvVip = findViewById(R.id.tv_vip);
        tvLevel = findViewById(R.id.tv_level);
        mWaveView = findViewById(R.id.wave_view);
        mIvSettings = findViewById(R.id.iv_settings);
        mInviteFriendView = findViewById(R.id.rl_invite_friend);
        mIdentityAuthView = findViewById(R.id.rl_identity_auth);
        setOnClickListener(mIvSettings, mInviteFriendView, mIdentityAuthView, ivHead);
        setOnClickListener(R.id.rl_my_alliance, R.id.rl_system_notice, R.id.rl_my_node,R.id.iv_kefu,R.id.ll_az_value,R.id.ll_bzi_value,R.id.iv_settings_qr_code,R.id.iv_settings_scan);

        mSmartRefresh.setEnableRefresh(true);
        mSmartRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getUserInfo();
            }
        });
    }

    @Override
    protected void initData() {
        mUserDialogManager = new UserDialogManager(getContext(), this);
        setupView();
    }

    @Override
    protected void onFragmentResume(boolean first) {
        super.onFragmentResume(first);
        getUserInfo();
    }

    @Override
    protected void onActivityResume() {
        super.onActivityResume();
        getUserInfo();
    }

    private void setupView() {
        tvUserName.setText(UserHelper.getInstance().getUser().getNickName());
        User user = UserHelper.getInstance().getUser();
        tv_active_value.setText(user.getHashVal() + "");
        tv_az_value.setText(MathUtil.format4(user.getIntegralAz()));
        tv_bzi_value.setText(MathUtil.format4(user.getBzi()));
        if (UserHelper.getInstance().getUser().getAuthentication() == 0) {
            tvVip.setText("未认证");
        } else {
            tvVip.setText("已认证");
        }
        tvLevel.setText(UserHelper.getLevelDesc(UserHelper.getInstance().getUser().getLevel()));
        GlideApp.with(this)
                .load(UserHelper.getInstance().getUser().getAvatar())
                .placeholder(R.drawable.empty_avatar_user)
                .circleCrop()
                .error(R.drawable.empty_avatar_user)
                .into(ivHead);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_identity_auth:
                if (UserHelper.getInstance().getUser().getAuthentication() == 0){
                    IdentityAuthActivity.start(getActivity());
                }else {
                    toast("已完成认证");
                }
                break;
            case R.id.iv_settings:
                SettingActivity.start(getActivity());
                break;
            case R.id.iv_settings_qr_code:
                EmptyActivity.start(getActivity(),MyCollectionQRCodeFragment.class);
                break;
            case R.id.iv_settings_scan:
                startScan();
                break;
            case R.id.rl_invite_friend:
                //自定义对话框
                showShareDialog();
                break;
            case R.id.rl_my_alliance:
                //我的联盟
                AllianceActiveActivity.start(getActivity(), UserHelper.getInstance().getUser().getUid());
                break;
            case R.id.rl_my_node:
                MineNodeActivity.start(getActivity());
                break;
            case R.id.ivHead:
                HeadPicturePreviewActivity.start(getActivity(), UserHelper.getInstance().getUser().getAvatar());
                break;
            case R.id.iv_kefu:
                try {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:18956915241"));
                    getActivity().startActivity(intent);
                } catch (Exception e) {}
                break;
            case R.id.ll_az_value:
                AzDetailActivity.start(getActivity());
                break;
            case R.id.ll_bzi_value:
                BZRRecordListActivity.start(getActivity());
                break;
            case R.id.rl_system_notice:
                SystemNoticeActivity.start(getActivity());
                break;
        }
    }

    @Permissions({Permission.CAMERA, Permission.READ_EXTERNAL_STORAGE})
    private void startScan() {
        EmptyActivity.start(getActivity(),QrCodeScanFragment.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        mWaveView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mWaveView.onPause();
    }

    private void showShareDialog() {
        if (!UserHelper.getInstance().isLogin()) {
            LoginActivity.start(getActivity(), "", "");
            return;
        }
        new BaseDialog.Builder<>(getContext())
                .setContentView(R.layout.dialog_invite_friend)
                .setAnimStyle(BaseDialog.ANIM_SCALE)
                .setCanceledOnTouchOutside(true)
                .setOnClickListener(R.id.btn_dialog_custom_ok, new BaseDialog.OnClickListener<View>() {
                    @Override
                    public void onClick(BaseDialog dialog, View view) {
                        Bitmap bitmapFromView = BitmapHelper.createBitmapFromView(dialog.findViewById(R.id.ll_content_share));
                        ImageUtil.saveBitmapToAlbum(getContext().getApplicationContext(), bitmapFromView, "fba_" + System.currentTimeMillis() + "_share", "image/jpeg", Bitmap.CompressFormat.JPEG);
                        dialog.dismiss();
                    }
                })
                .setOnCreateListener(new BaseDialog.OnCreateListener() {
                    @Override
                    public void onCreate(BaseDialog dialog) {
                        ImageView ivAvatar = dialog.findViewById(R.id.iv_avatar);
                        TextView tvNick = dialog.findViewById(R.id.tv_nick);
                        TextView tvId = dialog.findViewById(R.id.tv_id);
                        ImageView ivQrCode = dialog.findViewById(R.id.iv_qr_code);

                        tvId.setText("ID:" + UserHelper.getInstance().getUser().getUid());
                        tvNick.setText(UserHelper.getInstance().getUser().getNickName());
                        GlideApp.with(getContext()).load(UserHelper.getInstance().getUser().getAvatar())
                                .into(ivAvatar);
                        GlideApp.with(getContext()).load(UserHelper.getInstance().getUser().getRecommendPic())
                                .into(ivQrCode);
                    }
                })
                .setOnKeyListener((dialog, event) -> {
                    return false;
                })
                .show();
    }

    private void getUserInfo() {
        EasyHttp.post(this)
                .api(new UserInfoApi()
                        .setToken(UserHelper.getInstance().getUser().getToken())
                        .setUid(UserHelper.getInstance().getUser().getUid()))
                .request(new HttpCallback<HttpData<User>>(this) {

                    @Override
                    public void onSucceed(HttpData<User> data) {
                        if (data!=null && data.isRequestSucceed()){
                            if (TextUtils.isEmpty(data.getData().getToken())){
                                data.getData().setToken(UserHelper.getInstance().getUser().getToken());
                            }
                            mSmartRefresh.finishRefresh(2000,true,null);
                            UserHelper.getInstance().saveUserInfo(data.getData());
                            setupView();
                        }else {
                            mSmartRefresh.finishRefresh(2000,false,null);
                        }
                    }
                });
    }


    @Override
    public boolean isStatusBarEnabled() {
        return true;
    }
}
