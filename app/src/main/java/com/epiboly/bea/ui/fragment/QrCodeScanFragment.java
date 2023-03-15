package com.epiboly.bea.ui.fragment;

import static android.content.Context.VIBRATOR_SERVICE;

import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.epiboly.bea.action.ToastAction;
import com.epiboly.bea.http.api.QrReChargePicAPI;
import com.epiboly.bea.http.api.QrScanRequestAPI;
import com.epiboly.bea.http.glide.GlideApp;
import com.epiboly.bea.http.model.HttpData;
import com.epiboly.bea.http.model.IntegralServer;
import com.epiboly.bea.http.model.ScanQrModel;
import com.epiboly.bea.R;
import com.epiboly.bea.ui.activity.AzScanResultExchangeActivity;
import com.epiboly.bea.ui.activity.EmptyActivity;
import com.epiboly.bea.ui.activity.ImageSelectActivity;
import com.epiboly.bea.ui.dialog.TipsDialog;
import com.google.gson.reflect.TypeToken;
import com.hjq.base.BaseFragment;
import com.hjq.gson.factory.GsonFactory;
import com.hjq.http.EasyHttp;
import com.hjq.http.config.IRequestServer;
import com.hjq.http.listener.HttpCallback;

import java.lang.reflect.Type;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

/**
 * @author mao
 * @time 2023/3/8
 * @describe 二维码扫描
 */
public class QrCodeScanFragment extends BaseFragment<EmptyActivity> implements QRCodeView.Delegate , ToastAction {

    private static final String TAG = QrCodeScanFragment.class.getSimpleName();

    private ZXingView mZXingView;
    private TextView mTvLight;
    private boolean isLightOpen = false;
    private LinearLayout mLlAlbum;
    private ImageView mIvClose;
    private ImageView mIvLight;
    private LinearLayout mLlLight;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_qr_code_scan;
    }

    @Override
    protected void initView() {
        mZXingView = findViewById(R.id.zxingview);
        mZXingView.setDelegate(this);
        mIvLight = (ImageView) findViewById(R.id.iv_light);
        mLlLight = (LinearLayout) findViewById(R.id.ll_light);
        mTvLight = (TextView) findViewById(R.id.tv_light);
        mLlAlbum = (LinearLayout) findViewById(R.id.ll_album);
        mIvClose = (ImageView) findViewById(R.id.iv_close);
        setOnClickListener(mLlLight, mLlAlbum, mIvClose);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onStart() {
        super.onStart();
        mZXingView.startCamera(); // 打开后置摄像头开始预览，但是并未开始识别
//      mZXingView.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT); // 打开前置摄像头开始预览，但是并未开始识别
        mZXingView.startSpotAndShowRect(); // 显示扫描框，并开始识别
    }

    @Override
    public void onStop() {
        mZXingView.stopCamera(); // 关闭摄像头预览，并且隐藏扫描框
        super.onStop();
    }

    @Override
    public void onDestroy() {
        mZXingView.onDestroy(); // 销毁二维码扫描控件
        super.onDestroy();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getContext().getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        if (TextUtils.isEmpty(result)){
            toast("扫码解析失败");
            vibrate();
            getAttachActivity().finish();
        }else {
            if (result.startsWith("http:") || result.startsWith("https")){
                requestUrl(result);
            }else {
                toast("扫码解析失败");
                vibrate();
                getAttachActivity().finish();
            }
        }
    }

    private void requestUrl(String result) {
        EasyHttp.get(this)
                .api(new QrScanRequestAPI().setUrl(result))
                .server(new IRequestServer() {
                    @Override
                    public String getHost() {
                        return "";
                    }
                })
                .request(new HttpCallback<HttpData<ScanQrModel>>(null) {

                    @Override
                    public void onSucceed(HttpData<ScanQrModel> data) {
                        if (data == null || data.getData() == null){
                            new TipsDialog.Builder(getActivity())
                                    .setIcon(TipsDialog.ICON_ERROR)
                                    .setMessage(R.string.password_reset_fail)
                                    .setDuration(2000)
                                    .show();
                            return;
                        }
                        if (data.isRequestSucceed()){
                            vibrate();
                            AzScanResultExchangeActivity.start(getActivity(),data.getData());
                            getAttachActivity().finish();
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
    public void onCameraAmbientBrightnessChanged(boolean isDark) {
        if (isLightOpen) {
            mTvLight.setText("轻触关闭");
        } else {
            mTvLight.setText("轻触照亮");
        }
        if (isDark || isLightOpen) {
            mLlLight.setVisibility(View.VISIBLE);
        } else {
            mLlLight.setVisibility(View.GONE);
        }
    }

    @Override
    public void onScanQRCodeOpenCameraError() {

    }

    @Override
    public void onClick(View view) {
        if (view == mLlLight) {
            if (isLightOpen) {
                mZXingView.closeFlashlight(); // 关闭闪光灯
                mIvLight.setImageResource(R.drawable.ic_scan_light_close);
            } else {
                mZXingView.openFlashlight(); // 打开闪光灯
                mIvLight.setImageResource(R.drawable.ic_scan_light_open);
            }
            isLightOpen = !isLightOpen;
        } else if (view == mLlAlbum) {
            ImageSelectActivity.start(getAttachActivity(), data -> {
                mZXingView.decodeQRCode(data.get(0));
            });
        } else if (view == mIvClose) {
            getAttachActivity().finish();
        }
    }
}
