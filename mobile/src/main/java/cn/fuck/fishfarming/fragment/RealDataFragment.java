package cn.fuck.fishfarming.fragment;

/**
 * Created by Administrator on 2016/11/30 0030.
 */

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

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.fuck.fishfarming.R;
import cn.fuck.fishfarming.adapter.realdata.RealDataExpandAdapter;
import cn.fuck.fishfarming.application.MyApplication;
import cn.fuck.fishfarming.cache.JsonObjectManager;
import cn.fuck.fishfarming.weather.WeatherViewManager;
import cn.netty.farmingsocket.SPackage;
import cn.netty.farmingsocket.SocketClientManager;
import cn.netty.farmingsocket.data.DataAnalysisHelper;
import cn.netty.farmingsocket.data.IDataCompleteCallback;

import static cn.fuck.fishfarming.cache.JsonObjectManager.getMapObject;


/**
 * Created by Administrator on 2016/4/1.
 */
public class RealDataFragment extends Fragment {



    @BindView(R.id.expandRealDataListView)
    ExpandableListView expandRealDataListView;
    @BindView(R.id.weatherView)
    View weatherView;

    Handler  nettyHandler = new Handler(Looper.getMainLooper());

    MyApplication myApp;
    RealDataExpandAdapter adapter;
    KProgressHUD hud;
    int parentSelectId=-1;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fr_realdata, null);
        ButterKnife.bind(this,view);

        WeatherViewManager.initViewData(getActivity(),weatherView,0);



        myApp= (MyApplication)getActivity().getApplicationContext();



        adapter=new RealDataExpandAdapter(myApp.getCollectorInfos(),getActivity());


        expandRealDataListView.setAdapter(adapter);
        expandRealDataListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int i) {

                if(parentSelectId!=-1){
                    expandRealDataListView.collapseGroup(parentSelectId);
                }
                parentSelectId=i;


                final String deviceId=myApp.getCollectorInfos().get(i).getDeviceID();
                Log.v("onGroupExpand","onGroupExpand"+i+"  deviceId:"+deviceId);

                Map<String,String> cacheData=JsonObjectManager.getMapObject(getActivity(),deviceId);

                if(cacheData==null||cacheData.size()<=0){

                    hud= KProgressHUD
                            .create(getActivity()).setLabel("数据加载中...")
                            .show();

                }

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
                                    Log.v("dict","dict : "+dict);

                                    JsonObjectManager.cacheMapObjectToLocal(getActivity(),spackage.getDeviceID(),dict);

                                    adapter.notifyDataSetChanged();
                                    Toast.makeText(getActivity(),"实时数据更新成功",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                });
            }
        });

        expandRealDataListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
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


    }

    @Override
    public void onStop() {
        super.onStop();
        SocketClientManager.getInstance().closeConnect();

    }
}
