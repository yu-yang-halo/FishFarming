package cn.farmFish.service.webserviceApi.bean;

import java.util.Arrays;

/**
 * Created by Administrator on 2016/12/7.
 */

public class CollectorInfo {
    private String CustomerNo;
    private int UserType;
    private String CollectorID;
    private String DeviceID;
    private String ProvinceName;
    private String CityName;
    private String OrgName;
    private String FiledID;
    private String PondName;
    private String Electrics;

    private String[] deviceElectricsArr;

    public String getCustomerNo() {
        return CustomerNo;
    }

    public void setCustomerNo(String customerNo) {
        CustomerNo = customerNo;
    }

    public int getUserType() {
        return UserType;
    }

    public void setUserType(int userType) {
        UserType = userType;
    }

    public String getCollectorID() {
        return CollectorID;
    }

    public void setCollectorID(String collectorID) {
        CollectorID = collectorID;
    }

    public String getDeviceID() {
        return DeviceID;
    }

    public void setDeviceID(String deviceID) {
        DeviceID = deviceID;
    }

    public String getProvinceName() {
        return ProvinceName;
    }

    public void setProvinceName(String provinceName) {
        ProvinceName = provinceName;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }

    public String getOrgName() {
        return OrgName;
    }

    public void setOrgName(String orgName) {
        OrgName = orgName;
    }

    public String getFiledID() {
        return FiledID;
    }

    public void setFiledID(String filedID) {
        FiledID = filedID;
    }

    public String getPondName() {
        return PondName;
    }

    public void setPondName(String pondName) {
        PondName = pondName;
    }

    public String getElectrics() {
        return Electrics;
    }

    public void setElectrics(String electrics) {
        Electrics = electrics;
    }

    public String[] getDeviceElectricsArr() {

        if(this.Electrics!=null){
            deviceElectricsArr=this.Electrics.split(",");
        }

        return deviceElectricsArr;
    }

    @Override
    public String toString() {
        return "CollectorInfo{" +
                "CustomerNo='" + CustomerNo + '\'' +
                ", UserType=" + UserType +
                ", CollectorID='" + CollectorID + '\'' +
                ", DeviceID='" + DeviceID + '\'' +
                ", ProvinceName='" + ProvinceName + '\'' +
                ", CityName='" + CityName + '\'' +
                ", OrgName='" + OrgName + '\'' +
                ", FiledID='" + FiledID + '\'' +
                ", PondName='" + PondName + '\'' +
                ", Electrics='" + Electrics + '\'' +
                ", deviceElectricsArr=" + Arrays.toString(deviceElectricsArr) +
                '}';
    }
}
/**
 {"GetCollectorInfoResult":"[{\"CustomerNo\":\"00-00-04-01\",\"UserType\":1,
 \"CollectorID\":\"68eeffe7-9561-4a0f-9a7d-751c4cca98fe\",
 \"DeviceID\":\"00-00-04-01\",
 \"ProvinceName\":\"\",\"CityName\":\"\",\"OrgName\":\"\",
 \"FiledID\":\"4f2ca14a-5a15-47f0-95e2-52746c4abeb7\",
 \"PondName\":\"肥东县程继来家庭农场场地\",\"Electrics\":\"1-增氧机,2-增氧机\"}]"}
 */