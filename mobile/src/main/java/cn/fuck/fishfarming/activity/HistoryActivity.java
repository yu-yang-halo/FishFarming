package cn.fuck.fishfarming.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ExpandableListView;

import com.farmingsocket.client.WebSocketReqImpl;
import com.farmingsocket.client.bean.BaseHistData;
import com.farmingsocket.manager.ConstantsPool;
import com.farmingsocket.manager.UIManager;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.fuck.fishfarming.R;
import cn.fuck.fishfarming.adapter.history.HistoryExpandAdapter;
import cn.fuck.fishfarming.application.DataHelper;
import cn.fuck.fishfarming.application.MyApplication;

/**
 * Created by Administrator on 2016/12/8.
 */

public class HistoryActivity extends StatusBarActivity{
    @BindView(R.id.expandHistoryListView)
    ExpandableListView expandHistoryListView;

    HistoryExpandAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_history);
        ButterKnife.bind(this);

        initCustomActionBar();

        tvTitle.setText("历史数据");



        adapter=new HistoryExpandAdapter(DataHelper.getMyApp().getBaseDevices(),this);

        expandHistoryListView.setAdapter(adapter);
        expandHistoryListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int i) {
                final String mac=DataHelper.getMyApp().getBaseDevices().get(i).getMac();
                final String gprsMac=DataHelper.getMyApp().getBaseDevices().get(i).getGprsmac();
                if(DataHelper.getMyApp().getHistDatas(mac)==null){
                    DataHelper.getMyApp().showDialogNoTips("数据加载中...");
                }
                if(DataHelper.getDay(mac)<0){
                    DataHelper.setDay(mac,0);
                }
                WebSocketReqImpl.getInstance().fetchHistoryData(mac,gprsMac,DataHelper.getDay(mac));


            }
        });

        expandHistoryListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int i) {

            }
        });


        UIManager.getInstance().addObserver(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        UIManager.getInstance().setCurrentObject(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        UIManager.getInstance().deleteObserver(this);
    }
    @Override
    public void update(UIManager o, final Object arg, final int command) {
        super.update(o, arg, command);
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
               if(command== ConstantsPool.COMMAND_HISTORY_DATA){
                   if(arg!=null){
                       DataHelper.getMyApp().hideDialogNoMessage();
                       BaseHistData baseHistData= (BaseHistData) arg;
                       if(baseHistData==null
                               ||baseHistData.getHistDatas()==null
                               ||baseHistData.getHistDatas().size()<=0){
                           showToast("很抱歉,暂无历史数据");
                       }
                       DataHelper.getMyApp().setHistDatas(baseHistData);

                       adapter.notifyDataSetChanged();
                   }

               }
            }
        });
    }
}
