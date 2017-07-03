package com.farmingsocket.helper;

import android.util.Log;

import com.farmingsocket.client.bean.BaseInfo;
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
    private static Map<String,Class> classMap=new HashMap<>();
    static {
        classMap.put("100",BaseInfo.class);
        classMap.put("101",BaseInfo.class);
        classMap.put("102",BaseInfo.class);
        classMap.put("104",BaseInfo.class);
        classMap.put("106",BaseInfo.class);
        classMap.put("109",BaseInfo.class);
    }

    public static BaseInfo parseBaseInfo(String text){
        Gson gson = new Gson();
        BaseInfo baseInfo = gson.fromJson(text, BaseInfo.class);


        return baseInfo;
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


    public static Map  parseMapObject(String text){
        Gson gson = new Gson();
        Map dict = gson.fromJson(text, Map.class);

        return dict;
    }



}
