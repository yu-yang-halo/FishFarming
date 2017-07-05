package com.farmingsocket.client.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/5 0005.
 */

public class BaseSwitchInfo extends BaseCommand {
    private String mac;

    @SerializedName("switch")
    private List<Map<String,String>> switchs;


    @Override
    public String toString() {
        return "BaseSwitchInfo{" +
                "mac='" + mac + '\'' +
                ", switchs=" + switchs +
                '}';
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public List<Map<String, String>> getSwitchs() {
        return switchs;
    }

    public void setSwitchs(List<Map<String, String>> switchs) {
        this.switchs = switchs;
    }
}
