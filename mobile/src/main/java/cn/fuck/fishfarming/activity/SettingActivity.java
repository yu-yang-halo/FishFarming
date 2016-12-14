package cn.fuck.fishfarming.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.fuck.fishfarming.R;
import cn.netty.farmingsocket.SPackage;
import cn.netty.farmingsocket.SocketClientManager;
import cn.netty.farmingsocket.data.ICmdPackageProtocol;
import cn.netty.farmingsocket.data.IDataCompleteCallback;

/**
 * Created by Administrator on 2016/12/10 0010.
 */

public class SettingActivity extends StatusBarActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_settings);
        initCustomActionBar();
        tvTitle.setText("个人中心");

        ButterKnife.bind(this);

        SocketClientManager.getInstance().beginConnect("00-00-04-01", new IDataCompleteCallback() {
            @Override
            public void onDataComplete(SPackage spackage) {

            }
        });



    }
    @OnClick(R.id.button3) void post(){
         SocketClientManager.getInstance().getHandler().rangSetOrGet(ICmdPackageProtocol.MethodType.POST,15,3);
    }
    @OnClick(R.id.button4) void get(){
        SocketClientManager.getInstance().getHandler().rangSetOrGet(ICmdPackageProtocol.MethodType.GET,0,0);
    }
}
