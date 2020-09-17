package com.huawei.hms.ads6;

import android.app.Application;

import com.huawei.hms.ads.HwAds;

public class AdsApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        HwAds.init(this);
    }
}
