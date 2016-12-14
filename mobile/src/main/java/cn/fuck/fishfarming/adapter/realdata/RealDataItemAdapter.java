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
        dict.remove("30");
        this.dict=dict;
        this.datas=new ArrayList<String>(dict.values());



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


        //%s|%f|%f|%s
        if(dataArr.length==4){

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
