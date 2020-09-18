package com.sample.huawei.ads;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.huawei.hms.ads.AdListener;
import com.huawei.hms.ads.AdParam;
import com.huawei.hms.ads.R;
import com.huawei.hms.ads.VideoOperator;
import com.huawei.hms.ads.nativead.NativeAd;
import com.huawei.hms.ads.nativead.NativeAdConfiguration;
import com.huawei.hms.ads.nativead.NativeAdLoader;
import com.huawei.hms.ads.nativead.NativeView;

public class NativeActivity extends AppCompatActivity {

    private static final String TAG = NativeActivity.class.getSimpleName();

    private NativeAd nativeAd;
    private View.OnClickListener loadAd;
    private RadioGroup typeRadioGroup;
    private Button loadAdBtn;
    private ScrollView nativeAdScrollView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native);

        typeRadioGroup = findViewById(R.id.typeRadioGroup);
        loadAdBtn = findViewById(R.id.loadAdBtn);
        nativeAdScrollView = findViewById(R.id.nativeAdScrollView);
        loadAd = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // NativeAdConfiguration.Builder используется для настроек рекламы
                // подробности: https://developer.huawei.com/consumer/en/doc/development/HMS-References/ads-api-nativeadconfiguration-builder
                NativeAdConfiguration config = new NativeAdConfiguration.Builder().build();

                // Создаем Builder в конструктуре которого передаем контекст и id рекламы
                // Далеее устанавливаем слушателей и настройки созданыне ранее -> загружаем рекламу
                NativeAdLoader.Builder builder = new NativeAdLoader.Builder(NativeActivity.this, getAdId());
                builder.setNativeAdLoadedListener(nativeAdLoadedListener)
                        .setAdListener(adListener)
                        .setNativeAdOptions(config)
                        .build()
                        .loadAd(new AdParam.Builder().build());
            }
        };

        loadAdBtn.setOnClickListener(loadAd);
        loadAdBtn.performClick();

    }

    private NativeAd.NativeAdLoadedListener nativeAdLoadedListener = nativeAd -> showNativeAd(nativeAd);

    private void showNativeAd(NativeAd nativeAd) {
        if (this.nativeAd != null) {
            this.nativeAd.destroy();
        }
        this.nativeAd = nativeAd;

        NativeView nativeView = (NativeView) getLayoutInflater().inflate(getLayoutType(), null);

        // инициализируем данные загруженной рекламой
        initNativeAdView(this.nativeAd, nativeView);

        // добавляем рекламу на UI
        nativeAdScrollView.removeAllViews();
        nativeAdScrollView.addView(nativeView);

    }

    private void initNativeAdView(NativeAd nativeAd, NativeView nativeView) {
        // инициализируем view в NativeView
        nativeView.setTitleView(nativeView.findViewById(R.id.ad_title));
        nativeView.setMediaView(nativeView.findViewById(R.id.ad_media));
        nativeView.setAdSourceView(nativeView.findViewById(R.id.ad_source));
        nativeView.setCallToActionView(nativeView.findViewById(R.id.ad_call_to_action));

        // заполняем данными
        ((TextView)nativeView.getTitleView()).setText(nativeAd.getTitle());
        nativeView.getMediaView().setMediaContent(nativeAd.getMediaContent());

        if (nativeAd.getAdSource() != null) {
            TextView adSourceView = (TextView) nativeView.getAdSourceView();
            adSourceView.setText(nativeAd.getAdSource());
            adSourceView.setVisibility(View.VISIBLE);
        } else {
            nativeView.getAdSourceView().setVisibility(View.INVISIBLE);
        }

        if (nativeAd.getCallToAction() != null) {
            Button callToActionButton = (Button) nativeView.getCallToActionView();
            callToActionButton.setText(nativeAd.getCallToAction());
            callToActionButton.setVisibility(View.VISIBLE);
        } else {
            nativeView.getCallToActionView().setVisibility(View.INVISIBLE);
        }

        // если реклама содержит видео добавляем слушатель VideoLifecycleListener
        VideoOperator videoOperator = nativeAd.getVideoOperator();
        if (videoOperator.hasVideo()) {
            videoOperator.setVideoLifecycleListener(videoLifecycleListener);
        }

        nativeView.setNativeAd(nativeAd);
    }

    VideoOperator.VideoLifecycleListener videoLifecycleListener = new VideoOperator.VideoLifecycleListener() {
        @Override
        public void onVideoStart() {
            super.onVideoStart();
        }

        @Override
        public void onVideoPlay() {
            super.onVideoPlay();
        }

        @Override
        public void onVideoEnd() {
            super.onVideoEnd();
        }
    };


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

    // получаем layout для отображения рекламы (для большого размера и видео использовается один тип, для маленькой рекламы другой)
    private int getLayoutType() {
        return typeRadioGroup.getCheckedRadioButtonId() == R.id.radio_button_small
                ? R.layout.native_small_template
                : R.layout.native_video_template;
    }

    // получаем id рекламы в зависимости от выбранного типа
    private String getAdId() {
        switch (typeRadioGroup.getCheckedRadioButtonId()) {
            case R.id.radio_button_small:
                return getString(R.string.ad_native_small);
            case R.id.radio_button_large:
                return getString(R.string.ad_native_large);
            case R.id.radio_button_video:
                return getString(R.string.ad_native_video);
            default:
                return "";
        }
    }

}
