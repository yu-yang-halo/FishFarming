package cn.fuck.fishfarming.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.farmingsocket.client.bean.BaseInfo;
import com.farmingsocket.manager.ConstantsPool;
import com.farmingsocket.manager.ReceiveUI;
import com.farmingsocket.manager.UIManager;
import com.kaopiz.kprogresshud.KProgressHUD;

import cn.fuck.fishfarming.application.MyApplication;

/**
 * Created by Administrator on 2017/7/5 0005.
 */

public class BaseActivity extends FragmentActivity implements ReceiveUI {
    protected Handler mainHandler=new Handler(Looper.getMainLooper());
    protected KProgressHUD hud;
    @Override
    public void update(UIManager o, final Object arg, final int command) {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {

                if(hud!=null){
                    hud.dismiss();
                }

                switch (command){
                    case ConstantsPool.ERROR_CODE_READ_TIMEOUT:
                        showToast("数据读取超时");
                        break;
                    case ConstantsPool.ERROR_CODE_CONNECT_CLOSED:
                        showToast("网络连接已关闭");
                        break;
                    case ConstantsPool.ERROR_CODE_CONNECT_CLOSING:
                        showToast("网络连接即将关闭");
                        break;
                    case ConstantsPool.ERROR_CODE_CONNECT_FAILURE:
                        showToast("网络连接失败");
                        break;
                    case ConstantsPool.ERROR_CODE_CONNECT_OPEN:
                        showToast("网络连接打开");
                        break;
                    case ConstantsPool.ERROR_CODE_CONNECT_TIMEOUT:
                        showToast("网络连接超时");
                        break;
                }

                if(command>0x1000&&!(BaseActivity.this instanceof LoginActivity)){
                    Intent intent=new Intent();
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.setClass(BaseActivity.this,LoginActivity.class);
                    startActivity(intent);
                }


            }
        });


    }

    protected void showToast(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }


    protected void setBaseInfo(BaseInfo baseInfo){
        MyApplication myApp= (MyApplication) getApplicationContext();
        myApp.setBaseInfo(baseInfo);
    }
    protected BaseInfo getBaseInf(){
        MyApplication myApp= (MyApplication) getApplicationContext();
        return myApp.getBaseInfo();
    }
}
