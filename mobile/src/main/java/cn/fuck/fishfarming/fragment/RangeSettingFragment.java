package cn.fuck.fishfarming.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import cn.farmFish.service.webserviceApi.WebServiceApi;
import cn.farmFish.service.webserviceApi.WebServiceCallback;
import cn.farmFish.service.webserviceApi.bean.CollectorInfo;
import cn.farmFish.service.webserviceApi.bean.SensorInfo;
import cn.fuck.fishfarming.R;
import cn.fuck.fishfarming.adapter.setting.RangeExpandAdapter;
import cn.fuck.fishfarming.adapter.setting.SettingExpandAdapter;
import cn.fuck.fishfarming.application.MyApplication;
import cn.fuck.fishfarming.utils.JSONBeanHelper;
import cn.netty.farmingsocket.SPackage;
import cn.netty.farmingsocket.SocketClientManager;
import cn.netty.farmingsocket.data.ICmdPackageProtocol;
import cn.netty.farmingsocket.data.IDataCompleteCallback;

/**
 * Created by Administrator on 2017/2/23 0023.
 */

public class RangeSettingFragment extends Fragment {
    ExpandableListView expandSettingListView;
    MyApplication myApp;
    RangeExpandAdapter adapter;
    List<CollectorInfo> collectorInfos;
    Handler nettyHandler = new Handler(Looper.getMainLooper());

    int selectParentPos=-1;
    KProgressHUD hud;
    boolean loadOnceMode=false;
    boolean loadOnceRange=false;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View convertView=inflater.inflate(R.layout.fr_alertsetting,null);
        expandSettingListView=ButterKnife.findById(convertView,R.id.expandSettingListView);

        myApp= (MyApplication)getActivity().getApplicationContext();
        collectorInfos=myApp.getCollectorInfos();
        adapter=new RangeExpandAdapter(collectorInfos,getActivity());
        expandSettingListView.setAdapter(adapter);


        expandSettingListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                loadOnceMode=false;
                loadOnceRange=false;
                SocketClientManager.getInstance().closeConnect();
            }
        });

        expandSettingListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(final int groupPosition) {

                if(groupPosition!=selectParentPos&&selectParentPos>=0){
                    loadOnceMode=false;
                    loadOnceRange=false;
                    expandSettingListView.collapseGroup(selectParentPos);
                    SocketClientManager.getInstance().closeConnect();
                }
                selectParentPos=groupPosition;
                SocketClientManager.getInstance().beginConnect( collectorInfos.get(groupPosition).getDeviceID(), new IDataCompleteCallback() {
                    @Override
                    public void onDataComplete(SPackage spackage) {


                        if(!loadOnceRange){

                            if (spackage.getCmdword()==19){
                                Log.v("max", Arrays.toString( spackage.getRang()));
                                collectorInfos.get(groupPosition).setRange(spackage.getRang());
                                nettyHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        adapter.notifyDataSetChanged();
                                    }
                                });
                                loadOnceRange=true;
                            }

                        }
                        if(!loadOnceMode){

                            if (spackage.getCmdword()==21){
                                Log.v("Mode", spackage.getMode()==ICmdPackageProtocol.MANUAL_MODE?"手动":"自动");
                                collectorInfos.get(groupPosition).setMode(spackage.getMode());
                                nettyHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        adapter.notifyDataSetChanged();
                                    }
                                });

                                loadOnceMode=true;
                            }
                        }

                        if(!loadOnceMode){
                            SocketClientManager.getInstance().getHandler().modeStatusSetOrGet(ICmdPackageProtocol.MethodType.GET, ICmdPackageProtocol.MANUAL_MODE, null);
                        }

                        if(!loadOnceRange){
                            SocketClientManager.getInstance().getHandler().rangSetOrGet(ICmdPackageProtocol.MethodType.GET, 0, 0, null);
                        }





                    }
                });

            }
        });


        return convertView;
    }

    @Override
    public void onStop() {
        super.onStop();
        SocketClientManager.getInstance().closeConnect();
    }
}
