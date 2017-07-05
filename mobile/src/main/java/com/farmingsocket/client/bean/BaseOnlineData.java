package com.farmingsocket.client.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/5 0005.
 */

public class BaseOnlineData extends BaseCommand{

    private List<BaseDevice> device;

    @Override
    public String toString() {
        return "BaseOnlineData{" +
                "device=" + device +
                '}';
    }

    public List<BaseDevice> getDevice() {
        return device;
    }

    public void setDevice(List<BaseDevice> device) {
        this.device = device;
    }
}
