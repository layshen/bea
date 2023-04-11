package com.epiboly.bea.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.epiboly.bea.app.AppActivity;
import com.epiboly.bea.cache.UserHelper;
import com.epiboly.bea.http.api.AzQrExchangeApi;
import com.epiboly.bea.http.api.AzRecordListApi;
import com.epiboly.bea.http.glide.GlideApp;
import com.epiboly.bea.http.model.HttpData;
import com.epiboly.bea.http.model.IntegralServer;
import com.epiboly.bea.http.model.ScanQrModel;
import com.epiboly.bea.R;
import com.epiboly.bea.ui.dialog.PayPasswordDialog;
import com.hjq.bar.TitleBar;
import com.hjq.base.BaseDialog;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.hjq.widget.view.RegexEditText;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * @author vemao
 * @time 2023/2/8
 * @describe
 */
public class AzScanResultExchangeActivity extends AppActivity {


    private TitleBar mTitleBar;
    private TextView mTvDesc;
    private TextView mTvNick;
    private RegexEditText mInputAz;
    private TextView mTv1;
    private TextView mTv2;
    private TextView mTv3;
    private FrameLayout mFlDel;
    private TextView mTvPay;
    private TextView mTv4;
    private TextView mTv5;
    private TextView mTv6;
    private TextView mTv7;
    private TextView mTv8;
    private TextView mTv9;
    private TextView mTv0;
    private TextView mTvPoint;
    private ImageView mIvHead;

    public static void start(Context context, ScanQrModel data) {
        Intent intent = new Intent(context, AzScanResultExchangeActivity.class);
        intent.putExtra("inUid", data.getUid());
        intent.putExtra("inUserName", data.getUserName());
        intent.putExtra("inUserAvatar", data.getAvatar());
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_az_scan_result_exchange;
    }

    @Override
    protected void initView() {
        mTitleBar = (TitleBar) findViewById(R.id.title_bar);
        mTvDesc = (TextView) findViewById(R.id.tv_desc);
        mTvNick = (TextView) findViewById(R.id.tv_nick);
        mInputAz = (RegexEditText) findViewById(R.id.input_az);
        mTv1 = (TextView) findViewById(R.id.tv_1);
        mTv2 = (TextView) findViewById(R.id.tv_2);
        mTv3 = (TextView) findViewById(R.id.tv_3);
        mFlDel = (FrameLayout) findViewById(R.id.fl_del);
        mTvPay = (TextView) findViewById(R.id.tv_pay);
        mTv4 = (TextView) findViewById(R.id.tv_4);
        mTv5 = (TextView) findViewById(R.id.tv_5);
        mTv6 = (TextView) findViewById(R.id.tv_6);
        mTv7 = (TextView) findViewById(R.id.tv_7);
        mTv8 = (TextView) findViewById(R.id.tv_8);
        mTv9 = (TextView) findViewById(R.id.tv_9);
        mTv0 = (TextView) findViewById(R.id.tv_0);
        mTvPoint = (TextView) findViewById(R.id.tv_point);
        softHandle();
        setOnClickListener(mTv1, mTv2, mTv3, mTv4, mTv5, mTv6, mTv7, mTv8, mTv9, mTv0, mTvPoint, mTvPay, mFlDel);
        mFlDel.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                delText();
                return true;
            }
        });
        mIvHead = (ImageView) findViewById(R.id.iv_head);
    }

    private void softHandle() {
        if (Build.VERSION.SDK_INT > 10) {//4.0以上
            try {
                Class cls = EditText.class;
                Method setShowSoftInputOnFocus;
                setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(mInputAz, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        mInputAz.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    InputMethodManager imm = (InputMethodManager) mInputAz.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(mInputAz.getWindowToken(), 0);
                    }
                }
            }
        });
    }

    @Override
    protected void initData() {
        mTvNick.setText(getString("inUserName"));
        GlideApp.with(this)
                .load(getString("inUserAvatar"))
                .placeholder(R.drawable.empty_avatar_user)
                .error(R.drawable.empty_avatar_user)
                .into(mIvHead);
    }

    @Override
    public void onClick(View view) {
        if (view == mTv0) {
            insertText("0");
        } else if (view == mTv1) {
            insertText("1");
        } else if (view == mTv2) {
            insertText("2");
        } else if (view == mTv3) {
            insertText("3");
        } else if (view == mTv4) {
            insertText("4");
        } else if (view == mTv5) {
            insertText("5");
        } else if (view == mTv6) {
            insertText("6");
        } else if (view == mTv7) {
            insertText("7");
        } else if (view == mTv8) {
            insertText("8");
        } else if (view == mTv9) {
            insertText("9");
        } else if (view == mTvPay) {
            toPay();
        } else if (view == mFlDel) {
            delText();
        } else if (view == mTvPoint) {
            insertText(".");
        }
    }

    private void toPay() {
        if (TextUtils.isEmpty(mInputAz.getText().toString())){
            toast("输入不能为空");
            return;
        }
        //check
        new PayPasswordDialog.Builder(this)
                .setTitle("请输入交易密码")
                .setMoney("AZ：" + mInputAz.getText().toString())
                //.setAutoDismiss(false) // 设置点击按钮后不关闭对话框
                .setListener(new PayPasswordDialog.OnListener() {

                    @Override
                    public void onCompleted(BaseDialog dialog, String password) {
                        realPay(mInputAz.getText().toString(), password);
                    }

                    @Override
                    public void onCancel(BaseDialog dialog) {
                        toast("请输入交易密码");
                    }
                })
                .show();
    }

    private void realPay(String num, String password) {
        float aFloat = Float.parseFloat(num);
        EasyHttp.post(this)
                .server(new IntegralServer())
                .api(new AzQrExchangeApi()
                        .setPassword(password)
                        .setAz(aFloat)
                        .setInUid(getString("inUid"))
                        .setToken(UserHelper.getInstance().getUser().getToken())
                        .setUid(UserHelper.getInstance().getUser().getUid()))
                .request(new HttpCallback<HttpData<ArrayList<AzRecordListApi.Bean>>>(this) {

                    @Override
                    public void onSucceed(HttpData<ArrayList<AzRecordListApi.Bean>> data) {
                        if (data == null) {
                            return;
                        }
                        if (data.isRequestSucceed()) {
                            toast("转出成功");
                            getActivity().finish();
                        } else {
                            toast(data.getDesc());
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        toast("请求出错");
                    }
                });
    }

    private void insertText(String num) {
        String str = mInputAz.getText().toString();
        if ("0".equals(str) && TextUtils.isEmpty(str)) {
            return;
        }
        int index = mInputAz.getSelectionStart();
        Editable editable = mInputAz.getText();
        editable.insert(index, num);
    }

    private void delText() {
        String str = mInputAz.getText().toString();
        if (TextUtils.isEmpty(str)) {
            return;
        }
        int index = mInputAz.getSelectionStart();
        if (index <= 0) {
            return;
        }
        Editable editable = mInputAz.getText();
        editable.delete(index - 1, index);
    }
}
