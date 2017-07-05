package cn.fuck.fishfarming.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.kaopiz.kprogresshud.KProgressHUD;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
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




    }


}
