package com.sample.huawei.ads;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.huawei.hms.ads.R;
import com.huawei.hms.ads.consent.bean.AdProvider;
import com.huawei.hms.ads.consent.constant.ConsentStatus;
import com.huawei.hms.ads.consent.constant.DebugNeedConsent;
import com.huawei.hms.ads.consent.inter.Consent;
import com.huawei.hms.ads.consent.inter.ConsentUpdateListener;

import java.util.ArrayList;
import java.util.List;

import static com.sample.huawei.ads.Constants.SP_CONSENT_KEY;
import static com.sample.huawei.ads.Constants.SP_NAME;

public class MainActivity extends AppCompatActivity {

    private ArrayList<AdFormat> dataSet;

    private RecyclerView recyclerView;

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataSet = new ArrayList<AdFormat>();
        dataSet.add(new AdFormat("Banner Ad", BannerActivity.class));
        dataSet.add(new AdFormat("Interstitial Ad", InterstitialActivity.class));
        dataSet.add(new AdFormat("Native Ad", NativeActivity.class));
        dataSet.add(new AdFormat("Reward Ad", RewardActivity.class));

        recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(new RvAdapter(dataSet, this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        checkConsentStatus();
    }

    private void checkConsentStatus() {
        ArrayList<AdProvider> adProviderList = new ArrayList();
        Consent consentInfo = Consent.getInstance(this);
        consentInfo.addTestDeviceId("********");
        consentInfo.setDebugNeedConsent(DebugNeedConsent.DEBUG_NOT_NEED_CONSENT);
        consentInfo.requestConsentUpdate(new ConsentUpdateListener() {
            @Override
            public void onSuccess(ConsentStatus consentStatus, boolean isNeedConsent, List<AdProvider> adProviders) {
                Log.d(TAG, "ConsentStatus: $consentStatus, isNeedConsent: $isNeedConsent");
                if (isNeedConsent) {
                    if (adProviders.size() >0) {
                        adProviderList.addAll(adProviders);
                    }
                    showConsentDialog(adProviderList);
                }
            }

            @Override
            public void onFail(String s) {
                Log.d(TAG, "User's consent status failed to update: $errorDescription");
                if (getPreferences(
                        SP_CONSENT_KEY,
                        Constants.DEFAULT_SP_CONSENT_VALUE
                ) < 0) {
                    // В нашем примере мы показываем диалог с согласием независимо от того,
                    // получили ли список провайдеров или нет, просто передаём туда пустой список
                    showConsentDialog(adProviderList);
                }
            }
        });
    }



    private int getPreferences(String key, int defValue) {
        SharedPreferences preferences = getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        int value = preferences.getInt(key, defValue);
        Log.d(TAG, "Key:$key, Preference value is: $value");
        return value;
    }

    private void showConsentDialog(ArrayList<AdProvider> providers) {
        ConsentDialog dialog = new ConsentDialog(this, providers);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

}

