package cn.fuck.fishfarming.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.farmFish.service.webserviceApi.WebServiceApi;
import cn.farmFish.service.webserviceApi.WebServiceCallback;
import cn.farmFish.service.webserviceApi.bean.UserInfo;
import cn.fuck.fishfarming.R;
import cn.fuck.fishfarming.application.MyApplication;
import cn.fuck.fishfarming.cache.ContentBox;
import cn.fuck.fishfarming.fir.FirManagerService;
import cn.fuck.fishfarming.weather.WeatherHelper;

/**
 * Created by Administrator on 2016/11/27 0027.
 */
public class LoginActivity extends FragmentActivity {
    @BindView(R.id.usernamEdit) EditText usernamEdit;
    @BindView(R.id.passwordEdit) EditText passwordEdit;

    @BindView(R.id.button) Button checkBtn;
    @BindView(R.id.button2) Button loginBtn;

    @BindView(R.id.textView3)
    TextView versionLabel;

    Handler  okHttpHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.ac_login);

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



        FirManagerService.checkUpdate(this);


    }

    @OnClick(R.id.button) void checkYN(){

        if(checkBtn.isSelected()){
            checkBtn.setSelected(false);

        }else{
            checkBtn.setSelected(true);

        }

        cacheUserPass();

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
            final KProgressHUD hud=KProgressHUD
                    .create(this).setLabel("登录中...")
                    .show();


            WebServiceApi.getInstance().LoginN(username, password, new WebServiceCallback() {
                @Override
                public void onSuccess(final String jsonData) {

                    okHttpHandler.post(new Runnable() {
                        @Override
                        public void run() {

                            Log.v("jsonData","jsonData : "+jsonData);

                            Gson gson=new Gson();
                            Map<String,String> dict=gson.fromJson(jsonData,Map.class);

                            String result=dict.get("LoginNResult");

                            Type type=new TypeToken<List<UserInfo>>(){}.getType();
                            List<UserInfo> userInfos;
                            try{
                                userInfos=gson.fromJson(result,type);
                            }catch (Exception e){
                                userInfos=null;
                            }


                            if(userInfos==null||userInfos.size()<=0){
                                Toast.makeText(LoginActivity.this,"登录名或密码错误",Toast.LENGTH_SHORT).show();

                            }else{

                                UserInfo userInfo=userInfos.get(0);

                                MyApplication myApp= (MyApplication) getApplicationContext();
                                myApp.setCustomerNo(userInfo.getCustomerNo());
                                myApp.setUserAccount(username);
                                myApp.setLoginUserInfo(userInfo);


                                Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                                startActivity(intent);

                            }

                            hud.dismiss();
                        }
                    });



                }

                @Override
                public void onFail(String errorData) {
                    okHttpHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            hud.dismiss();
                            showToast("网络连接超时,请重试");

                        }
                    });

                }
            });

        }


    }

    protected void showToast(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}
