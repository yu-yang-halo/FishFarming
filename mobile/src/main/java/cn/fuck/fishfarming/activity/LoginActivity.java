package cn.fuck.fishfarming.activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.farmingsocket.client.WebSocketReqImpl;
import com.farmingsocket.client.bean.BaseInfo;
import com.farmingsocket.manager.ConstantsPool;
import com.farmingsocket.manager.UIManager;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.pgyersdk.update.PgyUpdateManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.fuck.fishfarming.BuildConfig;
import cn.fuck.fishfarming.R;
import cn.fuck.fishfarming.activity.ui.ServerListUi;
import cn.fuck.fishfarming.cache.ContentBox;
import cn.fuck.fishfarming.fir.FirManagerService;
import cn.fuck.fishfarming.permission.PermissionUtils;
import cn.fuck.fishfarming.weather.WeatherHelper;

/**
 * Created by Administrator on 2016/11/27 0027.
 */
public class LoginActivity extends BaseActivity {
    @BindView(R.id.usernamEdit) EditText usernamEdit;
    @BindView(R.id.passwordEdit) EditText passwordEdit;

    @BindView(R.id.button) Button checkBtn;
    @BindView(R.id.button2) Button loginBtn;

    @BindView(R.id.textView3)
    TextView versionLabel;

    @BindView(R.id.textView)
    TextView appNameLabel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.ac_login);
        UIManager.getInstance().addObserver(this);

        ButterKnife.bind(this);


        /**
         * 加载天气数据
         */
        WeatherHelper.downloadWeatheData(this);


        String username=ContentBox.getValueString(this,ContentBox.KEY_USERNAME,"");
        String password=ContentBox.getValueString(this,ContentBox.KEY_PASSWORD,"");


        if(password.equals("")){
            checkBtn.setSelected(false);
        }else{
            checkBtn.setSelected(true);
        }


        usernamEdit.setText(username);
        passwordEdit.setText(password);

        versionLabel.setText("当前版本 v"+FirManagerService.getVersionInfo(this).versionName);



        PgyUpdateManager.setIsForced(false); //设置是否强制更新。true为强制更新；false为不强制更新（默认值）。
        PgyUpdateManager.register(this, BuildConfig.APP_PROVIDER_FIELD);


        if(BuildConfig.APP_TYPE==1){
            appNameLabel.setText(getResources().getString(R.string.appName_shuichan));
        }else if(BuildConfig.APP_TYPE==2){
            appNameLabel.setText(getResources().getString(R.string.appName_dapeng));
        }



        PermissionUtils.requestMultiPermissions(this, new PermissionUtils.PermissionGrant() {
            @Override
            public void onPermissionGranted(int requestCode) {

            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        WebSocketReqImpl.getInstance().logout();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UIManager.getInstance().deleteObserver(this);
        WebSocketReqImpl.getInstance().logout();
    }

    @OnClick(R.id.button) void checkYN(){

        if(checkBtn.isSelected()){
            checkBtn.setSelected(false);

        }else{
            checkBtn.setSelected(true);

        }

        cacheUserPass();

    }
    @OnClick(R.id.btnOption) void toOptionServer()
    {
        Intent intent=new Intent(LoginActivity.this,ServerListUi.class);
        startActivity(intent);
    }
    private void cacheUserPass(){
        String username=usernamEdit.getText().toString();
        String password=passwordEdit.getText().toString();
        if(checkBtn.isSelected()){
            ContentBox.loadString(LoginActivity.this,ContentBox.KEY_USERNAME,username);
            ContentBox.loadString(LoginActivity.this,ContentBox.KEY_PASSWORD,password);
        }else{
            ContentBox.loadString(this,ContentBox.KEY_PASSWORD,null);
        }
    }

    @OnClick(R.id.button2) void login(){

        final String username=usernamEdit.getText().toString();
        final String password=passwordEdit.getText().toString();

        cacheUserPass();


        if(username.trim().equals("")||password.trim().equals("")){
            Toast.makeText(this,"用户名或密码不能为空",Toast.LENGTH_SHORT).show();
        }else{
            hud=KProgressHUD
                    .create(this).setLabel("登录中...")
                    .show();


            String serverAddress = ContentBox.getValueString(LoginActivity.this,ContentBox.KEY_SERVER,"socket.tldwlw.com:8443");
            WebSocketReqImpl.getInstance().login(username,password,serverAddress);

        }

    }

    @Override
    public void update(UIManager o, final Object arg, final int command) {
        super.update(o, arg, command);

        mainHandler.post(new Runnable() {
            @Override
            public void run() {
//                if(hud!=null){
//                    hud.dismiss();
//                }
                if(command== ConstantsPool.COMMAND_LOGIN_INFO){

                    if (arg!=null){
                        BaseInfo baseInfo= (BaseInfo) arg;
                        setBaseInfo(baseInfo);

                        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });

    }
}
