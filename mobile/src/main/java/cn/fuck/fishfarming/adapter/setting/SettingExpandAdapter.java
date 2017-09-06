package cn.fuck.fishfarming.adapter.setting;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.farmingsocket.client.bean.BaseDevice;
import com.farmingsocket.client.bean.SensorInfo;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import cn.fuck.fishfarming.R;

/**
 * Created by Administrator on 2017/2/5 0005.
 */

public class SettingExpandAdapter extends BaseExpandableListAdapter {
    private List<BaseDevice> collectorInfos;
    private List<SensorInfo> sensorInfos;
    private Context ctx;
    private KProgressHUD hud;
    private int flags;
    Handler mainUIHandeler=new Handler(Looper.getMainLooper());
    public SettingExpandAdapter(List<BaseDevice> collectorInfos,Context ctx){
        this.ctx=ctx;
        this.collectorInfos=collectorInfos;
    }

    public List<SensorInfo> getSensorInfos() {
        return sensorInfos;
    }

    public void setSensorInfos(List<SensorInfo> sensorInfos) {
        this.sensorInfos = sensorInfos;
    }

    @Override
    public int getGroupCount() {
        if(collectorInfos!=null){

            return collectorInfos.size();
        }
        return 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public BaseDevice getGroup(int groupPosition) {
        if(collectorInfos!=null){
            return collectorInfos.get(groupPosition);
        }
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView= LayoutInflater.from(ctx).inflate(R.layout.adapter_expand_setting_group,null);
        }
        TextView titleView= ButterKnife.findById(convertView,R.id.titleView);

        titleView.setText(getGroup(groupPosition).getName());


        return convertView;
    }

    private void showToastMessage(String message){
        Toast.makeText(ctx,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=LayoutInflater.from(ctx).inflate(R.layout.adapter_expand_setting_child,null);
        }

        ListView listView=ButterKnife.findById(convertView,R.id.listView);

        SettingItemAdapter settingItemAdapter=new SettingItemAdapter(sensorInfos,ctx);
        listView.setAdapter(settingItemAdapter);
        ViewGroup.LayoutParams layoutParams=listView.getLayoutParams();

        if(sensorInfos!=null&&sensorInfos.size()>0){
            View view=settingItemAdapter.getView(0,null,null);
            view.measure(0,0);

            layoutParams.height=view.getMeasuredHeight()*sensorInfos.size()+listView.getDividerHeight()*(sensorInfos.size()-1);
        }

        listView.setLayoutParams(layoutParams);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
