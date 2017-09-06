package com.farmingsocket.client.bean;

/**
 * Created by Administrator on 2017/9/6.
 * 时间间隔 fuck
 */

public class BaseTimePeriod extends BaseCommand {
    private String mac;
    private int  minute;

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }
}
