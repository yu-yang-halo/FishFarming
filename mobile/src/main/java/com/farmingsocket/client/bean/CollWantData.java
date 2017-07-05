package com.farmingsocket.client.bean;

/**
 * Created by Administrator on 2017/7/5 0005.
 */

public class CollWantData {
    private int    reviceTime;
    private float  value;
    private int    type;
    private int    order;

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;

    }

    public int getReviceTime() {
        return reviceTime;
    }

    public void setReviceTime(int reviceTime) {
        this.reviceTime = reviceTime;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "CollWantData{" +
                "reviceTime=" + reviceTime +
                ", value=" + value +
                '}';
    }

    public CollWantData(int reviceTime, float value) {
        this.reviceTime = reviceTime;
        this.value = value;
    }
}