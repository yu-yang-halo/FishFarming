package com.farmingsocket.client.bean;

/**
 * Created by Administrator on 2017/7/8 0008.
 */

public class URealtem {
    private float value;
    private float max;

    private String itemName;
    private String itemCell;
    public URealtem(){

    }
    public URealtem(float value, float max, String itemName, String itemCell) {
        this.value = value;
        this.max = max;
        this.itemName = itemName;
        this.itemCell = itemCell;
    }

    @Override
    public String toString() {
        return "URealtem{" +
                "value=" + value +
                ", max=" + max +
                ", itemName='" + itemName + '\'' +
                ", itemCell='" + itemCell + '\'' +
                '}';
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public float getMax() {
        return max;
    }

    public void setMax(float max) {
        this.max = max;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemCell() {
        return itemCell;
    }

    public void setItemCell(String itemCell) {
        this.itemCell = itemCell;
    }
}
