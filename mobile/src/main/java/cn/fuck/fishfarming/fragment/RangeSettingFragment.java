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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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


        expandSettingListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                SocketClientManager.getInstance().closeConnect();
            }
        });

        expandSettingListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(final int groupPosition) {

                if(groupPosition!=selectParentPos&&selectParentPos>=0){
                    expandSettingListView.collapseGroup(selectParentPos);
                    SocketClientManager.getInstance().closeConnect();
                }
                selectParentPos=groupPosition;
                SocketClientManager.getInstance().beginConnect( collectorInfos.get(groupPosition).getDeviceID(), new IDataCompleteCallback() {
                    @Override
                    public void onDataComplete(SPackage spackage) {

                        if(spackage.getFlag()==0x82){
                            myApplication.hideDialog();
                        }
                        if (spackage.getCmdword()==19){
                            Log.v("max", Arrays.toString( spackage.getRang()));
                            collectorInfos.get(groupPosition).setRange(spackage.getRang());
                            nettyHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.notifyDataSetChanged();
                                }
                            });

                            if(spackage.getFlag()==0x82){
                                Toast.makeText(getContext(),"阈值设置成功",Toast.LENGTH_SHORT).show();
                            }



                        }else if (spackage.getCmdword()==21){
                            Log.v("Mode", spackage.getMode()==ICmdPackageProtocol.MANUAL_MODE?"手动":"自动");
                            collectorInfos.get(groupPosition).setMode(spackage.getMode());
                            nettyHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.notifyDataSetChanged();
                                }
                            });
                            if(spackage.getFlag()==0x82){
                                Toast.makeText(getContext(),"模式设置成功",Toast.LENGTH_SHORT).show();
                            }


                        }else if(spackage.getCmdword()==24){
                            collectorInfos.get(groupPosition).setTime(spackage.getTime());
                            nettyHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.notifyDataSetChanged();
                                }
                            });
                            if(spackage.getFlag()==0x82){
                                Toast.makeText(getContext(),"时间设置成功",Toast.LENGTH_SHORT).show();
                            }
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
