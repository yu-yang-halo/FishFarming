package com.farmingsocket;

import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.farmFish.service.webserviceApi.bean.CollectorInfo;
import cn.fuck.fishfarming.utils.ConstantUtils;

/**
 * Created by Administrator on 2017/5/7 0007.
 */

public class DataAnalysisHelper {


    public  static  CollectorInfo findCollectorInfo(List<CollectorInfo> collectorInfos,String devId){
        CollectorInfo tmp=null;
        for (CollectorInfo collectorInfo:collectorInfos){
            if(collectorInfo.getDeviceID().equals(devId)){
                tmp=collectorInfo;
            }
        }
        return tmp;
    }


    /**

     * 通过byte数组取得float

     *

     * @param b

     * @param index

     *            第几位开始取.

     * @return

     */

    public static float getFloat(byte[] b, int index) {

        int l;

        l = b[index + 3];

        l &= 0xff;

        l |= ((long) b[index + 2] << 8);

        l &= 0xffff;

        l |= ((long) b[index + 1] << 16);

        l &= 0xffffff;

        l |= ((long) b[index + 0] << 24);

        return Float.intBitsToFloat(l);

    }
    public static String getBytesString(byte[] bytes) {

        if(bytes.length<4){
            return "00000000";
        }
        return String.format("%02x%02x%02x%02x",bytes[0],bytes[1],bytes[2],bytes[3]);
    }

    public static Map analysisData(SPackage spackage){

        Map<String,String> dict=new HashMap<String, String>();

        if(spackage.getCmdword()==3){
            if(spackage.getLength()==45){
                //5*8==40  5 1e
                byte[] contents=spackage.getContents();

                for(int i=0;i<spackage.getLength();i=i+5){

                    byte[] floatBaytes=new byte[4];

                    System.arraycopy(contents,i+1,floatBaytes,0,4);

                    float value=getFloat(floatBaytes,0);
                    if(contents[i]==(byte) 0x1e){
                        dict.put(spackage.getDeviceID(),getBytesString(floatBaytes));
                    }else{
                        if(value<=0){
                            continue;
                        }
                        String key=String.valueOf(contents[i]);

                        dict.put(key,
                                String.format("%s|%.2f|%.1f|%s|%d",
                                        ConstantUtils.CONTENTS.get(key),
                                        value,
                                        50.0,
                                        ConstantUtils.UNITS.get(key),ConstantUtils.ORDERS.get(key)));
                    }
                }
            }

        }else if(spackage.getCmdword()==15){
            if(spackage.getLength()==16){
                //0100 0200 0300 0400 0500 0600 0700 0800
                byte[] contents=spackage.getContents();

                StringBuffer sb=new StringBuffer();

                for(int i=0;i<spackage.getLength();i=i+2){
                    sb.append(contents[i+1]+"");
                }
                Log.v("status",sb.toString());

                dict.put(spackage.getDeviceID(),sb.toString());


            }
        }


        return  dict;
    }



}
