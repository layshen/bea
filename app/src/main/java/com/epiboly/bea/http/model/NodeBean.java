package com.epiboly.bea.http.model;

import androidx.annotation.Keep;

/**
 * @author 节点
 * @time 2023/1/23
 * @describe
 */
@Keep
public class NodeBean {

    public static final int COMING_TYPE = -1000;

    /**
     * id : 1
     * type : 0
     * cycle : 30
     * maxBuy : 1
     * totalOutput : 13.5
     * purchasingPower : 0
     * hashVal : 1
     */

    private int id;
    private String name;
    private int type;
    private String cycle;
    private int maxBuy;
    private double totalOutput;
    private int purchasingPower;
    private int hashVal;
    private String img;
    private String description;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public int getMaxBuy() {
        return maxBuy;
    }

    public void setMaxBuy(int maxBuy) {
        this.maxBuy = maxBuy;
    }

    public double getTotalOutput() {
        return totalOutput;
    }

    public void setTotalOutput(double totalOutput) {
        this.totalOutput = totalOutput;
    }

    public int getPurchasingPower() {
        return purchasingPower;
    }

    public void setPurchasingPower(int purchasingPower) {
        this.purchasingPower = purchasingPower;
    }

    public int getHashVal() {
        return hashVal;
    }

    public void setHashVal(int hashVal) {
        this.hashVal = hashVal;
    }

    public String getImgUrl() {
        return img;
    }

    public void setImgUrl(String imgUrl) {
        this.img = imgUrl;
    }

    public String getName() {
        switch (type){
            case 0:
                return "T级节点";
            case 1:
                return "A级节点";
            case 2:
                return "B级节点";
            case 3:
                return "C级节点";
            case 4:
                return "D级节点";
            case 5:
                return "E级节点";
            case 6:
                return "F级节点";
        }
        return "未知";
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
