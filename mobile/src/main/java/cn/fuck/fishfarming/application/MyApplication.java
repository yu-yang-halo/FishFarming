package cn.fuck.fishfarming.application;


import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.videogo.openapi.EZOpenSDK;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cn.farmFish.service.webserviceApi.bean.CollectorInfo;
import cn.farmFish.service.webserviceApi.bean.SensorInfo;
import cn.farmFish.service.webserviceApi.bean.UserInfo;
import cn.farmFish.service.webserviceApi.bean.VideoInfo;
import cn.fuck.fishfarming.cache.ContentBox;

import im.fir.sdk.FIR;

/**
 * Created by Administrator on 2016/3/20.
 */
public class MyApplication extends Application {
    LocationClient mLocationClient;
    Handler mainHandler=new Handler(Looper.getMainLooper());
    private String customerNo;
    private String userAccount;
    private List<CollectorInfo> collectorInfos;
    private Map<String,String>  realDataDict;
    private List<VideoInfo>     videoInfos;

    private Activity currentActivity;

    private UserInfo loginUserInfo;

    private List<SensorInfo> sensorInfos;

    public List<SensorInfo> getSensorInfos() {
        return sensorInfos;
    }

    public void setSensorInfos(List<SensorInfo> sensorInfos) {
        this.sensorInfos = sensorInfos;
    }

    public void onLowMemory() {
        super.onLowMemory();
    }
    @Override
    public void onTerminate() {
        super.onTerminate();
    }


    public List<VideoInfo> getVideoInfos() {
        return videoInfos;
    }

    public void setVideoInfos(List<VideoInfo> videoInfos) {
        this.videoInfos = videoInfos;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }


    //开发者需要填入自己申请的appkey
    public static String AppKey = "08202da424924e5ea45e88b1b8a61c91";

    public static EZOpenSDK getOpenSDK() {
        return EZOpenSDK.getInstance();
    }

    private void initSDK() {
        {
            /**
             * sdk日志开关，正式发布需要去掉
             */
            EZOpenSDK.showSDKLog(true);

            /**
             * 设置是否支持P2P取流,详见api
             */
            EZOpenSDK.enableP2P(false);

            /**
             * APP_KEY请替换成自己申请的
             */
            EZOpenSDK.initLib(this, AppKey, "");
        }
    }
    @Override
    public void onCreate() {
        super.onCreate();

        enableFIR();
        locationInit();
        initSDK();
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {
                currentActivity=activity;
            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });



    }

    private void enableFIR(){


        FIR.init(this);

        Log.v("weather", ContentBox.getValueString(getApplicationContext(),ContentBox.KEY_WEATHER,""));


    }

    private void locationInit(){
            SDKInitializer.initialize(getApplicationContext());

            mLocationClient = new LocationClient(getApplicationContext());
            mLocationClient.registerLocationListener(new BDLocationListener() {
                @Override
                public void onReceiveLocation(BDLocation bdLocation) {
                    Log.e("BDLocation",bdLocation.getCity());
                    ContentBox.loadString(getApplicationContext(),ContentBox.KEY_CITYNAME,bdLocation.getCity());
                }

                @Override
                public void onConnectHotSpotMessage(String s, int i) {

                }
            });

            LocationClientOption option = new LocationClientOption();
            option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
            );
            option.setCoorType("bd09ll");
            int span=1000;
            option.setScanSpan(span);
            option.setIsNeedAddress(true);
            option.setOpenGps(true);
            option.setLocationNotify(true);
            option.setIsNeedLocationDescribe(true);
            option.setIsNeedLocationPoiList(true);
            option.setIgnoreKillProcess(false);
            option.SetIgnoreCacheException(false);
            option.setEnableSimulateGps(false);
            mLocationClient.setLocOption(option);
            mLocationClient.start();

    }

    public List<CollectorInfo> getCollectorInfos() {
//
//        List<CollectorInfo> collectorInfos2=new ArrayList<>();
//        for (CollectorInfo info : collectorInfos){
//            collectorInfos2.add(info);
//
//            CollectorInfo info2=new CollectorInfo();
//            info2.setUserType(info.getUserType());
//            info2.setPondName(info.getPondName()+"2");
//            info2.setCustomerNo(info.getCustomerNo());
//            info2.setCollectorID(info.getCollectorID());
//            info2.setDeviceID(info.getDeviceID()+"-XX");
//
//            collectorInfos2.add(info2);
//        }

        return collectorInfos;
    }

    public void setCollectorInfos(List<CollectorInfo> collectorInfos) {
        this.collectorInfos = collectorInfos;


    }


    public UserInfo getLoginUserInfo() {
        return loginUserInfo;
    }

    public void setLoginUserInfo(UserInfo loginUserInfo) {
        this.loginUserInfo = loginUserInfo;
    }
    KProgressHUD hud;
    Timer timer;
    public void showDialog(String message){
        hud= KProgressHUD
                .create(currentActivity).setLabel(message).show();

        timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(hud!=null){
                            hud.dismiss();
                            hud=null;
                            Toast.makeText(getApplicationContext(),"发送超时,已取消",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        },8000);
    }

    public void showDialogNoTips(String message){
        hud= KProgressHUD
                .create(currentActivity).setLabel(message).show();

        timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(hud!=null){
                            hud.dismiss();
                            hud=null;
                        }
                    }
                });

            }
        },10000);
    }



    public void hideDialog(){
        if(hud!=null){
            hud.dismiss();
            hud=null;
            timer.cancel();
            Toast.makeText(this,"设置成功",Toast.LENGTH_SHORT).show();
        }
    }
    public void hideDialogNoMessage(){
        if(hud!=null){
            hud.dismiss();
            hud=null;
            timer.cancel();
        }
    }
}
