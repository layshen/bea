package com.epiboly.bea2.cache;

import android.app.Application;
import android.text.TextUtils;

import com.github.gzuliyujiang.oaid.DeviceID;
import com.github.gzuliyujiang.oaid.DeviceIdentifier;
import com.github.gzuliyujiang.oaid.IGetter;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.mmkv.MMKV;

/**
 * @author vemao
 * @time 2023/2/12
 * @describe
 */
public class DeviceIdentifierHelper {
    private final MMKV mMmkv;
    private static volatile DeviceIdentifierHelper mInstance;

    private DeviceIdentifierHelper(){
        mMmkv = MMKV.mmkvWithID("DeviceIdentifier");
    }

    public static DeviceIdentifierHelper getInstance(){
        if (mInstance == null){
            synchronized (DeviceIdentifierHelper.class){
                if (mInstance == null){
                    mInstance = new DeviceIdentifierHelper();
                }
            }
        }
        return mInstance;
    }

    public String getDeviceUniqueId(Application application){
        String device_unique_id = mMmkv.getString("device_unique_id", "");
        try {
            if (!TextUtils.isEmpty(device_unique_id)){
                return device_unique_id;
            }
            String androidID = DeviceIdentifier.getAndroidID(application);
            if (!TextUtils.isEmpty(androidID)){
                saveDeviceUniqueId(androidID);
                return androidID;
            }
            String widevineID = DeviceIdentifier.getWidevineID();
            if (!TextUtils.isEmpty(widevineID)){
                saveDeviceUniqueId(widevineID);
                return widevineID;
            }
            String pseudoID = DeviceIdentifier.getPseudoID();
            if (!TextUtils.isEmpty(pseudoID)){
                saveDeviceUniqueId(pseudoID);
                return pseudoID;
            }
            String imei = DeviceIdentifier.getIMEI(application);
            if (!TextUtils.isEmpty(imei)){
                saveDeviceUniqueId(imei);
                return imei;
            }
        }catch (Exception e){
            CrashReport.postCatchedException(e);
        }
        return "";
    }

    public void init(Application application) {
        try {
            DeviceIdentifier.register(application);
            // 获取OAID/AAID，异步回调
            if (DeviceID.supportedOAID(application)){
                DeviceID.getOAID(application, new IGetter() {
                    @Override
                    public void onOAIDGetComplete(String result) {
                        // 不同厂商的OAID/AAID格式是不一样的，可进行MD5、SHA1之类的哈希运算统一
                        saveDeviceUniqueId(result);
                    }

                    @Override
                    public void onOAIDGetError(Exception error) {
                        // 获取OAID/AAID失败
                    }
                });
                return;
            }
            String androidID = DeviceIdentifier.getAndroidID(application);
            if (!TextUtils.isEmpty(androidID)){
                saveDeviceUniqueId(androidID);
                return;
            }
            String widevineID = DeviceIdentifier.getWidevineID();
            if (!TextUtils.isEmpty(widevineID)){
                saveDeviceUniqueId(widevineID);
                return;
            }
            String pseudoID = DeviceIdentifier.getPseudoID();
            if (!TextUtils.isEmpty(pseudoID)){
                saveDeviceUniqueId(pseudoID);
                return;
            }
            String imei = DeviceIdentifier.getIMEI(application);
            if (!TextUtils.isEmpty(imei)){
                saveDeviceUniqueId(imei);
            }
        }catch (Exception e){
            CrashReport.postCatchedException(e);
        }
    }

    private void saveDeviceUniqueId(String id) {
        mMmkv.putString("device_unique_id",id);
    }

}
