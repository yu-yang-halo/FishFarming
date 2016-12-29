package cn.fuck.fishfarming.application;


import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.os.Bundle;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;

import java.util.List;
import java.util.Map;

import cn.farmFish.service.webserviceApi.bean.CollectorInfo;
import cn.farmFish.service.webserviceApi.bean.VideoInfo;
import cn.fuck.fishfarming.R;
import cn.fuck.fishfarming.cache.ContentBox;
import im.fir.sdk.FIR;

/**
 * Created by Administrator on 2016/3/20.
 */
public class MyApplication extends Application {
    LocationClient mLocationClient;

    private String customerNo;
    private String userAccount;
    private List<CollectorInfo> collectorInfos;
    private Map<String,String>  realDataDict;
    private List<VideoInfo>     videoInfos;


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

    @Override
    public void onCreate() {
        super.onCreate();

        enableFIR();
        locationInit();

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

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
        return collectorInfos;
    }

    public void setCollectorInfos(List<CollectorInfo> collectorInfos) {
        this.collectorInfos = collectorInfos;
    }



}
