package cn.fuck.fishfarming.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ExpandableListView;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.fuck.fishfarming.R;
import cn.fuck.fishfarming.adapter.control.RemoteControlExpandAdapter;
import cn.fuck.fishfarming.adapter.history.HistoryExpandAdapter;
import cn.fuck.fishfarming.application.MyApplication;
import cn.fuck.fishfarming.cache.JsonObjectManager;
import cn.netty.farmingsocket.SPackage;
import cn.netty.farmingsocket.SocketClientManager;
import cn.netty.farmingsocket.data.DataAnalysisHelper;
import cn.netty.farmingsocket.data.IDataCompleteCallback;

/**
 * Created by Administrator on 2016/12/8.
 */

public class HistoryActivity extends StatusBarActivity{
    @BindView(R.id.expandHistoryListView)
    ExpandableListView expandHistoryListView;
    Handler nettyHandler = new Handler(Looper.getMainLooper());

    MyApplication myApp;
    HistoryExpandAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_history);
        ButterKnife.bind(this);

        initCustomActionBar();

        tvTitle.setText("历史数据");


        myApp= (MyApplication)getApplicationContext();



        adapter=new HistoryExpandAdapter(myApp.getCollectorInfos(),this);

        expandHistoryListView.setAdapter(adapter);
        expandHistoryListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int i) {

            }
        });

        expandHistoryListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int i) {

            }
        });





    }
}
