package cn.fuck.fishfarming.fragment;

/**
 * Created by Administrator on 2016/11/30 0030.
 */

import android.content.Context;
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
import com.farmingsocket.client.bean.BaseRealTimeData;
import com.farmingsocket.manager.ConstantsPool;
import com.farmingsocket.manager.UIManager;
import com.kaopiz.kprogresshud.KProgressHUD;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.fuck.fishfarming.R;
import cn.fuck.fishfarming.adapter.realdata.RealDataExpandAdapter;
import cn.fuck.fishfarming.application.DataHelper;
import cn.fuck.fishfarming.application.MyApplication;
import cn.fuck.fishfarming.cache.JsonObjectManager;
import cn.fuck.fishfarming.weather.WeatherViewManager;

/**
 * Created by Administrator on 2016/4/1.
 */
public class RealDataFragment extends BaseFragment{

    private static final  String TAG="RealDataFragment";

    @BindView(R.id.expandRealDataListView)
    ExpandableListView expandRealDataListView;
    @BindView(R.id.weatherView)
    View weatherView;
    @BindView(R.id.swipeRefreshLayout)
    PullRefreshLayout swipeRefreshLayout;


    Handler  mainHandler = new Handler(Looper.getMainLooper());
    RealDataExpandAdapter adapter;
    int selectPos=-1;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e(TAG,"onAttach..... ");
        UIManager.getInstance().addObserver(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        UIManager.getInstance().deleteObserver(this);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e(TAG,"onCreateView..... ");
        View view=inflater.inflate(R.layout.fr_realdata, null);
        ButterKnife.bind(this,view);


        WeatherViewManager.initViewData(getActivity(),weatherView,0);


        swipeRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                WebSocketReqImpl.getInstance().fetchDeviceOnlineStatus();
            }
        });

        adapter=new RealDataExpandAdapter(DataHelper.getMyApp().getBaseDevices(),getActivity());


        expandRealDataListView.setAdapter(adapter);
        expandRealDataListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int i) {

                if(selectPos>=0&&selectPos!=i){
                    expandRealDataListView.collapseGroup(selectPos);
                }
                selectPos=i;

                final String mac=DataHelper.getMyApp().getBaseDevices().get(i).getMac();
                Log.v("onGroupExpand","onGroupExpand"+i+"  mac:"+mac);

                if(DataHelper.getMyApp().getRealTimeData(mac)==null){
                    DataHelper.getMyApp().showDialogNoTips("数据加载中...");
                }
                WebSocketReqImpl.getInstance().fetchRealTimeData(mac);


            }
        });
        expandRealDataListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {

            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        WebSocketReqImpl.getInstance().fetchDeviceOnlineStatus();
    }

    @Override
    public void onStop() {
        super.onStop();
        collapseGroupAll();
    }

    private void collapseGroupAll(){
        if(adapter!=null&&expandRealDataListView!=null){
            for (int i=0;i< adapter.getGroupCount();i++){
                expandRealDataListView.collapseGroup(i);
            }
        }
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.e(TAG,"onHiddenChanged "+hidden);
        if(hidden){
            collapseGroupAll();

        }
    }

    @Override
    public void update(UIManager o, final Object arg, final int command) {
        super.update(o, arg, command);

        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
                switch (command){
                    case ConstantsPool.COMMAND_REAL_TIME_DATA:
                        DataHelper.getMyApp().hideDialogNoMessage();
                        if(arg!=null){
                            BaseRealTimeData  baseRealTimeData= (BaseRealTimeData) arg;
                            DataHelper.getMyApp().setRealTimeData(baseRealTimeData);

                            adapter.notifyDataSetChanged();
                        }
                        break;
                    case ConstantsPool.COMMAND_ONLINE_STATUS:
                        if(arg!=null){
                            BaseOnlineData  baseOnlineData= (BaseOnlineData) arg;
                            DataHelper.getMyApp().setOnlineData(baseOnlineData);

                            adapter.notifyDataSetChanged();
                        }
                        break;
                }

            }
        });

    }
}
