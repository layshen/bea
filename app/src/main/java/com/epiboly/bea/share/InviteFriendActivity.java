package com.epiboly.bea.share;

import com.epiboly.bea.rich.R;
import com.epiboly.bea.app.AppActivity;
import com.epiboly.bea.cache.UserHelper;
import com.epiboly.bea.http.api.InviteFriendApi;
import com.epiboly.bea.http.model.HttpData;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;


/**
 * @author mao
 * @time 2023/1/21
 * @describe 邀请好友
 */
public class InviteFriendActivity extends AppActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.dialog_invite_friend;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        EasyHttp.post(this)
                .api(new InviteFriendApi()
                        .setToken(UserHelper.getInstance().getUser().getToken())
                        .setUid(UserHelper.getInstance().getUser().getUid()))
                .request(new HttpCallback<HttpData<String>>(this) {

                    @Override
                    public void onSucceed(HttpData<String> data) {
                        if (data.isRequestSucceed()){
                            postDelayed(() -> {

                            }, 1000);
                        }else {
                            postDelayed(() -> {
                                toast(data.getDesc());
                            }, 1000);
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                        postDelayed(() -> {

                        }, 1000);
                    }
                });
    }
}
