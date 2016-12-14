package cn.fuck.fishfarming.cache;

import android.content.Context;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/7.
 */

public class JsonObjectManager {

    public static Map<String,String> getMapObject(Context ctx,String deviceID){
        String jsonStr=ContentBox.getValueString(ctx,deviceID,null);
        if(jsonStr==null){
            return null;
        }

        Gson gson=new Gson();
        Map<String,String> dict=gson.fromJson(jsonStr,Map.class);
        return dict;
    }


    public static void cacheMapObjectToLocal(Context ctx,String deviceID,Map<String,String> dict){
        Gson gson=new Gson();
        String jsonStr=gson.toJson(dict);
        ContentBox.loadString(ctx,deviceID,jsonStr);
    }





}
