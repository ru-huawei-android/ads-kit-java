package com.sample.huawei.ads;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.huawei.hms.ads.AdParam;
import com.huawei.hms.ads.AudioFocusType;
import com.huawei.hms.ads.R;
import com.huawei.hms.ads.splash.SplashAdDisplayListener;
import com.huawei.hms.ads.splash.SplashView;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();

    // Время показа рекламного блока в миллисекундах
    private static final int AD_TIMEOUT = 5000;

    private static final int MSG_AD_TIMEOUT = 1001;

    private boolean hasPaused = false;

    private SplashView splashView;

    private Handler timeoutHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (SplashActivity.this.hasWindowFocus()) {
                proceedToMainActivity();
            }
            return false;
        }
    });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        loadAd();
    }

    private SplashView.SplashAdLoadListener splashAdLoadListener = new SplashView.SplashAdLoadListener() {
        @Override
        public void onAdLoaded() {
            Log.i(TAG, "SplashAdLoadListener onAdLoaded.");
            Toast.makeText(SplashActivity.this, getString(R.string.status_load_ad_success), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAdFailedToLoad(int errorCode) {
            Log.i(TAG, "SplashAdLoadListener onAdFailedToLoad, errorCode: " + errorCode);
            Toast.makeText(SplashActivity.this, getString(R.string.status_load_ad_fail) + errorCode, Toast.LENGTH_SHORT).show();
            proceedToMainActivity();
        }

        @Override
        public void onAdDismissed() {
            // Этот метод выполняется когда заканчивается показ рекламы
            Log.i(TAG, "SplashAdLoadListener onAdDismissed.");
            Toast.makeText(SplashActivity.this, getString(R.string.status_ad_dismissed), Toast.LENGTH_SHORT).show();
            proceedToMainActivity();
        }
    };

    private SplashAdDisplayListener adDisplayListener = new SplashAdDisplayListener() {
        @Override
        public void onAdShowed() {
            Log.i(TAG, "SplashAdDisplayListener onAdShowed.");
        }

        @Override
        public void onAdClick() {
            Log.i(TAG, "SplashAdDisplayListener onAdClick.");
        }
    };

    private void loadAd() {
        Log.i(TAG, "Start to load ad");

        AdParam adParam = new AdParam.Builder().build();

        splashView = findViewById(R.id.splashAdView);
        splashView.setAdDisplayListener(adDisplayListener);

        // Устанавливаем лого
        splashView.setLogoResId(R.mipmap.ic_launcher);
        // Описание к лого
        splashView.setMediaNameResId(R.string.app_name);
        // Задаём аудиофокус для видео
        splashView.setAudioFocusType(AudioFocusType.NOT_GAIN_AUDIO_FOCUS_WHEN_MUTE);

        splashView.load(getString(R.string.ad_splash), ActivityInfo.SCREEN_ORIENTATION_PORTRAIT, adParam, splashAdLoadListener);
        Log.i(TAG, "Finished loading ad");

        // Убираем сообщение о таймауте из очереди
        timeoutHandler.removeMessages(MSG_AD_TIMEOUT);
        // Посылаем отложенное сообщение, чтобы быть уверенными что домашняя страница приложения
        // уже может быть отображена после того как реклама закончила показ
        timeoutHandler.sendEmptyMessageDelayed(MSG_AD_TIMEOUT, AD_TIMEOUT);
    }

    @Override
    protected void onStop() {
        Log.i(TAG, "SplashActivity onStop.");
        // Убираем сообщение о таймауте из очереди
        timeoutHandler.removeMessages(MSG_AD_TIMEOUT);
        hasPaused = true;
        super.onStop();
    }

    @Override
    protected void onRestart() {
        Log.i(TAG, "SplashActivity onRestart.");
        super.onRestart();
        hasPaused = false;
        proceedToMainActivity();
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "SplashActivity onDestroy.");
        super.onDestroy();
        if (splashView != null) {
            splashView.destroyView();
        }
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "SplashActivity onPause.");
        super.onPause();
        if (splashView != null) {
            splashView.pauseView();
        }
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "SplashActivity onResume.");
        super.onResume();
        if (splashView != null) {
            splashView.resumeView();
        }
    }

    /**
     * Переход со сплэш-страницы на домашнюю когда закончился показ рекламы
     */
    private void proceedToMainActivity() {
        if (!hasPaused) {
            hasPaused = true;
            Intent mainActivityIntent = new Intent(this, MainActivity.class);
            startActivity(mainActivityIntent);

            new Handler().postDelayed(this::finish, 1000);
        }
    }

}
