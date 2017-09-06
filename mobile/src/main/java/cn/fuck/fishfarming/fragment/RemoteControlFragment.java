package cn.fuck.fishfarming.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.baoyz.widget.PullRefreshLayout;
import com.farmingsocket.client.WebSocketReqImpl;
import com.farmingsocket.client.bean.BaseOnlineData;
import com.farmingsocket.client.bean.BaseSwitchInfo;
import com.farmingsocket.client.bean.UControlItem;
import com.farmingsocket.helper.JSONParseHelper;
import com.farmingsocket.manager.ConstantsPool;
import com.farmingsocket.manager.UIManager;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.List;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.fuck.fishfarming.R;
import cn.fuck.fishfarming.activity.TabActivity;
import cn.fuck.fishfarming.activity.ui.AlertConfigUi;
import cn.fuck.fishfarming.activity.ui.ThresholdConfigUi;
import cn.fuck.fishfarming.adapter.control.RemoteControlExpandAdapter;
import cn.fuck.fishfarming.application.DataHelper;
import cn.fuck.fishfarming.application.MyApplication;
import cn.fuck.fishfarming.cache.JsonObjectManager;
import cn.fuck.fishfarming.weather.WeatherViewManager;

/**
 * Created by Administrator on 2016/12/3 0003.
 */

public class RemoteControlFragment extends BaseFragment {
    public static RemoteControlFragment getRemoteControlFragment(int index){
        RemoteControlFragment fragment=new RemoteControlFragment();
        fragment.index=index;
        return fragment;
    }
    private static final  String TAG="RemoteControlFragment";

    @BindView(R.id.weatherView)
    View weatherView;

    @BindView(R.id.expandControlListView)
    ExpandableListView expandRemoteControlListView;

    @BindView(R.id.swipeRefreshLayout)
    PullRefreshLayout swipeRefreshLayout;
    @OnClick(R.id.btnThresholdConfig) void toThresholdConfigUi(){
        Intent intent=new Intent(getActivity(), ThresholdConfigUi.class);
        getActivity().startActivity(intent);
    }
    TabActivity tabActivity;
    RemoteControlExpandAdapter adapter;
    int selectPos=-1;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e(TAG,"onAttach..............");
        tabActivity= (TabActivity) context;
        UIManager.getInstance().addObserver(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e(TAG,"onDetach..............");
        UIManager.getInstance().deleteObserver(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e(TAG,"onCreateView..............");
        View view=inflater.inflate(R.layout.fr_control, null);
        ButterKnife.bind(this,view);

        WeatherViewManager.initViewData(getActivity(),weatherView,0);


        swipeRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                WebSocketReqImpl.getInstance().fetchDeviceOnlineStatus();
            }
        });

        adapter=new RemoteControlExpandAdapter(DataHelper.getMyApp().getBaseDevices(),getActivity());


        expandRemoteControlListView.setAdapter(adapter);
        expandRemoteControlListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(final int i) {
                if(selectPos>=0&&selectPos!=i){
                    expandRemoteControlListView.collapseGroup(selectPos);
                }
                selectPos=i;
                final String mac=DataHelper.getMyApp().getBaseDevices().get(i).getMac();
                final String gprsMac=DataHelper.getMyApp().getBaseDevices().get(i).getGprsmac();

                if(DataHelper.getMyApp().getDevswitchStatus(mac)==null){
                    DataHelper.getMyApp().showDialogNoTips("数据加载中...");
                }
                WebSocketReqImpl.getInstance().fetchDeviceStatus(mac,gprsMac);

                Log.v("onGroupExpand","onGroupExpand"+i+"  mac:"+mac);


            }
        });

        expandRemoteControlListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {

            }
        });


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        UIManager.getInstance().setCurrentObject(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        collapseGroupAll();
    }
    private void collapseGroupAll(){
        if(adapter!=null&&expandRemoteControlListView!=null){
            for (int i=0;i< adapter.getGroupCount();i++){
                expandRemoteControlListView.collapseGroup(i);
            }
        }
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden){
            collapseGroupAll();
        }else{
            if(tabActivity.currentPos==index){
                WebSocketReqImpl.getInstance().fetchDeviceOnlineStatus();
            }
        }
        Log.e(TAG,"onHiddenChanged.............."+hidden);

    }

    @Override
    public void update(UIManager o, final Object arg, final int command) {
        super.update(o, arg, command);
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
                DataHelper.getMyApp().hideDialogNoMessage();
                switch (command){
                    case ConstantsPool.COMMAND_SIWTCH_CONTROL_INFO:
                        if(arg!=null){
                            BaseSwitchInfo baseSwitchInfo= (BaseSwitchInfo) arg;

                            DataHelper.getMyApp().setDevswitchStatus(baseSwitchInfo);
                            adapter.notifyDataSetChanged();
                        }
                        break;
                    case ConstantsPool.COMMAND_ONLINE_STATUS:
                        if(arg!=null){
                            BaseOnlineData baseOnlineData= (BaseOnlineData) arg;
                            DataHelper.getMyApp().setOnlineData(baseOnlineData);

                            adapter.notifyDataSetChanged();
                        }
                        break;
                }
            }
        });
    }
}
