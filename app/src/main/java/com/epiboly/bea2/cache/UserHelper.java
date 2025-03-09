package com.epiboly.bea2.cache;

import android.text.TextUtils;

import com.epiboly.bea2.http.model.User;
import com.hjq.gson.factory.GsonFactory;
import com.tencent.mmkv.MMKV;

/**
 * @author mve
 * @time 2022/11/27
 * @describe 用户登录数据
 */
public class UserHelper {

    private static final UserHelper mInstance = Holder.helper;
    private final MMKV mMmkv;
    private User user;
    private boolean focusIsLogin;

    private UserHelper(){
        mMmkv = MMKV.mmkvWithID("user_data");
        String userJson = mMmkv.getString("user", "");
        user = GsonFactory.getSingletonGson().fromJson(userJson,User.class);
    }

    public static UserHelper getInstance(){
        return mInstance;
    }

    public void clear(){
        user = new User();
        mMmkv.clear();
    }

    public boolean isLogin() {
        return mMmkv.getBoolean("focusIsLogin",true) && !TextUtils.isEmpty(getUser().getToken());
    }

    public void saveUserInfo(User user) {
        this.user = user;
        mMmkv.putString("user", GsonFactory.getSingletonGson().toJson(user));
    }

    public String getToken() {
        if (user == null){
            return "";
        }
        return user.getToken();
    }

    public User getUser() {
        if (user == null){
            user = new User();
            return user;
        }
        return user;
    }

    public static String getLevelDesc(int level) {
        switch (level){
            case 1:
                return "实习者";
            case 2:
                return "引路者";
            case 3:
                return "开拓者";
            case 4:
                return "缔造者";
        }
        return "普通用户";
    }

    public static String getAuthenticationText(int authentication){
        if (authentication == 0){
            return "未认证";
        }
        return "已认证";
    }

    public void setFocusIsLogin(boolean isLogin) {
        mMmkv.putBoolean("focusIsLogin",isLogin);
    }


    static class Holder{
        static UserHelper helper = new UserHelper();
    }


}
