package cn.fuck.fishfarming.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import cn.farmFish.service.webserviceApi.bean.SensorInfo;
import cn.farmFish.service.webserviceApi.bean.SensorResult;

/**
 * Created by Administrator on 2017/2/6 0006.
 */

public class JSONBeanHelper {
    public static List<SensorInfo> convertSensorBean(String jsonData){

        Gson gson=new Gson();
        SensorResult dict=gson.fromJson(jsonData,SensorResult.class);

        List<SensorInfo> sensorInfos=dict.getSensorList();
        return sensorInfos;
    }
}
