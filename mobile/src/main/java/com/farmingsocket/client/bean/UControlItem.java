package com.farmingsocket.client.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/7/8 0008.
 */

public class UControlItem {
    private String name;
    @SerializedName("no")
    private String number;
    @SerializedName("val")
    private String status;




    @Override
    public String toString() {
        return "UControlItem{" +
                "name='" + name + '\'' +
                ", number='" + number + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
