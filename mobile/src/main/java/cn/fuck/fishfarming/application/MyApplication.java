package cn.fuck.fishfarming.application;


import android.app.Activity;
import android.app.Application;
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
import com.farmingsocket.client.bean.BaseDevice;
import com.farmingsocket.client.bean.BaseHistData;
import com.farmingsocket.client.bean.BaseInfo;
import com.farmingsocket.client.bean.BaseOnlineData;
import com.farmingsocket.client.bean.BaseRealTimeData;
import com.farmingsocket.client.bean.BaseSwitchInfo;
import com.farmingsocket.client.bean.UThresholdItem;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.pgyersdk.crash.PgyCrashManager;
import com.videogo.openapi.EZOpenSDK;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import cn.fuck.fishfarming.cache.ContentBox;



/**
 * Created by Administrator on 2016/3/20.
 */
public class MyApplication extends Application {
    LocationClient mLocationClient;
    Handler mainHandler=new Handler(Looper.getMainLooper());
    private Activity currentActivity;

    private BaseInfo baseInfo;


    public BaseInfo getBaseInfo() {
        return baseInfo;
    }

    public List<BaseDevice> getBaseDevices() {
        if(baseInfo!=null){
            return baseInfo.getDevice();
        }

        return null;
    }

    private Map<String,BaseRealTimeData> realTimeDataCache=new HashMap<>();
    private Map<String,Integer> onlineCache=new HashMap<>();
    private Map<String,BaseSwitchInfo> switchInfoCache=new HashMap<>();
    private Map<String,BaseHistData> histDatasCache=new HashMap<>();
    private Map<String,UThresholdItem> thresholdCache=new HashMap<>();

    public void setHistDatas(BaseHistData baseHistData){
        if(baseHistData!=null){
            histDatasCache.put(baseHistData.getMac(),baseHistData);
        }
    }

    public BaseHistData getHistDatas(String mac){
        if(mac!=null){
            return histDatasCache.get(mac);
        }
        return null;
    }


    public void setRealTimeData(BaseRealTimeData realTimeData){
        if(realTimeData!=null){
            realTimeDataCache.put(realTimeData.getMac(),realTimeData);
        }
    }

    public void setDevswitchStatus(BaseSwitchInfo baseSwitchInfo){
        if(baseSwitchInfo!=null){

            switchInfoCache.put(baseSwitchInfo.getMac(),baseSwitchInfo);
        }
    }

    public BaseSwitchInfo getDevswitchStatus(String mac){
        if(mac!=null){
            return switchInfoCache.get(mac);
        }
        return null;
    }

    public UThresholdItem getUThresholdItem(String mac){
        if(mac!=null){
            return thresholdCache.get(mac);
        }
        return null;
    }
    public void setUThresholdItem(String mac,UThresholdItem item){
        if(mac!=null&&item!=null) {
            thresholdCache.put(mac, item);
        }
    }


    public void setOnlineData(BaseOnlineData baseOnlineData){
        if(baseOnlineData!=null){
            List<BaseDevice> devices=baseOnlineData.getDevice();

            for (BaseDevice dev:devices){
                onlineCache.put(dev.getMac(),dev.getOnline());
            }

        }
    }
    public int getOnlineStatus(String mac){
        if(mac!=null){
            if(onlineCache.get(mac)==null){
                return 0;
            }
            return onlineCache.get(mac);
        }
        return 0;
    }


    public BaseRealTimeData getRealTimeData(String mac){
        if(mac!=null){
           return realTimeDataCache.get(mac);
        }
        return null;
    }



    public void setBaseInfo(BaseInfo baseInfo) {
        this.baseInfo = baseInfo;
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

        DataHelper.init(getApplicationContext());



        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                Log.e("WebSocketReqImpl Crashs","Thread "+t+" error:"+e.getMessage());
                android.os.Process.killProcess(android.os.Process.myPid()) ;
                System.exit(0);
            }
        });

        PgyCrashManager.register(this);

    }



    private  int port;
    public  void setPort(int port){
        this.port=port;
    }

    public  int getPort(){
        return port;
    }

    private void enableFIR(){


        //FIR.init(this);

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


    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }
    public void onLowMemory() {
        super.onLowMemory();
    }
    @Override
    public void onTerminate() {
        super.onTerminate();
    }





}
