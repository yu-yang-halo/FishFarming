package com.farmingsocket.client.bean;

/**
 * Created by Administrator on 2017/7/5 0005.
 */

public class SensorInfo {
    public static float INVAILD_DATA=-9999;
    private String F_ID;
    private String F_ParamValue;
    private String F_ParamText;
    private String F_CollectType;
    private String F_Unit;
    private float  F_Upper;
    private float  F_Lower;
    private short  F_IsWarning;
    private String F_CollectTypeName;
    private String F_IsWarningName;
    private short  F_IsChecked;


    private float fixUpper=INVAILD_DATA;
    private float fixLower=INVAILD_DATA;

    public float getFixUpper() {
        return fixUpper;
    }

    public void setFixUpper(float fixUpper) {
        this.fixUpper = fixUpper;
    }

    public float getFixLower() {
        return fixLower;
    }

    public void setFixLower(float fixLower) {
        this.fixLower = fixLower;
    }

    public String getF_ID() {
        return F_ID;
    }

    public void setF_ID(String f_ID) {
        F_ID = f_ID;
    }

    public String getF_ParamValue() {
        return F_ParamValue;
    }

    public void setF_ParamValue(String f_ParamValue) {
        F_ParamValue = f_ParamValue;
    }

    public String getF_ParamText() {
        return F_ParamText;
    }

    public void setF_ParamText(String f_ParamText) {
        F_ParamText = f_ParamText;
    }

    public String getF_CollectType() {
        return F_CollectType;
    }

    public void setF_CollectType(String f_CollectType) {
        F_CollectType = f_CollectType;
    }

    public String getF_Unit() {
        return F_Unit;
    }

    public void setF_Unit(String f_Unit) {
        F_Unit = f_Unit;
    }

    public float getF_Upper() {
        return F_Upper;
    }

    public void setF_Upper(float f_Upper) {
        F_Upper = f_Upper;
    }

    public float getF_Lower() {
        return F_Lower;
    }

    public void setF_Lower(float f_Lower) {
        F_Lower = f_Lower;
    }

    public short getF_IsWarning() {
        return F_IsWarning;
    }

    public void setF_IsWarning(short f_IsWarning) {
        F_IsWarning = f_IsWarning;
    }

    public String getF_CollectTypeName() {
        return F_CollectTypeName;
    }

    public void setF_CollectTypeName(String f_CollectTypeName) {
        F_CollectTypeName = f_CollectTypeName;
    }

    public String getF_IsWarningName() {
        return F_IsWarningName;
    }

    public void setF_IsWarningName(String f_IsWarningName) {
        F_IsWarningName = f_IsWarningName;
    }

    public short getF_IsChecked() {
        return F_IsChecked;
    }

    public void setF_IsChecked(short f_IsChecked) {
        F_IsChecked = f_IsChecked;
    }

    @Override
    public String toString() {
        return "SensorInfo{" +
                "F_ID='" + F_ID + '\'' +
                ", F_ParamValue='" + F_ParamValue + '\'' +
                ", F_ParamText='" + F_ParamText + '\'' +
                ", F_CollectType='" + F_CollectType + '\'' +
                ", F_Unit='" + F_Unit + '\'' +
                ", F_Upper=" + F_Upper +
                ", F_Lower=" + F_Lower +
                ", F_IsWarning=" + F_IsWarning +
                ", F_CollectTypeName='" + F_CollectTypeName + '\'' +
                ", F_IsWarningName='" + F_IsWarningName + '\'' +
                ", F_IsChecked=" + F_IsChecked +
                '}';
    }
}