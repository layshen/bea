package com.epiboly.bea.http.model;

import androidx.annotation.Keep;

/**
 * @author vemao
 * @time 2023/1/30
 * @describe
 */
@Keep
public class TaskBean {
    private int progress;
    private int total;
    private int type;
    private String name;
    private String image;

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
