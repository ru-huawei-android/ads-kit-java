package com.sample.huawei.ads;

import android.app.Application;

import com.huawei.hms.ads.HwAds;

public class AdsApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        HwAds.init(this);
    }
}
