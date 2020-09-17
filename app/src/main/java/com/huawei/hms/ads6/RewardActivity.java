package com.huawei.hms.ads6;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.huawei.hms.ads.AdParam;
import com.huawei.hms.ads.R;
import com.huawei.hms.ads.reward.Reward;
import com.huawei.hms.ads.reward.RewardAd;
import com.huawei.hms.ads.reward.RewardAdLoadListener;
import com.huawei.hms.ads.reward.RewardAdStatusListener;

public class RewardActivity extends AppCompatActivity {

    private RewardAd rewardAd;
    private boolean isRewarded = false;
    Button getRewardButton;
    TextView rewardResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward);

        getRewardButton = findViewById(R.id.getRewardBtn);
        rewardResult = findViewById(R.id.rewardResult);

        loadRewardAd();
        getRewardButton.setOnClickListener(view -> showRewardAd());
    }

    private void showRewardAd() {
            if (rewardAd.isLoaded()) {
                isRewarded = false;
                rewardAd.show(this, rewardAdStatusListener);
            }
    }

    private void loadRewardAd() {
        rewardAd = new RewardAd(this, getString(R.string.ad_rewarded_vertical));
        rewardAd.loadAd(new AdParam.Builder().build(), rewardAdListener);
    }

    private RewardAdStatusListener rewardAdStatusListener = new RewardAdStatusListener() {
        @Override
        public void onRewardAdClosed() {
            Toast.makeText(getApplicationContext(), "onRewardAdClosed", Toast.LENGTH_SHORT).show();
            giveReward(isRewarded);
            loadRewardAd();
        }

        @Override
        public void onRewardAdFailedToShow(int code) {
            Toast.makeText(getApplicationContext(),
                    Utils.getErrorMessage(code), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRewardAdOpened() {
            Toast.makeText(getApplicationContext(), "onRewardAdOpened", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRewarded(Reward reward) {
            Toast.makeText(getApplicationContext(), "Ad finished, you get a reward", Toast.LENGTH_SHORT).show();
            isRewarded = true;
            loadRewardAd();
        }
    };

    private RewardAdLoadListener rewardAdListener = new RewardAdLoadListener() {
        @Override
        public void onRewardAdFailedToLoad(int code) {
            super.onRewardAdFailedToLoad(code);
            Toast.makeText(getApplicationContext(), Utils.getErrorMessage(code), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRewardedLoaded() {
            super.onRewardedLoaded();
        }
    };

    private void giveReward(boolean giveReward) {
        if (giveReward) {
            rewardResult.setText(getString(R.string.reward_given));
            rewardResult.setTextColor(getResources().getColor(R.color.colorRewardGiven));
        } else {
            rewardResult.setText(getString(R.string.reward_not_given));
            rewardResult.setTextColor(getResources().getColor(R.color.colorRewardRejected));
        }
    }
}
