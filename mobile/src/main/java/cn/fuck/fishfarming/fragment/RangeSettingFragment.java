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
import android.widget.Toast;

import com.farmingsocket.SPackage;
import com.farmingsocket.TcpSocketService;
import com.farmingsocket.manager.ConstantsPool;
import com.farmingsocket.manager.ReceiveUI;
import com.farmingsocket.manager.UIManager;

import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import cn.farmFish.service.webserviceApi.bean.CollectorInfo;
import cn.fuck.fishfarming.R;
import cn.fuck.fishfarming.adapter.setting.RangeExpandAdapter;
import cn.fuck.fishfarming.application.MyApplication;


/**
 * Created by Administrator on 2017/2/23 0023.
 */

public class RangeSettingFragment extends Fragment implements ReceiveUI {
    ExpandableListView expandSettingListView;
    MyApplication myApp;
    RangeExpandAdapter adapter;
    List<CollectorInfo> collectorInfos;
    Handler mainHandler = new Handler(Looper.getMainLooper());


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        UIManager.getInstance().addObserver(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        UIManager.getInstance().deleteObserver(this);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    MyApplication myApplication;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View convertView=inflater.inflate(R.layout.fr_alertsetting,null);
        myApplication= (MyApplication) getActivity().getApplication();
        expandSettingListView=ButterKnife.findById(convertView,R.id.expandSettingListView);

        myApp= (MyApplication)getActivity().getApplicationContext();
        collectorInfos=myApp.getCollectorInfos();
        adapter=new RangeExpandAdapter(collectorInfos,getActivity());
        expandSettingListView.setAdapter(adapter);


        expandSettingListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {

                TcpSocketService.getInstance().setDeviceId(collectorInfos.get(groupPosition).getDeviceID());
                TcpSocketService.getInstance().modeStatusSetOrGet(ConstantsPool.MethodType.GET,(short) 0);
                TcpSocketService.getInstance().timeSetOrGet(ConstantsPool.MethodType.GET,(short) 0);
                TcpSocketService.getInstance().rangSetOrGet(ConstantsPool.MethodType.GET,(short) 0,(short) 0);
            }
        });


        return convertView;
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private CollectorInfo findCollectorInfo(String devId){
        CollectorInfo tmp=null;
        for (CollectorInfo collectorInfo:collectorInfos){
            if(collectorInfo.getDeviceID().equals(devId)){
                tmp=collectorInfo;
            }
        }
        return tmp;
    }

    @Override
    public void update(UIManager o, Object arg) {

        if(arg instanceof SPackage){
            final SPackage spackage= (SPackage) arg;
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    if(spackage.getFlag()==0x82){
                        myApplication.hideDialog();
                    }
                    if (spackage.getCmdword()==19){
                        Log.v("max", Arrays.toString( spackage.getRang()));
                        CollectorInfo collectorInfo=findCollectorInfo(spackage.getDeviceID());
                        if(collectorInfo!=null){
                            collectorInfo.setRange(spackage.getRang());
                        }

                        adapter.notifyDataSetChanged();

                        if(spackage.getFlag()==0x82){
                            Toast.makeText(getContext(),"阈值设置成功",Toast.LENGTH_SHORT).show();
                        }

                    }else if (spackage.getCmdword()==21){

                        CollectorInfo collectorInfo=findCollectorInfo(spackage.getDeviceID());
                        if(collectorInfo!=null){
                            collectorInfo.setMode(spackage.getMode());
                        }
                        adapter.notifyDataSetChanged();
                        if(spackage.getFlag()==0x82){
                            Toast.makeText(getContext(),"模式设置成功",Toast.LENGTH_SHORT).show();
                        }


                    }else if(spackage.getCmdword()==24){
                        CollectorInfo collectorInfo=findCollectorInfo(spackage.getDeviceID());
                        if(collectorInfo!=null){
                            collectorInfo.setTime(spackage.getTime());
                        }
                        adapter.notifyDataSetChanged();
                        if(spackage.getFlag()==0x82){
                            Toast.makeText(getContext(),"时间设置成功",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });



        }


    }
}
