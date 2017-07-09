package com.farmingsocket.client.bean;

/**
 * Created by Administrator on 2017/7/8 0008.
 */

public class UControlItem {
    private String name;
    private String number;
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
