package cn.fuck.fishfarming.adapter.control;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import cn.farmFish.service.webserviceApi.bean.CollectorInfo;
import cn.fuck.fishfarming.R;
import cn.fuck.fishfarming.adapter.realdata.RealDataItemAdapter;
import cn.fuck.fishfarming.cache.JsonObjectManager;

/**
 * Created by Administrator on 2016/12/7.
 */

public class RemoteControlExpandAdapter extends BaseExpandableListAdapter {
    private List<CollectorInfo> collectorInfos;
    private Context ctx;

    private  Map<String,String> dicts;

    public RemoteControlExpandAdapter(List<CollectorInfo> collectorInfos, Context ctx){
        this.collectorInfos=collectorInfos;
        this.ctx=ctx;
    }

    @Override
    public int getGroupCount() {
        if(collectorInfos!=null){
            return collectorInfos.size();
        }
        return 0;
    }

    @Override
    public int getChildrenCount(int i) {

        dicts=JsonObjectManager.getMapObject(ctx,collectorInfos.get(i).getDeviceID());

        if(dicts==null){
            return 0;
        }

        return 1;
    }

    @Override
    public CollectorInfo getGroup(int i) {
        if(collectorInfos!=null){
            return collectorInfos.get(i);
        }
        return null;
    }

    @Override
    public Object getChild(int i, int i1) {
        return dicts;
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {

        if(view==null){
            view=LayoutInflater.from(ctx).inflate(R.layout.adapter_expand_control_group,null);
        }
        TextView titleView=ButterKnife.findById(view,R.id.titleView);


        titleView.setText(getGroup(i).getPondName());


        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean isLastChild, View view, ViewGroup viewGroup) {

        if(view==null){
            view=LayoutInflater.from(ctx).inflate(R.layout.adapter_expand_realdata_child,null);
        }

        String deviceID=getGroup(i).getDeviceID();
        ListView listView=ButterKnife.findById(view,R.id.listView);
        ControlItemAdapter adapter=new ControlItemAdapter(dicts,ctx,collectorInfos.get(i).getDeviceElectricsArr(),deviceID);
        listView.setAdapter(adapter);

        int totalHeight=0;
        int row=adapter.getCount();

        View viewItem = adapter.getView(0, null, listView);//这个很重要，那个展开的item的measureHeight比其他的大
        viewItem.measure(0, 0);
        totalHeight = viewItem.getMeasuredHeight()*row+(row-1)*listView.getDividerHeight();
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight;
        listView.setLayoutParams(params);


        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {

        return false;
    }


}
