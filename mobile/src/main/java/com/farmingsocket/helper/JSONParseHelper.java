package com.farmingsocket.helper;

import android.util.Log;

import com.farmingsocket.client.bean.BaseInfo;
import com.farmingsocket.manager.ConstantsPool;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.fuck.fishfarming.utils.ConstantUtils;

/**
 * Created by Administrator on 2017/5/7 0007.
 * 解析JSON数据
 */

public class JSONParseHelper {
    private static Map<Integer,Class> classMap=new HashMap<>();
    static {
        classMap.put(ConstantsPool.COMMAND_LOGIN_INFO,BaseInfo.class);
        classMap.put(ConstantsPool.COMMAND_SIWTCH_CONTROL_INFO,BaseInfo.class);
        classMap.put(ConstantsPool.COMMAND_REAL_TIME_DATA,BaseInfo.class);
        classMap.put(ConstantsPool.COMMAND_HISTORY_DATA,BaseInfo.class);
        classMap.put(ConstantsPool.COMMAND_RISKINDEX_NS,BaseInfo.class);
        classMap.put(ConstantsPool.COMMAND_ONLINE_STATUS,BaseInfo.class);
    }

    public static int objectToInt(Object object){
        if(object instanceof Number){
            int value= ((Number) object).intValue();

            return value;
        }

        return 0;
    }

    public static Object parseObject(String text){

        Map  dict=parseMapObject(text);
        int command=objectToInt(dict.get("command"));
        Class clazz=classMap.get(command);

        Gson gson = new Gson();
        Object object = gson.fromJson(text, clazz);


        return object;
    }

    /**
     *
     * @param text
     * @return
     *
     * "command":100 --->  BaseInfo    账户基本信息
     * "command":101 --->              开关控制信息
     * "command":102 --->              实时数据
     * "command":104 --->              历史数据
     * "command":106 --->              风险指数测量与亚硝酸盐含量测量状态
     * "command":109 --->              获取设备在线状态
     */


    private static Map  parseMapObject(String text){
        Gson gson = new Gson();
        Map dict = gson.fromJson(text, Map.class);

        return dict;
    }



}
