package com.huawei.hms.ads6;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.huawei.hms.ads.AdListener;
import com.huawei.hms.ads.AdParam;
import com.huawei.hms.ads.BannerAdSize;
import com.huawei.hms.ads.R;
import com.huawei.hms.ads.banner.BannerView;

public class BannerActivity extends AppCompatActivity {

    private static final String TAG = BannerActivity.class.getSimpleName();

    BannerView bannerView;
    View.OnClickListener loadAd;
    Button loadAdButton;
    RadioGroup sizeRadioGroup;
    RadioGroup colorRadioGroup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);

        loadAdButton = findViewById(R.id.loadAdBtn);
        sizeRadioGroup = findViewById(R.id.sizeRadioGroup);
        colorRadioGroup = findViewById(R.id.colorRadioGroup);

        // Создаем вью баннера и добавляем его на layout
        loadAd = view -> {
            FrameLayout adFrame = findViewById(R.id.adFrame);
            if (bannerView != null) {
                adFrame.removeView(bannerView);
                bannerView.destroy();
            }
            bannerView = new BannerView(this);
            adFrame.addView(bannerView);
            bannerView.setAdId(getString(R.string.ad_banner));
            bannerView.setBannerAdSize(getBannerSize(sizeRadioGroup.getCheckedRadioButtonId()));
            bannerView.setBackgroundColor(getColorBackGround(colorRadioGroup.getCheckedRadioButtonId()));
            bannerView.setAdListener(adListener);
            // Загружаем рекламу
            bannerView.loadAd(new AdParam.Builder().build());
        };

        loadAdButton.setOnClickListener(loadAd);
        loadAdButton.performClick();

    }

    private AdListener adListener = new AdListener() {
        @Override
        public void onAdClosed() {
            super.onAdClosed();
            Log.d(TAG, "onAdClosed");
        }

        @Override
        public void onAdFailed(int code) {
            super.onAdFailed(code);
            Log.d(TAG, "onAdFailed");
            Toast.makeText(getApplicationContext(), Utils.getErrorMessage(code), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onAdLeave() {
            super.onAdLeave();
            Log.d(TAG, "onAdLeave");
        }

        @Override
        public void onAdOpened() {
            super.onAdOpened();
            Log.d(TAG, "onAdOpened");
        }

        @Override
        public void onAdLoaded() {
            super.onAdLoaded();
            Log.d(TAG, "onAdLoaded");
        }

        @Override
        public void onAdClicked() {
            super.onAdClicked();
            Log.d(TAG, "onAdClicked");
        }

        @Override
        public void onAdImpression() {
            super.onAdImpression();
            Log.d(TAG, "onAdImpression");
        }
    };

    private BannerAdSize getBannerSize(int id) {
        switch (id) {
            case R.id.size_160_600:
                return BannerAdSize.BANNER_SIZE_160_600;
            case R.id.size_300_250:
                return BannerAdSize.BANNER_SIZE_300_250;
            case R.id.size_320_100:
                return BannerAdSize.BANNER_SIZE_320_100;
            case R.id.size_320_50:
                return BannerAdSize.BANNER_SIZE_320_50;
            case R.id.size_360_57:
                return BannerAdSize.BANNER_SIZE_360_57;
            case R.id.size_360_144:
                return BannerAdSize.BANNER_SIZE_360_144;
            case R.id.size_468_60:
                return BannerAdSize.BANNER_SIZE_468_60;
            case R.id.size_728_90:
                return BannerAdSize.BANNER_SIZE_728_90;
            case R.id.size_smart:
                return BannerAdSize.BANNER_SIZE_SMART;
            case R.id.size_dynamic:
                return BannerAdSize.BANNER_SIZE_DYNAMIC;
            default:
                return BannerAdSize.BANNER_SIZE_320_50;
        }
    }
    private int getColorBackGround(int id) {
        switch (id) {
            case R.id.color_white:
                return Color.WHITE;
            case R.id.color_black:
                return Color.BLACK;
            case R.id.color_red:
                return Color.RED;
            case R.id.color_transparent:
                return Color.TRANSPARENT;
            default:
                return Color.TRANSPARENT;
        }
    }



}
