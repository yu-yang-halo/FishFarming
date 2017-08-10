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

import com.farmingsocket.client.WebSocketReqImpl;
import com.farmingsocket.client.bean.BaseDevice;
import com.farmingsocket.client.bean.BaseHistData;
import com.farmingsocket.helper.JSONParseHelper;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import cn.fuck.fishfarming.R;
import cn.fuck.fishfarming.activity.TabEntity;
import cn.fuck.fishfarming.application.DataHelper;
import cn.fuck.fishfarming.weather.WeatherViewManager;

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


    private List<BaseDevice> collectorInfos;
    private Context ctx;
    private Handler okhttpHandler=new Handler(Looper.getMainLooper());
    KProgressHUD hud;

    public HistoryExpandAdapter(List<BaseDevice> collectorInfos, Context ctx){
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
    public BaseDevice getGroup(int i) {
        if(collectorInfos!=null){
            return collectorInfos.get(i);
        }
        return null;
    }

    @Override
    public BaseHistData getChild(int i, int i1) {
        BaseHistData baseHistData=DataHelper.getMyApp().getHistDatas(collectorInfos.get(i).getMac());
        return baseHistData;
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

        if(getGroup(i)!=null){
            titleView.setText(getGroup(i).getName());
        }else{
            titleView.setText("");
        }




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
        if(collectorInfos.get(i)==null){
            return view;
        }
        final String mac=collectorInfos.get(i).getMac();
        final String gprsMac=collectorInfos.get(i).getGprsmac();
        holder= (ViewHolder) view.getTag();
        final ViewHolder finalHolder = holder;
        WeatherViewManager.initViewData(ctx, finalHolder.weatherView,0);
        holder.mTabLayout_1.setCurrentTab(2-DataHelper.getDay(mac));
        holder.mTabLayout_1.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                Log.v("onTabSelect","onTabSelect "+position);
                DataHelper.getMyApp().showDialogNoTips("数据加载中...");
                DataHelper.setDay(mac,2-position);
                WeatherViewManager.initViewData(ctx, finalHolder.weatherView,2-position);
                WebSocketReqImpl.getInstance().fetchHistoryData(mac,gprsMac,2-position);
            }

            @Override
            public void onTabReselect(int position) {
                Log.v("onTabReselect","onTabReselect "+position);
            }
        });
        final HisItemAdapter hisItemAdapter= (HisItemAdapter) holder.hisListView.getAdapter();

        hisItemAdapter.setDicts(JSONParseHelper.convertWantData(getChild(i,i1)));
        hisItemAdapter.notifyDataSetChanged();

        int len=hisItemAdapter.getCount();
        int totalHeight=0;
        if(len>0){
            ViewGroup.LayoutParams params=holder.hisListView.getLayoutParams();

            View cell=hisItemAdapter.getView(0,null,holder.hisListView);
            cell.measure(0,0);

            totalHeight=cell.getMeasuredHeight()*len;
            params.height=totalHeight;
            holder.hisListView.setLayoutParams(params);

        }

        WeatherViewManager.initViewData(ctx, finalHolder.weatherView,DataHelper.getDay(mac));
        return view;
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
