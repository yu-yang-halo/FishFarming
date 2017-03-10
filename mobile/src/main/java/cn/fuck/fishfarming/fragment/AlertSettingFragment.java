package cn.fuck.fishfarming.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.farmFish.service.webserviceApi.WebServiceApi;
import cn.farmFish.service.webserviceApi.WebServiceCallback;
import cn.farmFish.service.webserviceApi.bean.CollectorInfo;
import cn.farmFish.service.webserviceApi.bean.SensorInfo;
import cn.fuck.fishfarming.R;
import cn.fuck.fishfarming.adapter.setting.SettingExpandAdapter;
import cn.fuck.fishfarming.application.MyApplication;
import cn.fuck.fishfarming.utils.JSONBeanHelper;

/**
 * Created by Administrator on 2017/2/23 0023.
 */

public class AlertSettingFragment extends Fragment {
    ExpandableListView expandSettingListView;
    MyApplication myApp;
    SettingExpandAdapter adapter;
    List<CollectorInfo> collectorInfos;
    Handler nettyHandler = new Handler(Looper.getMainLooper());

    int selectParentPos=-1;
    KProgressHUD hud;
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
        adapter=new SettingExpandAdapter(collectorInfos,getActivity());
        expandSettingListView.setAdapter(adapter);

        hud=KProgressHUD
                .create(getActivity()).setLabel("数据加载中...");

        expandSettingListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {

                if(groupPosition!=selectParentPos&&selectParentPos>=0){
                    expandSettingListView.collapseGroup(selectParentPos);
                }
                selectParentPos=groupPosition;

                hud.show();


                WebServiceApi.getInstance().GetCollecotSensorList(collectorInfos.get(groupPosition).getCollectorID(), "1", "1", new WebServiceCallback() {
                    @Override
                    public void onSuccess(String jsonData) {
                        final List<SensorInfo> sensorInfos= JSONBeanHelper.convertSensorBean(jsonData);

                        System.err.println("onSuccess result::::::"+sensorInfos);
                        nettyHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                hud.dismiss();
                                adapter.setSensorInfos(sensorInfos);
                                adapter.notifyDataSetChanged();

                            }
                        });
                    }

                    @Override
                    public void onFail(String errorData) {
                        System.err.println("onFail result::::::"+errorData);
                    }
                });
            }
        });


        return convertView;
    }





}
