package com.farmingsocket.helper;

import com.farmingsocket.client.bean.BaseCommand;
import com.farmingsocket.client.bean.BaseDevice;
import com.farmingsocket.client.bean.BaseHistData;
import com.farmingsocket.client.bean.BaseInfo;
import com.farmingsocket.client.bean.BaseMode;
import com.farmingsocket.client.bean.BaseOnlineData;
import com.farmingsocket.client.bean.BaseRealTimeData;
import com.farmingsocket.client.bean.BaseSwitchInfo;
import com.farmingsocket.client.bean.BaseThreshold;
import com.farmingsocket.client.bean.BaseTimePeriod;
import com.farmingsocket.client.bean.UCollWantData;
import com.farmingsocket.client.bean.UControlItem;
import com.farmingsocket.client.bean.URealtem;
import com.farmingsocket.manager.ConstantsPool;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.fuck.fishfarming.BuildConfig;
import cn.fuck.fishfarming.application.DataHelper;
import cn.fuck.fishfarming.utils.ConstantUtils;

/**
 * Created by Administrator on 2017/5/7 0007.
 * 解析JSON数据
 */

public class JSONParseHelper {
    private static Map<Integer,Class> classMap=new HashMap<>();
    static {
        classMap.put(ConstantsPool.COMMAND_ERROR,BaseCommand.class);
        classMap.put(ConstantsPool.COMMAND_LOGIN_INFO,BaseInfo.class);
        classMap.put(ConstantsPool.COMMAND_SIWTCH_CONTROL_INFO,BaseSwitchInfo.class);
        classMap.put(ConstantsPool.COMMAND_REAL_TIME_DATA,BaseRealTimeData.class);
        classMap.put(ConstantsPool.COMMAND_HISTORY_DATA,BaseHistData.class);
        classMap.put(ConstantsPool.COMMAND_RISKINDEX_NS,BaseInfo.class);
        classMap.put(ConstantsPool.COMMAND_ONLINE_STATUS,BaseOnlineData.class);
        classMap.put(ConstantsPool.COMMAND_THRESHOLD,BaseThreshold.class);
        classMap.put(ConstantsPool.COMMAND_MODE,BaseMode.class);
        classMap.put(ConstantsPool.COMMAND_PERIOD,BaseTimePeriod.class);

    }
    public static Map<String,Double> objectToMap(Object object){
        if(object instanceof Map){
            return (Map)object;
        }
        return null;
    }
    public static float objectToFloat(Object object){
        if(object==null){
            return 0;
        }
        if(object instanceof Number){
            float value= ((Number) object).floatValue();

            return value;
        }

        return 0;
    }

    public static String objectToString(Object object){
        if(object==null){
            return "";
        }
        return object.toString();
    }

    public static int objectToInt(Object object){
        if(object==null){
            return -1;
        }
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
    private static int getTypeValue(String typeStr){
        if(typeStr.length()<=1){
            return 0;
        }

        String intStr=typeStr.substring(1);

        try {
            return Integer.parseInt(intStr);
        } catch (NumberFormatException e) {
            return 0;
        }

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
    private static float getDateHour(String dateStr) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date;
        try {
            date = simpleDateFormat.parse(dateStr);
        } catch (ParseException e) {

            return 0;
        }

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);
        int hour=calendar.get(Calendar.HOUR_OF_DAY);
        int min=calendar.get(Calendar.MINUTE);
        float floatHour=hour+min/60.0f;


        return floatHour;

    }

    public static Map<String,List<UCollWantData>> convertWantData(BaseHistData baseHistData){
        if(baseHistData==null){
            return null;
        }
        Map<String,List<UCollWantData>> wantDataMap=new HashMap<>();

        List<Map> dicts=baseHistData.getHistDatas();

        for(Map dict:dicts){

            String time= (String) dict.get("time");

            Iterator<Map.Entry> entryIterator=dict.entrySet().iterator();
            while (entryIterator.hasNext()){


                Map.Entry entry=entryIterator.next();
                if(!entry.getKey().equals("time")&&!entry.getKey().equals("mac")){
                    List<UCollWantData> wantDataList=wantDataMap.get(entry.getKey());
                    if(wantDataList==null){
                        wantDataList=new ArrayList<>();
                    }
                    UCollWantData uCollWantData=new UCollWantData(getDateHour(time),objectToFloat(entry.getValue()));

                    uCollWantData.setOrder(ConstantUtils.ORDERS.get(entry.getKey()));
                    uCollWantData.setType(getTypeValue(objectToString(entry.getKey())));

                    wantDataList.add(uCollWantData);
                    wantDataMap.put(objectToString(entry.getKey()),wantDataList);
                }


            }

        }


        return wantDataMap;
    }

    public static List<UControlItem> convertSwitchInfo(BaseSwitchInfo baseSwitchInfo,Map<String,String> stringSwitchgMap){

        if(baseSwitchInfo==null){
            return null;
        }
        List<UControlItem> uControlItemList=baseSwitchInfo.getuControlItems();

        Iterator<UControlItem> itemIterator= uControlItemList.iterator();
        while (itemIterator.hasNext()){
            UControlItem uControlItem=itemIterator.next();
            if(stringSwitchgMap.get(uControlItem.getNumber())==null){
                itemIterator.remove();
            }
        }

        return uControlItemList;
    }



    private static List<URealtem> convertRealTimeDataM1(BaseRealTimeData baseRealTimeData){
        if(baseRealTimeData==null){
            return null;
        }
        List<URealtem> uRealtems=new ArrayList<>();

        Map<String,Object> data=baseRealTimeData.getRealData();

        Iterator<Map.Entry<String,Object>> entryIterator= data.entrySet().iterator();

        while (entryIterator.hasNext()){
            Map.Entry<String,Object> entry=entryIterator.next();
            if(!entry.getKey().equals("time")){
                float value=objectToFloat(entry.getValue());
                if(value<=0){
                    continue;
                }
                String itemName=ConstantUtils.CONTENTS.get(entry.getKey());
                String itemCell=ConstantUtils.UNITS.get(entry.getKey());
                Integer itemValue=ConstantUtils.MAXVALUES.get(entry.getKey());
                float max =0.0f;
                if(itemValue != null)
                {
                    max = itemValue.floatValue();
                }
                if(itemName == null)
                {
                    itemName = "...";
                }
                if(itemCell == null)
                {
                    itemCell = "...";
                }

                URealtem uRealtem=new URealtem(value,max,itemName,itemCell);
                uRealtems.add(uRealtem);
            }

        }
        return uRealtems;
    }

    private static List<URealtem> convertRealTimeDataM2(BaseRealTimeData baseRealTimeData){
        if(baseRealTimeData==null){
            return null;
        }

        List<URealtem> uRealtems=new ArrayList<>();

        Map<String,Object> data=baseRealTimeData.getRealData();

        Iterator<Map.Entry<String,Object>> entryIterator= data.entrySet().iterator();

        while (entryIterator.hasNext()){
            Map.Entry<String,Object> entry=entryIterator.next();
            if(!entry.getKey().equals("time")){
                Map<String,Double> dict=objectToMap(entry.getValue());
                if(dict==null||dict.size()<=0){
                    continue;
                }
                Set<Map.Entry<String,Double>> mSet=  dict.entrySet();

                for (Map.Entry<String,Double> et:mSet){
                    BaseDevice.Sense sense=DataHelper.getMyApp().getSensor(et.getKey());
                    if(sense == null)
                    {
                        continue;
                    }
                    URealtem uRealtem=new URealtem(et.getValue().floatValue(),sense.getMax(),sense.getName(),sense.getDanwei());
                    uRealtems.add(uRealtem);
                }
            }

        }
        return uRealtems;
    }


    public static List<URealtem> convertRealTimeData(BaseRealTimeData baseRealTimeData){

        if(BuildConfig.APP_TYPE==1){
            return convertRealTimeDataM1(baseRealTimeData);
        }else{
            return convertRealTimeDataM2(baseRealTimeData);
        }

    }

}
