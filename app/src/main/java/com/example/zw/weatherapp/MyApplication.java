package com.example.zw.weatherapp;

import android.app.Application;
import android.app.Service;
import android.os.Vibrator;

import com.baidu.apistore.sdk.ApiStoreSDK;
import com.baidu.mapapi.SDKInitializer;

/**
 * Created by zw on 2016/9/12.
 */
public class MyApplication extends Application {
      public LocationService locationService;
    public Vibrator mVibrator;
    private static MyApplication instance;

    @Override
    public void onCreate() {
        ApiStoreSDK.init(this,"f39a5a6438e3e8f259dbe091b43b0e7e");
        locationService =new LocationService(getApplicationContext());
        mVibrator=(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        SDKInitializer.initialize(getApplicationContext());
        instance=this;




        super.onCreate();


    }
    public static MyApplication getInstance(){
        return instance;
    }
}
