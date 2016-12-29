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

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.fuck.fishfarming.R;
import cn.fuck.fishfarming.adapter.control.RemoteControlExpandAdapter;
import cn.fuck.fishfarming.application.MyApplication;
import cn.fuck.fishfarming.cache.JsonObjectManager;
import cn.netty.farmingsocket.SPackage;
import cn.netty.farmingsocket.SocketClientManager;
import cn.netty.farmingsocket.data.DataAnalysisHelper;
import cn.netty.farmingsocket.data.IDataCompleteCallback;

/**
 * Created by Administrator on 2016/12/3 0003.
 */

public class RemoteControlFragment extends Fragment {

    private static final  String TAG="RemoteControlFragment";

    @BindView(R.id.expandControlListView)
    ExpandableListView expandRemoteControlListView;
    Handler nettyHandler = new Handler(Looper.getMainLooper());

    MyApplication myApp;
    RemoteControlExpandAdapter adapter;
    KProgressHUD hud;
    int parentSelectId=-1;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e(TAG,"onAttach..............");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e(TAG,"onCreateView..............");
        View view=inflater.inflate(R.layout.fr_control, null);
        ButterKnife.bind(this,view);


        myApp= (MyApplication)getActivity().getApplicationContext();



        adapter=new RemoteControlExpandAdapter(myApp.getCollectorInfos(),getActivity());



        expandRemoteControlListView.setAdapter(adapter);
        expandRemoteControlListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int i) {
                if(parentSelectId!=-1){
                    expandRemoteControlListView.collapseGroup(parentSelectId);
                }
                parentSelectId=i;
                final String deviceId=myApp.getCollectorInfos().get(i).getDeviceID();
                Map<String,String> cacheData= JsonObjectManager.getMapObject(getActivity(),deviceId);
                if(cacheData==null||cacheData.size()<=0){
                    hud= KProgressHUD
                            .create(getActivity()).setLabel("数据加载中...")
                            .show();
                }
                Log.v("onGroupExpand","onGroupExpand"+i+"  deviceId:"+deviceId);

                SocketClientManager.getInstance().beginConnect(deviceId,new IDataCompleteCallback() {
                    @Override
                    public void onDataComplete(final SPackage spackage) {


                        nettyHandler.post(new Runnable() {
                            @Override
                            public void run() {

                                Log.v("analysisData","analysisData : "+spackage);
                                Map<String,String> dict= DataAnalysisHelper.analysisData(spackage);
                                if(dict.size()>0){
                                    if(hud!=null){
                                        hud.dismiss();
                                    }

                                    if(dict.size()==1&&dict.keySet().contains("30")){


                                        String statusValue=dict.get("30");

                                        Log.v("control","30 : "+statusValue);
                                        dict= JsonObjectManager.getMapObject(getActivity(),deviceId);
                                        if(dict!=null){
                                            dict.put("30",statusValue);
                                        }
                                        Log.v("control","dict : "+dict);
                                    }
                                    JsonObjectManager.cacheMapObjectToLocal(getActivity(),spackage.getDeviceID(),dict);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        });

                    }
                });
            }
        });

        expandRemoteControlListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int i) {
                Log.v("onGroupCollapse","onGroupCollapse"+i);
                if(parentSelectId==i){
                    parentSelectId=-1;
                }
                SocketClientManager.getInstance().closeConnect();

                nettyHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        if(hud!=null){
                            hud.dismiss();
                        }

                    }
                });
            }
        });




        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        Log.e(TAG,"onStart..............");

    }

    @Override
    public void onStop() {
        super.onStop();
        SocketClientManager.getInstance().closeConnect();
        Log.e(TAG,"onStop..............");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden){
            if(adapter!=null&&expandRemoteControlListView!=null){
                for (int i=0;i< adapter.getGroupCount();i++){
                    expandRemoteControlListView.collapseGroup(i);
                }
            }
        }
        Log.e(TAG,"onHiddenChanged.............."+hidden);

    }
}
