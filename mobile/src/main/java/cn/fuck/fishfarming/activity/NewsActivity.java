package cn.fuck.fishfarming.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.farmFish.service.webserviceApi.WebServiceApi;
import cn.farmFish.service.webserviceApi.bean.NewsInfo;
import cn.farmFish.service.webserviceApi.WebServiceCallback;
import cn.fuck.fishfarming.R;
import cn.fuck.fishfarming.web.WebActivity;


/**
 * Created by Administrator on 2016/12/5.
 */

public class NewsActivity extends StatusBarActivity {
    @BindView(R.id.newsListView)
    ListView newsListView;
    List<Map<String,String>>  datas;
    Handler okHttpHander=new Handler(Looper.getMainLooper());
    KProgressHUD hud;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_news);

        ButterKnife.bind(this);


        initCustomActionBar();

        tvTitle.setText("知识库");



        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if(datas!=null&&datas.size()>0){
                    String url=datas.get(i).get("url");
                    String title=datas.get(i).get("title");
                    Intent intent=new Intent(NewsActivity.this, WebActivity.class);
                    intent.putExtra(WebActivity.KEY_WEB_URL,url);
                    intent.putExtra(WebActivity.KEY_WEB_TITLE,title);
                    startActivity(intent);
                }

            }
        });
        hud=KProgressHUD
                .create(this).setLabel("数据加载中...")
                .show();

        WebServiceApi.getInstance().GetNewsList(2, 10, new WebServiceCallback() {
            @Override
            public void onSuccess(final String jsonData) {
                okHttpHander.post(new Runnable() {
                    @Override
                    public void run() {

                        if(hud!=null){
                            hud.dismiss();
                        }

                        Log.v("jsonData","jsonData "+jsonData);

                        Gson gson=new Gson();
                        Map<String,String> dict=gson.fromJson(jsonData,Map.class);



                        Type type=new TypeToken<List<NewsInfo>>(){}.getType();
                        List<NewsInfo> results=gson.fromJson(dict.get("GetNewsListResult"),type);
                        datas=new ArrayList<Map<String, String>>();
                        if(results!=null){

                            for(NewsInfo newsInfo:results){
                                Map<String,String> map=new HashMap<String, String>();
                                map.put("title",newsInfo.getTitle());
                                map.put("url",newsInfo.getUrl());
                                datas.add(map);

                            }

                        }




                        String[] from = new String[]{"title"};
                        int[]    to   = new int[]{R.id.titleView};
                        SimpleAdapter simpleAdapter=new SimpleAdapter(NewsActivity.this,datas,R.layout.adapter_news,from,to);
                        newsListView.setAdapter(simpleAdapter);





                    }
                });
            }

            @Override
            public void onFail(final String errorData) {
                okHttpHander.post(new Runnable() {
                    @Override
                    public void run() {
                        if(hud!=null){
                            hud.dismiss();
                        }
                        showToast(errorData);

                    }
                });
            }
        });


    }


}
