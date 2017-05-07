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

import com.farmingsocket.DataAnalysisHelper;
import com.farmingsocket.SPackage;
import com.farmingsocket.TcpSocketService;
import com.farmingsocket.manager.ReceiveUI;
import com.farmingsocket.manager.UIManager;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.farmFish.service.webserviceApi.bean.CollectorInfo;
import cn.fuck.fishfarming.R;
import cn.fuck.fishfarming.adapter.control.RemoteControlExpandAdapter;
import cn.fuck.fishfarming.application.MyApplication;
import cn.fuck.fishfarming.cache.JsonObjectManager;

/**
 * Created by Administrator on 2016/12/3 0003.
 */

public class RemoteControlFragment extends Fragment implements ReceiveUI {

    private static final  String TAG="RemoteControlFragment";

    @BindView(R.id.expandControlListView)
    ExpandableListView expandRemoteControlListView;
    Handler mainHandler = new Handler(Looper.getMainLooper());

    MyApplication myApp;
    RemoteControlExpandAdapter adapter;
    KProgressHUD hud;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e(TAG,"onAttach..............");
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


        myApp= (MyApplication)getActivity().getApplicationContext();



        adapter=new RemoteControlExpandAdapter(myApp.getCollectorInfos(),getActivity());



        expandRemoteControlListView.setAdapter(adapter);
        expandRemoteControlListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(final int i) {

                final String deviceId=myApp.getCollectorInfos().get(i).getDeviceID();
                Map<String,String> cacheData= JsonObjectManager.getMapObject(getContext(),deviceId);
                if(cacheData==null||cacheData.size()<=0){
                    hud= KProgressHUD
                            .create(getActivity()).setLabel("数据加载中...")
                            .show();

                }
                Log.v("onGroupExpand","onGroupExpand"+i+"  deviceId:"+deviceId);

                TcpSocketService.getInstance().setDeviceId(deviceId);
                TcpSocketService.getInstance().sendFuckHeart();
            }
        });




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
        if(hidden){
            if(adapter!=null&&expandRemoteControlListView!=null){
                for (int i=0;i< adapter.getGroupCount();i++){
                    expandRemoteControlListView.collapseGroup(i);
                }
            }
        }
        Log.e(TAG,"onHiddenChanged.............."+hidden);

    }

    @Override
    public void update(UIManager o, Object arg) {
        if(arg instanceof SPackage){
            final SPackage spackage= (SPackage) arg;
            mainHandler.post(new Runnable() {
                @Override
                public void run() {

                    if (spackage.getCmdword()==21){
                        CollectorInfo collectorInfo= DataAnalysisHelper.findCollectorInfo( myApp.getCollectorInfos(),spackage.getDeviceID());
                        if(collectorInfo!=null){
                            collectorInfo.setMode(spackage.getMode());
                        }
                    }else{
                        Log.v("analysisData","analysisData : "+spackage);
                        if(hud!=null){
                            hud.dismiss();
                        }
                        if(spackage.getCmdword()==15){
                            MyApplication myApplication= (MyApplication) getContext().getApplicationContext();
                            myApplication.hideDialog();


                        }

                        Map<String,String> dict= DataAnalysisHelper.analysisData(spackage);
                        if(dict.size()>0){

                            if(dict.size()==1&&dict.keySet().contains(spackage.getDeviceID())){
                                String statusValue=dict.get(spackage.getDeviceID());
                                Log.v("control",spackage.getDeviceID()+" : "+statusValue);
                                dict= JsonObjectManager.getMapObject(myApp,spackage.getDeviceID());
                                if(dict!=null){
                                    dict.put(spackage.getDeviceID(),statusValue);
                                }
                                Log.v("control","dict : "+dict);
                            }
                            JsonObjectManager.cacheMapObjectToLocal(myApp,spackage.getDeviceID(),dict);
                            adapter.notifyDataSetChanged();
                        }
                    }


                }
            });

        }
    }
}
