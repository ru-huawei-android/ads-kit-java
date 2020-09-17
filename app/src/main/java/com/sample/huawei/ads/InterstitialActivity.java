package com.sample.huawei.ads;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.huawei.hms.ads.AdListener;
import com.huawei.hms.ads.AdParam;
import com.huawei.hms.ads.InterstitialAd;
import com.huawei.hms.ads.R;

public class InterstitialActivity extends AppCompatActivity {

    private InterstitialAd interstitialAd;
    private View.OnClickListener loadAd;
    private RadioGroup interstitialRadioGroup;
    private Button loadInterstitialButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interstitial);

        interstitialRadioGroup = findViewById(R.id.interstitialRadioGroup);
        loadInterstitialButton = findViewById(R.id.loadInterstitialAdBtn);

        loadAd = view -> {
            interstitialAd = new InterstitialAd(InterstitialActivity.this);
            interstitialAd.setAdId(getAdId(interstitialRadioGroup.getCheckedRadioButtonId()));
            interstitialAd.setAdListener(adListener);
            interstitialAd.loadAd(new AdParam.Builder().build());
        };

        loadInterstitialButton.setOnClickListener(loadAd);
    }

    private AdListener adListener = new AdListener() {
        @Override
        public void onAdFailed(int code) {
            super.onAdFailed(code);
            Toast.makeText(getApplicationContext(), Utils.getErrorMessage(code), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onAdLoaded() {
            super.onAdLoaded();
            showInterstitial();
        }
    };

    private void showInterstitial() {
        // показываем рекламу только если она загрузилась
        if (interstitialAd.isLoaded()) {
            interstitialAd.show();
        } else {
            Toast.makeText(this, "Ad did not load", Toast.LENGTH_SHORT).show();
        }
    }


    private String getAdId(int id) {
        return id == R.id.display_image
                ? getString(R.string.ad_interestial_img)
                : getString(R.string.ad_interestial_vid);
    }
}
