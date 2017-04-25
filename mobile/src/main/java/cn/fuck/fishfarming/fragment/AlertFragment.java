package cn.fuck.fishfarming.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.farmFish.service.webserviceApi.WebServiceApi;
import cn.farmFish.service.webserviceApi.WebServiceCallback;
import cn.farmFish.service.webserviceApi.bean.CollectorInfo;
import cn.farmFish.service.webserviceApi.bean.SensorInfo;
import cn.fuck.fishfarming.R;
import cn.fuck.fishfarming.adapter.AlertAdapter;
import cn.fuck.fishfarming.adapter.control.RemoteControlExpandAdapter;
import cn.fuck.fishfarming.application.MyApplication;
import cn.fuck.fishfarming.cache.JsonObjectManager;
import cn.fuck.fishfarming.utils.JSONBeanHelper;
import cn.netty.farmingsocket.SPackage;
import cn.netty.farmingsocket.SocketClientManager;
import cn.netty.farmingsocket.data.DataAnalysisHelper;
import cn.netty.farmingsocket.data.IDataCompleteCallback;

/**
 * Created by Administrator on 2016/12/3 0003.
 */

public class AlertFragment extends Fragment {
    CollectorInfo collectorInfo;
    Handler mainHandler = new Handler(Looper.getMainLooper());
    ListView alertListView;
    TextView tips;
    MyApplication myApp;
    AlertAdapter alertAdapter;

    Set<String> messages=new HashSet<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fr_alert,null);
        tips= (TextView) view.findViewById(R.id.textView13);
        alertListView= (ListView) view.findViewById(R.id.alertListView);

        alertAdapter=new AlertAdapter(getActivity(),null);
        alertListView.setAdapter(alertAdapter);

        myApp= (MyApplication)getActivity().getApplicationContext();

        List<CollectorInfo> collectorInfos= myApp.getCollectorInfos();

        if(collectorInfos!=null&&collectorInfos.size()>0) {
            collectorInfo=collectorInfos.get(0);
            WebServiceApi.getInstance().GetCollecotSensorList(collectorInfo.getCollectorID(), "1", "1", new WebServiceCallback() {
                @Override
                public void onSuccess(String jsonData) {
                    List<SensorInfo> sensorInfos= JSONBeanHelper.convertSensorBean(jsonData);
                    myApp.setSensorInfos(sensorInfos);

                }

                @Override
                public void onFail(String errorData) {
                    System.err.println("onFail result::::::"+errorData);
                }
            });


        }


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){

            if(collectorInfo!=null){
                SocketClientManager.getInstance().beginConnect(collectorInfo.getDeviceID(),new IDataCompleteCallback() {
                    @Override
                    public void onDataComplete(final SPackage spackage) {
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Log.v("analysisData","analysisData : "+spackage);
                                Map<String,String> dict= DataAnalysisHelper.analysisData(spackage);
                                if(dict.size()>0){
                                    Log.v("dict","dict : "+dict);
                                    //{3=PH|7.71|20.0| |3, 1=溶氧|7.07|20.0|mg/L|1, 30=00000000, 6=亚硝酸盐|0.08|20.0|mg/L|5, 5=水温|12.34|20.0|℃|2, 4=氨氮|0.29|20.0|mg/L|4}

                                    JsonObjectManager.cacheMapObjectToLocal(myApp,spackage.getDeviceID(),dict);
                                    List<SensorInfo> sensorInfos=myApp.getSensorInfos();



                                    if(sensorInfos!=null){
                                        for (SensorInfo sensorInfo: sensorInfos){
                                             String realData=dict.get(sensorInfo.getF_ID());
                                             if(realData!=null){
                                                 String[] datas=realData.split("\\|");
                                                 if(datas!=null&&datas.length==5){
                                                      float value=Float.parseFloat(datas[1]);

                                                      if(value>sensorInfo.getF_Upper()){
                                                          //超过上限
                                                          messages.add(datas[0]+"超过上限 [当前值:"+value+"  上限值:"+sensorInfo.getF_Upper()+"]" );
                                                      }else if(value<sensorInfo.getF_Lower()){
                                                          //超过下限
                                                          messages.add(datas[0]+"低于下限 [当前值:"+value+"  下限值:"+sensorInfo.getF_Lower()+"]" );
                                                      }

                                                 }
                                             }
                                        }

                                        if (messages.size()<0){
                                            tips.setVisibility(View.VISIBLE);
                                            alertListView.setVisibility(View.GONE);
                                        }else{
                                            tips.setVisibility(View.GONE);
                                            alertListView.setVisibility(View.VISIBLE);

                                            alertAdapter.setMessages(new ArrayList<String>(messages));

                                            alertAdapter.notifyDataSetChanged();
                                        }
                                    }










                                }
                            }
                        });

                    }
                });

            }
        }else{
            SocketClientManager.getInstance().closeConnect();

        }
    }
}
