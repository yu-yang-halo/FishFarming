package cn.fuck.fishfarming.adapter.realdata;

import android.content.Context;
import android.graphics.Outline;
import android.graphics.Rect;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import cn.fuck.fishfarming.R;
import cn.fuck.fishfarming.view.ClipView;

/**
 * Created by Administrator on 2016/12/7.
 */

public class RealDataItemAdapter extends BaseAdapter {
    private Context ctx;
    private Map<String,String> dict;
    private List<String> datas;

    public RealDataItemAdapter(Map<String,String> dict, Context ctx){
        this.ctx=ctx;
        if(dict!=null){
            this.dict=dict;
            this.datas=new ArrayList<String>(dict.values());
            Collections.sort(datas, new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    String[]  arr1=o1.split("\\|");
                    String[]  arr2=o2.split("\\|");
                    if(arr1.length==5&&arr2.length==5){
                        return  Integer.parseInt(arr1[4])-Integer.parseInt(arr2[4]);
                    }
                    return 0;
                }
            });
        }



    }
    @Override
    public int getCount() {
        if(datas==null){
            return 0;
        }
        return datas.size();
    }

    @Override
    public Object getItem(int i) {
        if(datas==null){
            return null;
        }
        return datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(view==null){
            view=LayoutInflater.from(ctx).inflate(R.layout.adapter_realdata_item,null);
        }

        String    value  =datas.get(i);
        String[]  dataArr=value.split("\\|");
        TextView nameLabel=ButterKnife.findById(view,R.id.textView10);
        TextView valueLabel=ButterKnife.findById(view,R.id.textView11);


        final ClipView containerViewValue=ButterKnife.findById(view,R.id.containerViewValue);



        float currnetValue=0;
        float maxValue=20;


        //%s|%f|%f|%s|%d
        if(dataArr.length==5){

            currnetValue=Float.parseFloat(dataArr[1]);
            maxValue=Float.parseFloat(dataArr[2]);

            nameLabel.setText(dataArr[0]);
            valueLabel.setText(dataArr[1]+" "+dataArr[3]);

        }else{
            nameLabel.setText("");
            valueLabel.setText("");
        }

        containerViewValue.setPercentage(currnetValue/maxValue);
        containerViewValue.postInvalidate();








        return view;
    }


}
