package cn.fuck.fishfarming.activity.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.widget.ExpandableListView;

import com.farmingsocket.client.bean.BaseDevice;
import com.farmingsocket.manager.UIManager;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.List;

import butterknife.ButterKnife;
import cn.fuck.fishfarming.R;
import cn.fuck.fishfarming.activity.StatusBarActivity;
import cn.fuck.fishfarming.adapter.setting.SettingExpandAdapter;
import cn.fuck.fishfarming.application.MyApplication;

/**
 * Created by Administrator on 2017/9/6.
 * 预警设置
 */

public class AlertConfigUi extends StatusBarActivity{
    ExpandableListView expandSettingListView;
    MyApplication myApp;
    SettingExpandAdapter adapter;
    List<BaseDevice> collectorInfos;
    Handler mainHandler = new Handler(Looper.getMainLooper());
    int selectParentPos=-1;
    KProgressHUD hud;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fr_alertsetting);
        UIManager.getInstance().addObserver(this);
        initCustomActionBar();
        tvTitle.setText("预警设置");
        expandSettingListView= ButterKnife.findById(this,R.id.expandSettingListView);
        myApp= (MyApplication)getApplicationContext();
        collectorInfos=myApp.getBaseInfo().getDevice();
        adapter=new SettingExpandAdapter(collectorInfos,this);
        expandSettingListView.setAdapter(adapter);

        hud=KProgressHUD
                .create(this).setLabel("数据加载中...");

        expandSettingListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {

                if(groupPosition!=selectParentPos&&selectParentPos>=0){
                    expandSettingListView.collapseGroup(selectParentPos);
                }
                selectParentPos=groupPosition;

                hud.show();

            }
        });
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
}
