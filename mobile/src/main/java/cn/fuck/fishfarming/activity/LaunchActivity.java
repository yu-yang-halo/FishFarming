package cn.fuck.fishfarming.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import java.util.Timer;
import java.util.TimerTask;

import cn.fuck.fishfarming.R;

/**
 * Created by Administrator on 2016/11/27 0027.
 */
public class LaunchActivity extends FragmentActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_launch);
        Timer timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message msg=new Message();
                msg.what=1000;
                mHandler.sendMessage(msg);
            }
        },3000);
    }

    Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Intent intent=new Intent(LaunchActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
    };

}
