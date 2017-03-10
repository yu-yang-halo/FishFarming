package cn.fuck.fishfarming.activity;


import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.farmFish.service.webserviceApi.WebServiceApi;
import cn.farmFish.service.webserviceApi.WebServiceCallback;
import cn.farmFish.service.webserviceApi.bean.CollectorInfo;
import cn.farmFish.service.webserviceApi.bean.NewsInfo;
import cn.farmFish.service.webserviceApi.bean.VideoInfo;
import cn.fuck.fishfarming.R;
import cn.fuck.fishfarming.adapter.GridItemAdapter;
import cn.fuck.fishfarming.application.MyApplication;
import cn.fuck.fishfarming.utils.NetworkHelper;

import static cn.fuck.fishfarming.activity.TabActivity.KEY_POSITION;

public class MainActivity extends StatusBarActivity {

    @BindView(R.id.gridView) GridView gridView;
    @OnClick(R.id.textView4) void callPhone(){
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "15156098260"));

        startActivity(intent);
    }
    private MyApplication myApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initCustomActionBar();
        tvTitle.setText("主页");
        rightBtn.setVisibility(View.VISIBLE);
        rightBtn.setText("个人中心");
        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkDataLoadCompleted()){
                    Intent intent=new Intent(MainActivity.this,SettingActivity.class);
                    startActivity(intent);
                }

            }
        });

        myApp= (MyApplication) getApplicationContext();


        String[] titles=new String[]{"实时监测","视频监控","远程控制","超限预警","知识库","历史记录"};

        int[]    images=new int[]{R.mipmap.main_realdata,R.mipmap.main_video,R.mipmap.main_control,
                R.mipmap.main_alert,R.mipmap.main_news,R.mipmap.main_history};

        GridItemAdapter gridItemAdapter=new GridItemAdapter(this,titles,images);
        gridItemAdapter.setGridView(gridView);
        gridView.setAdapter(gridItemAdapter);



        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(myApp.getCollectorInfos()!=null){
                    if(position<4){
                        if(position==1&&myApp.getVideoInfos()==null){
                            showToast("视频数据加载未完成,请重试");
                            onStart();
                        }else {

                            if(position==1){
                                if(!NetworkHelper.isWifi(MainActivity.this)){
                                    showToast("建议在wifi网络下观看视频");
                                }
                            }

                            Intent intent=new Intent(MainActivity.this,TabActivity.class);
                            intent.putExtra(KEY_POSITION,position);
                            startActivity(intent);
                        }

                    }else if(position==4){
                        Intent intent=new Intent(MainActivity.this,NewsActivity.class);
                        startActivity(intent);
                    }else if(position==5){
                        Intent intent=new Intent(MainActivity.this,HistoryActivity.class);
                        startActivity(intent);
                    }

                }else{
                    showToast("数据加载未完成,请重试");
                    onStart();
                }

            }
        });




    }
    private boolean checkDataLoadCompleted(){
        if(myApp.getCollectorInfos()==null){
            showToast("数据加载未完成,请重试");
            onStart();
            return false;
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        WebServiceApi.getInstance().GetUserVideoInfo(myApp.getUserAccount(), new WebServiceCallback() {
            @Override
            public void onSuccess(String jsonData) {
                Log.v("GetUserVideoInfo",jsonData);
                Log.v("jsonData","jsonData : "+jsonData);
                Gson gson=new Gson();
                Map<String,String> dict=gson.fromJson(jsonData,Map.class);

                Type type=new TypeToken<List<VideoInfo>>(){}.getType();
                List<VideoInfo> results=gson.fromJson(dict.get("GetUserVideoInfoResult"),type);


                if(results!=null&&results.size()>0){

                    Collections.sort(results, new Comparator<VideoInfo>() {
                                @Override
                                public int compare(VideoInfo o1, VideoInfo o2) {

                                    return o1.getF_IndexCode() - o2.getF_IndexCode();
                                }
                            });

                    myApp.setVideoInfos(results);

                }

            }

            @Override
            public void onFail(String errorData) {

            }
        });



        WebServiceApi.getInstance().GetCollectorInfo(myApp.getUserAccount(), myApp.getCustomerNo(), new WebServiceCallback() {
            @Override
            public void onSuccess(String jsonData) {
                Log.v("Tag",jsonData);
                Gson gson=new Gson();
                Map<String,String> dict=gson.fromJson(jsonData,Map.class);
                Type type=new TypeToken<List<CollectorInfo>>(){}.getType();
                List<CollectorInfo> results=gson.fromJson(dict.get("GetCollectorInfoResult"),type);
                myApp.setCollectorInfos(results);
            }

            @Override
            public void onFail(String errorData) {
                Log.v("Tag",errorData);
            }
        });
    }
}
