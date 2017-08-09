package cn.fuck.fishfarming.weather;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;

import butterknife.ButterKnife;
import cn.fuck.fishfarming.R;
import cn.fuck.fishfarming.cache.ContentBox;
import weatherApi.WeatherJSONBean;

/**
 * Created by Administrator on 2016/12/3 0003.
 */

public class WeatherViewManager {
    private static String[] weeks=new String[]{"星期日","星期一","星期二","星期三","星期四","星期五","星期六"};
    public static void initViewData(Context ctx,View view,int mixDay){

        ViewHolder holder=null;

        if(view.getTag()==null){
            holder=new ViewHolder();
            holder.imageView=ButterKnife.findById(view, R.id.imageView9);
            holder.weatherText=ButterKnife.findById(view, R.id.textView6);
            holder.dateText=ButterKnife.findById(view, R.id.textView8);
            holder.cityText=ButterKnife.findById(view, R.id.textView9);

            view.setTag(holder);
        }else{
            holder= (ViewHolder) view.getTag();
        }

        WeatherJSONBean bean=getTodayWeather(ctx,mixDay);
        if(bean!=null&&bean.getResult()!=null&&
                bean.getResult().getData()!=null
                &&bean.getResult().getData().getRealtime()!=null
                &&bean.getResult().getData().getRealtime().getWeather()!=null){

            holder.cityText.setText(bean.getResult().getData().getRealtime().getCity_name());
            String temp=bean.getResult().getData().getRealtime().getWeather().getTemperature();
            String info=bean.getResult().getData().getRealtime().getWeather().getInfo();
            String imageTag=bean.getResult().getData().getRealtime().getWeather().getImg();

            int week= bean.getResult().getData().getRealtime().getWeek();

            String weekDesc="";
            if(week<weeks.length){
                weekDesc=weeks[week];
            }
            holder.weatherText.setText(temp+"℃  "+info);
            holder.dateText.setText(bean.getResult().getData().getRealtime().getDate()+" "+weekDesc);

            int resId=getImageResource(ctx,imageTag);
            if(resId>0){
                holder.imageView.setImageResource(resId);
            }


        }



    }

    private static int getImageResource(Context ctx,String imageTag){
        int tag=Integer.parseInt(imageTag);
        if(tag<10){
            imageTag="0"+tag;
        }

        imageTag="day"+imageTag;

        int resId = ctx.getResources().getIdentifier(imageTag, "mipmap", ctx.getPackageName());
        //如果没有在"mipmap"下找到imageName,将会返回0

        return resId;


    }
    //mixDay 0--today  1--yestday 2--yestday yestday
    //
    private static WeatherJSONBean getTodayWeather(Context ctx,int mixDay){
        WeatherJSONBean bean=null;
        String jsonArr=ContentBox.getValueString(ctx,ContentBox.KEY_WEATHER,null);
        if(jsonArr!=null){

            Gson gson=new Gson();
            Type type=new TypeToken<List<String>>(){}.getType();
            List<String> list=gson.fromJson(jsonArr,type);
            if(list.size()>mixDay){
                String jsonObj=list.get(list.size()-1-mixDay);
                bean=gson.fromJson(jsonObj, WeatherJSONBean.class);
            }else if(list.size()>0){
                String jsonObj=list.get(list.size()-1);
                bean=gson.fromJson(jsonObj, WeatherJSONBean.class);
            }
        }

        return bean;
    }

    public static class ViewHolder{
        ImageView imageView;
        TextView weatherText;
        TextView dateText;
        TextView cityText;
    }
}
