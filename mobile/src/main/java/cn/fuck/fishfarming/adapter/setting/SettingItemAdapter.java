package cn.fuck.fishfarming.adapter.setting;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.appyvet.rangebar.RangeBar;


import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import cn.farmFish.service.webserviceApi.bean.SensorInfo;
import cn.fuck.fishfarming.R;

/**
 * Created by Administrator on 2017/2/10 0010.
 */

public class SettingItemAdapter extends BaseAdapter{
    private  List<SensorInfo> sensorInfos;
    private  Context ctx;

    public SettingItemAdapter(List<SensorInfo> sensorInfos, Context ctx){
        if(sensorInfos!=null){
            Iterator<SensorInfo> iterator=sensorInfos.iterator();

            while (iterator.hasNext()){
                SensorInfo sensorInfo=iterator.next();
                if(sensorInfo.getF_IsChecked()==0){
                    iterator.remove();
                }
            }
        }

        this.sensorInfos=sensorInfos;
        this.ctx=ctx;
    }
    @Override
    public int getCount() {
        if(sensorInfos!=null){
            return sensorInfos.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(sensorInfos!=null){
            return sensorInfos.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=LayoutInflater.from(ctx).inflate(R.layout.adapter_expand_setting_child_item,null);
        }
        TextView titleView= (TextView) convertView.findViewById(R.id.textView14);

        EditText minEdit= (EditText) convertView.findViewById(R.id.min_edit);
        EditText maxEdit= (EditText) convertView.findViewById(R.id.max_edit);

        titleView.setText(sensorInfos.get(position).getF_ParamText());

        Log.e("max-min",(int)sensorInfos.get(position).getF_Upper()+" "+(int)sensorInfos.get(position).getF_Lower());

        maxEdit.setText(sensorInfos.get(position).getF_Upper()+"");
        minEdit.setText(sensorInfos.get(position).getF_Lower()+"");



        minEdit.addTextChangedListener(new TextWatcherImpl(position,false));
        maxEdit.addTextChangedListener(new TextWatcherImpl(position,true));







        return convertView;
    }

    public  class TextWatcherImpl implements TextWatcher{
        private int pos;
        private boolean isMax;
        public TextWatcherImpl(int pos,boolean isMax){
            this.pos=pos;
            this.isMax=isMax;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            Log.e("beforeTextChanged","s "+s +" start "+start +" count "+count+" after "+after);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Log.e("onTextChanged","s "+s +" start "+start +" count "+count+" before "+before);
        }

        @Override
        public void afterTextChanged(Editable s) {
            Log.e("afterTextChanged","s "+s);
            if(s.toString().isEmpty()){
                return;
            }
            float val= Float.parseFloat(s.toString());

            if(isMax){
                sensorInfos.get(pos).setFixUpper(val);
            }else{
                sensorInfos.get(pos).setFixLower(val);
            }


        }
    }
}
