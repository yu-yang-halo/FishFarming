package cn.fuck.fishfarming.activity.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ExpandableListView;

import com.farmingsocket.client.WebSocketReqImpl;
import com.farmingsocket.client.bean.BaseDevice;
import com.farmingsocket.client.bean.BaseMode;
import com.farmingsocket.client.bean.BaseSwitchInfo;
import com.farmingsocket.client.bean.BaseThreshold;
import com.farmingsocket.client.bean.BaseTimePeriod;
import com.farmingsocket.client.bean.UThresholdItem;
import com.farmingsocket.manager.ConstantsPool;
import com.farmingsocket.manager.UIManager;

import java.util.List;

import butterknife.ButterKnife;
import cn.fuck.fishfarming.R;
import cn.fuck.fishfarming.activity.StatusBarActivity;
import cn.fuck.fishfarming.adapter.setting.RangeExpandAdapter;
import cn.fuck.fishfarming.application.DataHelper;
import cn.fuck.fishfarming.application.MyApplication;

/**
 * Created by Administrator on 2017/9/6.
 * 阈值设置
 */

public class ThresholdConfigUi extends StatusBarActivity {
    ExpandableListView expandSettingListView;
    MyApplication myApp;
    RangeExpandAdapter adapter;
    List<BaseDevice> collectorInfos;
    Handler mainHandler = new Handler(Looper.getMainLooper());
    int selectPos=-1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fr_alertsetting);
        UIManager.getInstance().addObserver(this);
        initCustomActionBar();
        tvTitle.setText("阈值设置");
        expandSettingListView= ButterKnife.findById(this,R.id.expandSettingListView);
        myApp= (MyApplication)getApplicationContext();
        collectorInfos=myApp.getBaseInfo().getDevice();
        adapter=new RangeExpandAdapter(collectorInfos,this);
        expandSettingListView.setAdapter(adapter);


        expandSettingListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                if(selectPos>=0&&selectPos!=groupPosition){
                    expandSettingListView.collapseGroup(selectPos);
                }
                selectPos=groupPosition;

                final String mac= DataHelper.getMyApp().getBaseDevices().get(groupPosition).getMac();
                final String gprsMac=DataHelper.getMyApp().getBaseDevices().get(groupPosition).getGprsmac();

                if(DataHelper.getMyApp().getDevswitchStatus(mac)==null){
                    DataHelper.getMyApp().showDialogNoTips("数据加载中...");
                }
                //获取阈值范围
                WebSocketReqImpl.getInstance().fetchThreshold(mac,gprsMac);
                //获取模式
                WebSocketReqImpl.getInstance().fetchMode(mac,gprsMac);
                //获取时间间隔
                WebSocketReqImpl.getInstance().fetchPeriod(mac);


                Log.v("onGroupExpand","onGroupExpand"+groupPosition+"  mac:"+mac);

            }
        });
        expandSettingListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        UIManager.getInstance().setCurrentObject(this);
    }

    @Override
    public void update(UIManager o, final Object arg, final int command) {
        super.update(o, arg, command);
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                switch (command){
                    case ConstantsPool.COMMAND_THRESHOLD:
                        if(arg!=null){
                            BaseThreshold baseThreshold= (BaseThreshold) arg;
                            UThresholdItem uThresholdItem=DataHelper.getMyApp().getUThresholdItem(baseThreshold.getMac());
                            if(uThresholdItem==null){
                                uThresholdItem=new UThresholdItem();
                            }
                            uThresholdItem.baseThreshold=baseThreshold;
                            DataHelper.getMyApp().setUThresholdItem(baseThreshold.getMac(),uThresholdItem);
                            adapter.notifyDataSetChanged();
                        }
                        break;
                    case ConstantsPool.COMMAND_PERIOD:
                        if(arg!=null) {
                            BaseTimePeriod baseTimePeriod = (BaseTimePeriod) arg;
                            UThresholdItem uThresholdItem = DataHelper.getMyApp().getUThresholdItem(baseTimePeriod.getMac());
                            if (uThresholdItem == null) {
                                uThresholdItem = new UThresholdItem();
                            }
                            uThresholdItem.baseTimePeriod = baseTimePeriod;
                            DataHelper.getMyApp().setUThresholdItem(baseTimePeriod.getMac(), uThresholdItem);
                            adapter.notifyDataSetChanged();
                        }
                        break;
                    case ConstantsPool.COMMAND_MODE:
                        if(arg!=null) {
                            BaseMode baseMode = (BaseMode) arg;
                            UThresholdItem uThresholdItem = DataHelper.getMyApp().getUThresholdItem(baseMode.getMac());
                            if (uThresholdItem == null) {
                                uThresholdItem = new UThresholdItem();
                            }
                            uThresholdItem.baseMode = baseMode;
                            DataHelper.getMyApp().setUThresholdItem(baseMode.getMac(), uThresholdItem);
                            adapter.notifyDataSetChanged();
                        }
                        break;
                }
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        collapseGroupAll();
    }

    private void collapseGroupAll(){
        if(adapter!=null&&expandSettingListView!=null){
            for (int i=0;i< adapter.getGroupCount();i++){
                expandSettingListView.collapseGroup(i);
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        UIManager.getInstance().deleteObserver(this);
    }


}
