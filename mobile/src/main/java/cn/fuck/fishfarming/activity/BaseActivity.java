package cn.fuck.fishfarming.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.farmingsocket.client.bean.BaseCommand;
import com.farmingsocket.client.bean.BaseInfo;
import com.farmingsocket.manager.BaseUIDelegate;
import com.farmingsocket.manager.ConstantsPool;
import com.farmingsocket.manager.ReceiveUI;
import com.farmingsocket.manager.UIManager;
import com.kaopiz.kprogresshud.KProgressHUD;

import cn.fuck.fishfarming.application.MyApplication;
import cn.fuck.fishfarming.fragment.BaseFragment;

/**
 * Created by Administrator on 2017/7/5 0005.
 */

public class BaseActivity extends FragmentActivity implements ReceiveUI {
    protected Handler mainHandler=new Handler(Looper.getMainLooper());
    protected KProgressHUD hud;
    private BaseUIDelegate delegate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        delegate=new BaseUIDelegate(this,this);
    }

    @Override
    public void update(UIManager o, Object arg, int command) {
        if(delegate!=null){
            delegate.update(o,arg,command);
        }
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                if(hud!=null){
                    hud.dismiss();
                }
            }
        });
    }


    protected void setBaseInfo(BaseInfo baseInfo){
        MyApplication myApp= (MyApplication) getApplicationContext();
        myApp.setBaseInfo(baseInfo);
    }
    protected BaseInfo getBaseInfo(){
        MyApplication myApp= (MyApplication) getApplicationContext();
        return myApp.getBaseInfo();
    }
}
