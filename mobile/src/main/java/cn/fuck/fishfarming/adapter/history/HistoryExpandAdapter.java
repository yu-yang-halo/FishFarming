package cn.fuck.fishfarming.adapter.history;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import cn.farmFish.service.webserviceApi.WebServiceApi;
import cn.farmFish.service.webserviceApi.WebServiceCallback;
import cn.farmFish.service.webserviceApi.bean.CollWantData;
import cn.farmFish.service.webserviceApi.bean.CollectorData;
import cn.farmFish.service.webserviceApi.bean.CollectorInfo;
import cn.fuck.fishfarming.R;
import cn.fuck.fishfarming.activity.TabEntity;
import cn.fuck.fishfarming.adapter.control.ControlItemAdapter;
import cn.fuck.fishfarming.cache.JsonObjectManager;
import cn.fuck.fishfarming.fragment.MoreFragment;
import cn.fuck.fishfarming.fragment.RealDataFragment;
import cn.fuck.fishfarming.fragment.RemoteControlFragment;
import cn.fuck.fishfarming.utils.ConstantUtils;
import cn.fuck.fishfarming.utils.DateUtils;
import cn.fuck.fishfarming.weather.WeatherViewManager;
import cn.netty.farmingsocket.SocketClientManager;

/**
 * Created by Administrator on 2016/12/7.
 */

public class HistoryExpandAdapter extends BaseExpandableListAdapter {

    private int[] mIconUnselectIds = {
            R.mipmap.tab_realdata0, R.mipmap.tab_video0,
            R.mipmap.tab_control0, R.mipmap.tab_alert0,R.mipmap.tab_more0};
    private int[] mIconSelectIds = {
            R.mipmap.tab_realdata1, R.mipmap.tab_video1,
            R.mipmap.tab_control1, R.mipmap.tab_alert1,R.mipmap.tab_more1};


    private List<CollectorInfo> collectorInfos;
    private Context ctx;
    private Handler okhttpHandler=new Handler(Looper.getMainLooper());
    KProgressHUD hud;

    public HistoryExpandAdapter(List<CollectorInfo> collectorInfos, Context ctx){
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
        return null;
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
    public View getChildView(final int i, int i1, boolean isLastChild, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view==null){
            view=LayoutInflater.from(ctx).inflate(R.layout.adapter_expand_history_child,null);

            holder=new ViewHolder();
            ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
            String[] mTitles=new String[]{"前天","昨天","今天"};
            holder.mTabLayout_1 = ButterKnife.findById(view, R.id.tl_2);
            holder.hisListView = ButterKnife.findById(view, R.id.hisListView);
            holder.weatherView = ButterKnife.findById(view, R.id.weatherView);
            for (int k = 0; k < mTitles.length; k++) {
                mTabEntities.add(new TabEntity(mTitles[k], mIconSelectIds[k], mIconUnselectIds[k]));
            }
            holder.mTabLayout_1.setTabData(mTabEntities);

            holder.hisListView.setAdapter(new HisItemAdapter(ctx,null));

            view.setTag(holder);
        }
        final String deviceId=collectorInfos.get(i).getDeviceID();

        holder= (ViewHolder) view.getTag();
        final ViewHolder finalHolder = holder;

        holder.mTabLayout_1.setCurrentTab(2);
        WeatherViewManager.initViewData(ctx, finalHolder.weatherView,0);

        holder.mTabLayout_1.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                Log.v("onTabSelect","onTabSelect "+position);
                WeatherViewManager.initViewData(ctx, finalHolder.weatherView,2-position);
                loadHistDatas(deviceId,position-2, finalHolder.hisListView);

            }

            @Override
            public void onTabReselect(int position) {
                Log.v("onTabReselect","onTabReselect "+position);
            }
        });

        loadHistDatas(deviceId,0, finalHolder.hisListView);

        return view;
    }

    private void loadHistDatas(String deviceId, int addDays, final ListView listView){
        final HisItemAdapter hisItemAdapter= (HisItemAdapter) listView.getAdapter();

        hud= KProgressHUD
                .create(ctx).setLabel("数据加载中...")
                .show();

        WebServiceApi.getInstance().GetCollectorData(deviceId,
                DateUtils.createNowDate("yyyy-MM-dd", addDays), new WebServiceCallback() {
                    @Override
                    public void onSuccess(final String jsonData) {


                        okhttpHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if(hud!=null){
                                    hud.dismiss();
                                }

                                Log.v("loadHistDatas","onSuccess----"+jsonData);

                                Gson gson=new Gson();
                                Map<String,String> dict=gson.fromJson(jsonData,Map.class);
                                Type type=new TypeToken<List<CollectorData>>(){}.getType();
                                List<CollectorData> results=gson.fromJson(dict.get("GetCollectorDataResult"),type);


                                Map<String,List<CollWantData>> dicts=new HashMap<String, List<CollWantData>>();

                                for (CollectorData collectorData:results){

                                    for(Field field:collectorData.getClass().getDeclaredFields()){

                                        if(field.getName().contains("F_Param")){

                                            String key=field.getName().substring(7);
                                            String realDataVal=null;
                                            field.setAccessible(true);
                                            try {
                                                realDataVal=(String)field.get(collectorData);
                                            } catch (IllegalAccessException e) {
                                                e.printStackTrace();
                                            }
                                            if(realDataVal==null||Float.parseFloat(realDataVal)<=0){
                                                continue;
                                            }


                                            List<CollWantData> values=dicts.get(key);

                                            if(values==null){
                                                values=new ArrayList<CollWantData>();
                                            }

                                            try {

                                                String time=collectorData.getF_ReceivedTime();
                                                String val= (String) field.get(collectorData);


                                                CollWantData data=new CollWantData(Integer.parseInt(time),Float.parseFloat(val));
                                                data.setType(Integer.parseInt(key));
                                                data.setOrder(ConstantUtils.ORDERS.get(key));


                                                values.add(data);
                                            } catch (IllegalAccessException e) {
                                                e.printStackTrace();
                                            }

                                            dicts.put(key,values);

                                        }

                                    }



                                }

                                Log.v("dicts",dicts.toString());

                                hisItemAdapter.setDicts(dicts);
                                hisItemAdapter.notifyDataSetChanged();

                                int len=hisItemAdapter.getCount();
                                int totalHeight=0;
                                if(len>0){
                                    ViewGroup.LayoutParams params=listView.getLayoutParams();

                                    View cell=hisItemAdapter.getView(0,null,listView);
                                    cell.measure(0,0);

                                    totalHeight=cell.getMeasuredHeight()*len;
                                    params.height=totalHeight;
                                    listView.setLayoutParams(params);

                                }

                            }
                        });



                    }

                    @Override
                    public void onFail(final String errorData) {
                        Log.v("loadHistDatas","onFail----"+errorData);
                        okhttpHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if(hud!=null){
                                    hud.dismiss();
                                }
                                Toast.makeText(ctx,errorData,Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {

        return false;
    }


    class ViewHolder{

        CommonTabLayout mTabLayout_1;
        ListView        hisListView;
        View            weatherView;

    }


}
