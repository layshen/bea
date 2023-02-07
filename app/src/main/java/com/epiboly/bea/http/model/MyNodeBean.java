package com.epiboly.bea.http.model;

/**
 * @author vemao
 * @time 2023/2/4
 * @describe
 */
public class MyNodeBean {
    private int drawable;
    private String name;
    private int num;

    public MyNodeBean() {
    }

    public MyNodeBean(int drawable, String name, int num) {
        this.drawable = drawable;
        this.name = name;
        this.num = num;
    }

    public int getDrawable() {
        return drawable;
    }

    public String getName() {
        return name;
    }

    public int getNum() {
        return num;
    }
}
