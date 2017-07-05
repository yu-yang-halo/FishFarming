package com.farmingsocket.client.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/5 0005.
 */

public class BaseHistData extends BaseCommand {
   private String mac;
   @SerializedName("array")
   private List<Map> histDatas;

    @Override
    public String toString() {
        return "BaseHistData{" +
                "mac='" + mac + '\'' +
                ", histDatas=" + histDatas +
                '}';
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public List<Map> getHistDatas() {
        return histDatas;
    }

    public void setHistDatas(List<Map> histDatas) {
        this.histDatas = histDatas;
    }
}
