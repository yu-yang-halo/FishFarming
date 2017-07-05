package com.farmingsocket.client.bean;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * Created by Administrator on 2017/7/5 0005.
 */

public class BaseRealTimeData extends BaseCommand{
    private String mac;
    @SerializedName("json")
    private Map realData;

    @Override
    public String toString() {
        return "BaseRealTimeData{" +
                "mac='" + mac + '\'' +
                ", realData=" + realData +
                '}';
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public Map getRealData() {
        return realData;
    }

    public void setRealData(Map realData) {
        this.realData = realData;
    }
}
