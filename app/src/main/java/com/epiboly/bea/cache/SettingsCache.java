package com.epiboly.bea.cache;

import com.tencent.mmkv.MMKV;

/**
 * @author vemao
 * @time 2023/1/29
 * @describe
 */
public class SettingsCache {
    private final MMKV mMmkv;

    private SettingsCache(){
        mMmkv = MMKV.mmkvWithID("SettingsCache");
    }

    public static SettingsCache create(){
        return new SettingsCache();
    }

    public void setIsAutoLogin(boolean isAutoLogin){
        mMmkv.putBoolean("isAutoLogin",isAutoLogin);
    }

    public boolean isAutoLogin(){
       return mMmkv.getBoolean("isAutoLogin",true);
    }
}
