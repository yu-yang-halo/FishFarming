package com.farmingsocket.client.bean;

/**
 * Created by Administrator on 2017/9/6.
 * 模式数据结构
 */

public class BaseMode extends BaseCommand {
    private String mac;
    private int model;//1：自动、2：手动、3：时段控制模式
    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public int getModel() {
        return model;
    }

    public void setModel(int model) {
        this.model = model;
    }
}
