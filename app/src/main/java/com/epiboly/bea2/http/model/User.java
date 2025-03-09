package com.epiboly.bea2.http.model;

import androidx.annotation.Keep;

/**
 * @author vemao
 * @time 2023/2/11
 * @describe
 */
@Keep
public class User{

    /**
     * id : 13
     * uid : 922448012
     * userName : JF922448012
     * nickName : mao
     * avatar : 空头像
     * phone : jqTCl8nBce0RyMsbdDLJ2w==
     * password : 5bc8DKjB2L4=
     * idCard : null
     * role : 0
     * authentication : 0
     * ipAdd :
     * imt : 0.0
     * rewardAz : 0.0
     * exchangeAz : 0.0
     * integralAz : 0.0
     * level : 1
     * device : null
     * mac : null
     * hashVal : 0
     * vxAccount : null
     * vxPayImg : null
     * zfbAccount : null
     * zfbPayImg : null
     * bzi : 0.0
     * createTime : 2023-01-21T01:35:24.000+00:00
     * updateTime : 2023-01-21T01:35:24.000+00:00
     * token : eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJsYXlTaGVuIiwianRpIjoiOTIyNDQ4MDEyIiwic3ViIjoiOTIyNDQ4MDEyIiwiaWF0IjoxNjc0MjY4MDUwLCJleHAiOjE2NzQzNTQ0NTB9.vy2lQEKKmnJegg3TFPd8yOPxSHU9WXe1bi9fjjNWYYI
     */

    private int id;
    private String uid;
    private String userName;
    private String nickName;
    private String avatar;
    private String phone;
    private String password;
    private String idCard;
    private int role;
    private int authentication;
    private String ipAdd;
    private double imt;
    private double rewardAz;
    private double exchangeAz;
    private double integralAz;
    private int level;
    private String device;
    private String mac;
    private int hashVal;
    private String vxAccount;
    private String vxPayImg;
    private String zfbAccount;
    private String zfbPayImg;
    private double bzi;
    private String createTime;
    private String updateTime;
    private String token;
    private String recommendPic;
    //是否需要校验验证码 0 不需要  1 需要
    private int needVerificationCode = 0;
    private Double lockupAz;

    public User() { }

    public User(String userJson) {
        //解析json -> user
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getAuthentication() {
        return authentication;
    }

    public void setAuthentication(int authentication) {
        this.authentication = authentication;
    }

    public String getIpAdd() {
        return ipAdd;
    }

    public void setIpAdd(String ipAdd) {
        this.ipAdd = ipAdd;
    }

    public double getImt() {
        return imt;
    }

    public void setImt(double imt) {
        this.imt = imt;
    }

    public double getRewardAz() {
        return rewardAz;
    }

    public void setRewardAz(double rewardAz) {
        this.rewardAz = rewardAz;
    }

    public double getExchangeAz() {
        return exchangeAz;
    }

    public void setExchangeAz(double exchangeAz) {
        this.exchangeAz = exchangeAz;
    }

    public double getIntegralAz() {
        return integralAz;
    }

    public void setIntegralAz(double integralAz) {
        this.integralAz = integralAz;
    }

    //用户等级： 1-实习着 2-引路者 3-开拓者 4-缔造者
    public int getLevel() {
        return level;
    }

    public String getLevelDesc() {
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

    public void setLevel(int level) {
        this.level = level;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public int getHashVal() {
        return hashVal;
    }

    public void setHashVal(int hashVal) {
        this.hashVal = hashVal;
    }

    public String getVxAccount() {
        return vxAccount;
    }

    public void setVxAccount(String vxAccount) {
        this.vxAccount = vxAccount;
    }

    public String getVxPayImg() {
        return vxPayImg;
    }

    public void setVxPayImg(String vxPayImg) {
        this.vxPayImg = vxPayImg;
    }

    public String getZfbAccount() {
        return zfbAccount;
    }

    public void setZfbAccount(String zfbAccount) {
        this.zfbAccount = zfbAccount;
    }

    public String getZfbPayImg() {
        return zfbPayImg;
    }

    public void setZfbPayImg(String zfbPayImg) {
        this.zfbPayImg = zfbPayImg;
    }

    public double getBzi() {
        return bzi;
    }

    public void setBzi(double bzi) {
        this.bzi = bzi;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRecommendPic() {
        return recommendPic;
    }

    public void setRecommendPic(String recommendPic) {
        this.recommendPic = recommendPic;
    }

    public void setNeedVerificationCode(int needVerificationCode) {
        this.needVerificationCode = needVerificationCode;
    }

    public int getNeedVerificationCode() {
        return needVerificationCode;
    }

    public boolean isNeedVerificationCode(){
        return needVerificationCode == 1;
    }

    public Double getLockupAz() {
        return lockupAz;
    }

    public void setLockupAz(Double lockupAz) {
        this.lockupAz = lockupAz;
    }
}
