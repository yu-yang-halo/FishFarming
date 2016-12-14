package cn.fuck.fishfarming.weather;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import cn.fuck.fishfarming.cache.ContentBox;
import cn.fuck.fishfarming.utils.DateUtils;
import weatherApi.WeatherApi;
import weatherApi.WeatherCallback;
import weatherApi.WeatherJSONBean;

/**
 * Created by Administrator on 2016/12/3 0003.
 */

public class WeatherHelper {
    /**
     *  获取天气辅助类
     **/
    public  static void downloadWeatheData( Context ctx){
        String cityName=ContentBox.getValueString(ctx,ContentBox.KEY_CITYNAME,null);
        downloadWeatheData(ctx,cityName);
    }
    public  static void downloadWeatheData(final Context ctx, String cityName){
        if(cityName==null||cityName.trim().equals("")){
            cityName="北京";
        }
        WeatherApi.getInstance().getWeatherDataByCity(cityName, new WeatherCallback() {
            @Override
            public void onSuccess(String jsonData) {
                System.out.println(jsonData);
                Gson gson=new Gson();
                if(jsonData==null){
                    return;
                }else{
                    WeatherJSONBean bean=gson.fromJson(jsonData,WeatherJSONBean.class);
                    jsonData=gson.toJson(bean);
                }
                List<String> list = null;
                String jsonArr=ContentBox.getValueString(ctx,ContentBox.KEY_WEATHER,null);

                if(jsonArr==null){
                    list=new ArrayList<String>();
                }else{
                    Type type=new TypeToken<List<String>>(){}.getType();
                    list=gson.fromJson(jsonArr,type);
                }


                int existPos=-1;

                for (int i=0;i<list.size();i++){

                    WeatherJSONBean bean=gson.fromJson(list.get(i),WeatherJSONBean.class);
                    String today=DateUtils.createNowDate("yyyy-MM-dd");

                    if(bean.getResult().getData().getRealtime().getDate().equals(today)){
                        existPos=i;
                    }

                }

                if(existPos>=0){
                    Collections.replaceAll(list,list.get(existPos),jsonData);

                }else{
                    if(list.size()<3){
                        list.add(jsonData);
                    }else{
                        list.remove(0);
                        list.add(jsonData);
                    }
                }
                jsonArr=gson.toJson(list);
                ContentBox.loadString(ctx,ContentBox.KEY_WEATHER,jsonArr);
            }

            @Override
            public void onFail(String errorData) {

            }
        });

    }
}
