package com.epiboly.bea2.ui.dialog;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;

import com.epiboly.bea2.cache.UserHelper;
import com.epiboly.bea2.http.api.IdentityAuthApi;
import com.epiboly.bea2.http.api.UpdateNickApi;
import com.epiboly.bea2.http.model.HttpData;
import com.hjq.base.BaseDialog;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;

/**
 * @author mao
 * @time 2023/1/21
 * @describe
 */
public class UserDialogManager {

    private final Context context;
    private final LifecycleOwner lifecycleOwner;

    public UserDialogManager(Context fragment, LifecycleOwner lifecycleOwner) {
        this.context = fragment;
        this.lifecycleOwner = lifecycleOwner;
    }

    public void showIdCardAuth() {
        // 输入对话框
        new InputDialog.Builder(context)
                // 标题可以不用填写
                .setTitle("身份认证")
                // 提示可以不用填写
                .setHint("请输入身份证")
                // 确定按钮文本
                .setConfirm("认证")
                // 设置 null 表示不显示取消按钮
                .setCancel("取消")
                // 设置点击按钮后不关闭对话框
                //.setAutoDismiss(false)
                .setListener(new InputDialog.OnListener() {

                    @Override
                    public void onConfirm(BaseDialog dialog, String content) {

                        EasyHttp.post(lifecycleOwner)
                                .api(new IdentityAuthApi()
                                        .setIdCard(content)
                                        .setToken(UserHelper.getInstance().getUser().getToken())
                                        .setUid(UserHelper.getInstance().getUser().getUid()))
                                .request(new HttpCallback<HttpData<Void>>(null) {


                                    @Override
                                    public void onSucceed(HttpData<Void> data) {
                                        if (data == null){
                                            showErrorTipDailog("认证失败");
                                            return;
                                        }
                                        if (data.isRequestSucceed()){
                                            showSuccessTipDailog("认证成功");
                                        }else {
                                            showErrorTipDailog("认证失败");
                                        }
                                    }

                                    @Override
                                    public void onFail(Exception e) {
                                        super.onFail(e);
                                        showErrorTipDailog("认证失败");
                                    }
                                });
                    }

                    @Override
                    public void onCancel(BaseDialog dialog) {
                    }
                })
                .show();
    }

    public void showNickUpdateDialog() {
        // 输入对话框
        new InputDialog.Builder(context)
                // 标题可以不用填写
                .setTitle("修改昵称")
                // 提示可以不用填写
                .setHint(UserHelper.getInstance().getUser().getNickName())
                // 确定按钮文本
                .setConfirm("修改")
                // 设置 null 表示不显示取消按钮
                .setCancel("取消")
                // 设置点击按钮后不关闭对话框
                //.setAutoDismiss(false)
                .setListener(new InputDialog.OnListener() {

                    @Override
                    public void onConfirm(BaseDialog dialog, String content) {

                        EasyHttp.post(lifecycleOwner)
                                .api(new UpdateNickApi()
                                        .setNickName(content)
                                        .setToken(UserHelper.getInstance().getUser().getToken())
                                        .setUid(UserHelper.getInstance().getUser().getUid()))
                                .request(new HttpCallback<HttpData<Void>>(null) {


                                    @Override
                                    public void onSucceed(HttpData<Void> data) {
                                        if (data == null){
                                            showErrorTipDailog("修改失败");
                                            return;
                                        }
                                        if (data.isRequestSucceed()){
                                            UserHelper.getInstance().getUser().setNickName(content);
                                            UserHelper.getInstance().saveUserInfo(UserHelper.getInstance().getUser());
                                            showSuccessTipDailog("修改成功");
                                        }else {
                                            showErrorTipDailog("修改失败");
                                        }
                                    }

                                    @Override
                                    public void onFail(Exception e) {
                                        super.onFail(e);
                                        showErrorTipDailog("修改失败");
                                    }
                                });
                    }

                    @Override
                    public void onCancel(BaseDialog dialog) {
                    }
                }).show();
    }

    private void showErrorTipDailog(String msg) {
        // 失败对话框
        new TipsDialog.Builder(context)
                .setIcon(TipsDialog.ICON_ERROR)
                .setMessage(msg)
                .show();
    }

    private void showSuccessTipDailog(String msg) {
        // 成功对话框
        new TipsDialog.Builder(context)
                .setIcon(TipsDialog.ICON_FINISH)
                .setMessage(msg)
                .show();
    }
}
