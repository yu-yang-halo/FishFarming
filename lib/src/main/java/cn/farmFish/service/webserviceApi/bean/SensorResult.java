package cn.farmFish.service.webserviceApi.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/2/10 0010.
 */

public class SensorResult {
    private String RstMsg;
    private int RstCode;
    private List<SensorInfo> SensorList;

    public List<SensorInfo> getSensorList() {
        return SensorList;
    }

    public void setSensorList(List<SensorInfo> sensorList) {
        SensorList = sensorList;
    }

    public String getRstMsg() {
        return RstMsg;
    }

    public void setRstMsg(String rstMsg) {
        RstMsg = rstMsg;
    }

    public int getRstCode() {
        return RstCode;
    }

    public void setRstCode(int rstCode) {
        RstCode = rstCode;
    }
}
