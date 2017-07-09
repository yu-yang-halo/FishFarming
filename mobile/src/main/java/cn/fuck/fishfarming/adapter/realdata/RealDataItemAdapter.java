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

import com.farmingsocket.client.bean.BaseRealTimeData;
import com.farmingsocket.client.bean.URealtem;
import com.farmingsocket.helper.JSONParseHelper;

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
    private List<URealtem> uRealtems;



    public RealDataItemAdapter(BaseRealTimeData baseRealTimeData, Context ctx){
        this.ctx=ctx;
        uRealtems=JSONParseHelper.convertRealTimeData(baseRealTimeData);
    }
    @Override
    public int getCount() {
        if(uRealtems==null){
            return 0;
        }
        return uRealtems.size();
    }

    @Override
    public Object getItem(int i) {
        if(uRealtems==null){
            return null;
        }
        return uRealtems.get(i);
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

        URealtem   uRealtem  =uRealtems.get(i);

        TextView nameLabel=ButterKnife.findById(view,R.id.textView10);
        TextView valueLabel=ButterKnife.findById(view,R.id.textView11);


        final ClipView containerViewValue=ButterKnife.findById(view,R.id.containerViewValue);



        float currentValue=0;
        float maxValue=20;


        currentValue=uRealtem.getValue();
        maxValue=uRealtem.getMax();

        nameLabel.setText(uRealtem.getItemName());
        valueLabel.setText(String.format("%.2f",currentValue)+" "+uRealtem.getItemCell());

        containerViewValue.setPercentage(currentValue/maxValue);
        containerViewValue.postInvalidate();


        return view;
    }


}
