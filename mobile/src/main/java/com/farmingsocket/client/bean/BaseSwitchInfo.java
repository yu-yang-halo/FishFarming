package com.farmingsocket.client.bean;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/5 0005.
 */

public class BaseSwitchInfo extends BaseCommand {
    private String mac;
    @SerializedName("switchs")
    private List<UControlItem> uControlItems;


    @Override
    public String toString() {
        return "BaseSwitchInfo{" +
                "mac='" + mac + '\'' +
                ", switchs=" + uControlItems +
                '}';
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public List<UControlItem> getuControlItems() {
        return uControlItems;
    }

    public void setuControlItems(List<UControlItem> uControlItems) {
        this.uControlItems = uControlItems;
    }
}
