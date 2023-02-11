package com.epiboly.bea.http.model;

import androidx.annotation.Keep;

/**
 * @author vemao
 * @time 2023/2/4
 * @describe
 */
@Keep
public class MyNodeBean {
    private int nid;
    private String createTime;
    private String dayOut;
    private String produced;
    private String totalOutput;
    private String daysRemaining;
    private String cycle;

    public int getNid() {
        return nid;
    }

    public void setNid(int nid) {
        this.nid = nid;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getDayOut() {
        return dayOut;
    }

    public void setDayOut(String dayOut) {
        this.dayOut = dayOut;
    }

    public String getProduced() {
        return produced;
    }

    public void setProduced(String produced) {
        this.produced = produced;
    }

    public String getTotalOutput() {
        return totalOutput;
    }

    public void setTotalOutput(String totalOutput) {
        this.totalOutput = totalOutput;
    }

    public String getDaysRemaining() {
        return daysRemaining;
    }

    public void setDaysRemaining(String daysRemaining) {
        this.daysRemaining = daysRemaining;
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }
}
