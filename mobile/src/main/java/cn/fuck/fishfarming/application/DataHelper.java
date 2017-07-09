package cn.fuck.fishfarming.application;

import android.content.Context;

import com.farmingsocket.client.bean.BaseRealTimeData;
import com.farmingsocket.client.bean.URealtem;
import com.farmingsocket.helper.JSONParseHelper;
import com.farmingsocket.manager.ConstantsPool;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.fuck.fishfarming.utils.ConstantUtils;

/**
 * Created by Administrator on 2017/7/8 0008.
 */

public class DataHelper {
    private static MyApplication myApp;

    public static void init(Context ctx){
        myApp= (MyApplication) ctx.getApplicationContext();
    }

    public static MyApplication getMyApp() {
        return myApp;
    }


    private static  Map<String,Integer> indexCache=new HashMap<>();


    public static int getDay(String mac) {
        if(mac==null){
            return 0;
        }

        return  JSONParseHelper.objectToInt(indexCache.get(mac));
    }

    public static void setDay(String mac,int day) {
        indexCache.put(mac,day);
    }
}
