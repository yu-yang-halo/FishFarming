package com.farmingsocket.client.bean;

/**
 * Created by Administrator on 2017/9/6.
 * 阈值数据结构
 */

public class BaseThreshold extends BaseCommand{
    private String mac;
    private int max;
    private int min;

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }
}
