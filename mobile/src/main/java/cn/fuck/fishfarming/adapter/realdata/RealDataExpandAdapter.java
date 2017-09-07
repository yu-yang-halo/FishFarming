package cn.fuck.fishfarming.adapter.realdata;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.farmingsocket.client.bean.BaseDevice;
import com.farmingsocket.client.bean.BaseRealTimeData;

import java.util.List;
import java.util.Map;
import butterknife.ButterKnife;
import cn.fuck.fishfarming.R;
import cn.fuck.fishfarming.application.DataHelper;
import cn.fuck.fishfarming.cache.JsonObjectManager;

/**
 * Created by Administrator on 2016/12/7.
 */

public class RealDataExpandAdapter extends BaseExpandableListAdapter {
    private List<BaseDevice> collectorInfos;
    private Context ctx;

    public RealDataExpandAdapter(List<BaseDevice> collectorInfos, Context ctx){
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

        BaseRealTimeData baseRealTimeData=DataHelper.getMyApp().getRealTimeData(collectorInfos.get(i).getMac());


        if(baseRealTimeData==null){
            return 0;
        }

        return 1;
    }

    @Override
    public BaseDevice getGroup(int i) {
        if(collectorInfos!=null){
            return collectorInfos.get(i);
        }
        return null;
    }

    @Override
    public BaseRealTimeData getChild(int i, int i1) {
        BaseRealTimeData baseRealTimeData=DataHelper.getMyApp().getRealTimeData(collectorInfos.get(i).getMac());


        return baseRealTimeData;
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
            view=LayoutInflater.from(ctx).inflate(R.layout.adapter_expand_realdata_group,null);
        }
        TextView titleView=ButterKnife.findById(view,R.id.titleView);
        TextView onlineView=ButterKnife.findById(view,R.id.statusView);


        titleView.setText(getGroup(i).getName());
        int status=DataHelper.getMyApp().getOnlineStatus(collectorInfos.get(i).getMac());

        if(status==1){
            onlineView.setText("在线");
            onlineView.setTextColor(Color.parseColor("#00ff00"));
        }else{
            onlineView.setText("离线");
            onlineView.setTextColor(Color.parseColor("#ff0000"));
        }


        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean isLastChild, View view, ViewGroup viewGroup) {

        if(view==null){
            view=LayoutInflater.from(ctx).inflate(R.layout.adapter_expand_realdata_child,null);
        }
        ListView listView=ButterKnife.findById(view,R.id.listView);
        RealDataItemAdapter adapter=new RealDataItemAdapter(getChild(i,i1),ctx);
        listView.setAdapter(adapter);

        int totalHeight=0;
        int row=adapter.getCount();
        if(row>0){
            View viewItem = adapter.getView(0, null, listView);//这个很重要，那个展开的item的measureHeight比其他的大
            viewItem.measure(0, 0);
            totalHeight = viewItem.getMeasuredHeight()*row+(row-1)*listView.getDividerHeight();
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight;
            listView.setLayoutParams(params);
        }
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {

        return false;
    }


}
