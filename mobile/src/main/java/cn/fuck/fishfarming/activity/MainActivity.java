package cn.fuck.fishfarming.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import com.videogo.main.EzvizWebViewActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.fuck.fishfarming.R;
import cn.fuck.fishfarming.adapter.GridItemAdapter;
import cn.fuck.fishfarming.application.MyApplication;
import cn.fuck.fishfarming.utils.NetworkHelper;

import static cn.fuck.fishfarming.activity.TabActivity.KEY_POSITION;

public class MainActivity extends StatusBarActivity {

    @BindView(R.id.gridView) GridView gridView;
    @OnClick(R.id.textView4) void callPhone(){
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "0551-63651196"));

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
        rightBtn.setVisibility(View.GONE);

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

                if(myApp.getBaseInfo()!=null){
                    if(position<4){
                        if(position==1){
                            if(!NetworkHelper.isWifi(MainActivity.this)){
                                showToast("建议在wifi网络下观看视频");
                            }

                            Intent intent=new Intent(MainActivity.this,EzvizWebViewActivity.class);
                            startActivity(intent);
                        }else {
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
        if(myApp.getBaseInfo()==null){
            showToast("数据加载未完成,请重试");
            onStart();
            return false;
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}
