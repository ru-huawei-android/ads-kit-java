package com.sample.huawei.ads;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.huawei.hms.ads.R;
import com.huawei.hms.ads.consent.bean.AdProvider;
import com.huawei.hms.ads.consent.constant.ConsentStatus;
import com.huawei.hms.ads.consent.inter.Consent;

import java.util.ArrayList;

import static com.sample.huawei.ads.Constants.SP_CONSENT_KEY;
import static com.sample.huawei.ads.Constants.SP_NAME;

public class ConsentDialog extends Dialog {

    private Context context;
    private View consentDialogView;
    private View initView;
    private View moreInfoView;
    private View partnerListView;
    private ArrayList<AdProvider> adProviders;
    private ConsentDialogCallback consentDialogCallback;

    // Интерфейс для колбэка диалога
    interface ConsentDialogCallback {
        void updateConsentStatus(ConsentStatus consentStatus);
    }

    void setCallback(ConsentDialogCallback callback) {
        consentDialogCallback = callback;
    }

    // Здесь конструктор для диалога
    public ConsentDialog(@NonNull Context context, ArrayList<AdProvider> adProviders) {
        super(context, R.style.dialog);
        this.context = context;
        this.adProviders = adProviders;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window consentDialogWindow = getWindow();
        consentDialogWindow.requestFeature(Window.FEATURE_NO_TITLE);

        LayoutInflater inflater = getLayoutInflater();
        consentDialogView = inflater.inflate(R.layout.dialog_consent, null);
        setContentView(consentDialogView);

        initView = inflater.inflate(R.layout.dialog_consent_content, null);
        moreInfoView = inflater.inflate(R.layout.dialog_consent_moreinfo, null);
        partnerListView = inflater.inflate(R.layout.dialog_consent_partner_list, null);

        TextView titleTextView = findViewById(R.id.consent_dialog_title_text);
        titleTextView.setText(context.getString(R.string.consent_title));

        showInitConsentInfo();
    }

    private void showInitConsentInfo() {
        addContentView(initView);
        addInitButtonAndLinkClick(consentDialogView);
    }

    private void addInitButtonAndLinkClick(View rootView) {
        // Навешиваем обработчики на кнопки согласия и отказа пользователя
        // от использования персонализированной рекламы
        Button consentYesButton = rootView.findViewById(R.id.btn_consent_init_yes);
        consentYesButton.setOnClickListener((view) -> {
            dismiss();
            updateConsentStatus(ConsentStatus.PERSONALIZED);
        });

        Button consentNoButton = rootView.findViewById(R.id.btn_consent_init_skip);
        consentNoButton.setOnClickListener((view) -> {
            dismiss();
            updateConsentStatus(ConsentStatus.NON_PERSONALIZED);
        });

        TextView initInfoTextView = rootView.findViewById(R.id.consent_center_init_content);
        initInfoTextView.setMovementMethod(ScrollingMovementMethod.getInstance());

        SpannableStringBuilder spanInitText = new SpannableStringBuilder(context.getString(R.string.consent_init_text));

        ClickableSpan initTouchHere = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                showTouchHereInfo();
            }
        };

        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#0000FF"));
        
        int initTouchHereStart = context.getResources().getInteger(R.integer.init_here_start);
        int initTouchHereEnd = context.getResources().getInteger(R.integer.init_here_end);

        spanInitText.setSpan(initTouchHere, initTouchHereStart, initTouchHereEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanInitText.setSpan(colorSpan, initTouchHereStart, initTouchHereEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        
        initInfoTextView.setText(spanInitText);
        initInfoTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void showTouchHereInfo() {
        addContentView(moreInfoView);
        addMoreInfoButtonAndLinkClick(consentDialogView);
    }

    private void addMoreInfoButtonAndLinkClick(View rootView) {
        Button moreInfoBackButton = rootView.findViewById(R.id.btn_consent_more_info_back);
        moreInfoBackButton.setOnClickListener((view) -> { showInitConsentInfo(); });
        TextView moreInfoTextView = rootView.findViewById(R.id.consent_center_more_info_content);
        moreInfoTextView.setMovementMethod(ScrollingMovementMethod.getInstance());

        String moreInfoText = context.getString(R.string.consent_more_info_text);
        SpannableStringBuilder spanMoreInfoText = new SpannableStringBuilder(moreInfoText);
        ClickableSpan moreInfoTouchHere = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                showPartnersListInfo();
            }
        };

        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#0000FF"));
        int moreInfoTouchHereStart = context.getResources().getInteger(R.integer.more_info_here_start);
        int moreInfoTouchHereEnd = context.getResources().getInteger(R.integer.more_info_here_end);

        spanMoreInfoText.setSpan(moreInfoTouchHere, moreInfoTouchHereStart, moreInfoTouchHereEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanMoreInfoText.setSpan(colorSpan, moreInfoTouchHereStart, moreInfoTouchHereEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        moreInfoTextView.setText(spanMoreInfoText);
        moreInfoTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void showPartnersListInfo() {
        TextView partnerListTextView = partnerListView.findViewById(R.id.partners_list_content);
        partnerListTextView.setMovementMethod(ScrollingMovementMethod.getInstance());
        partnerListTextView.setText("");

        ArrayList<AdProvider> learnAdProviders = adProviders;

        if (learnAdProviders != null) {
            for(AdProvider learnAdProvider : learnAdProviders) {
                String link = ("<font color='#0000FF'><a href=" + learnAdProvider.getPrivacyPolicyUrl() + ">"
                        + learnAdProvider.getName() + "</a>");
                partnerListTextView.append(Html.fromHtml(link));
                partnerListTextView.append("  ");
            }
        } else {
            partnerListTextView.append(" 3rd party’s full list of advertisers is empty");
        }
        partnerListTextView.setMovementMethod(LinkMovementMethod.getInstance());
        addContentView(partnerListView);
        addPartnersListButtonAndLinkClick(consentDialogView);
    }

    private void addPartnersListButtonAndLinkClick(View rootView) {
        Button partnerListBackButton = rootView.findViewById(R.id.btn_partners_list_back);
        partnerListBackButton.setOnClickListener((view) -> { showTouchHereInfo(); });
    }

    // сохраняем выбор пользвоателя в shared preferences
    private void updateConsentStatus(ConsentStatus consentStatus) {
        Consent.getInstance(context).setConsentStatus(consentStatus);

        SharedPreferences preferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        preferences.edit().putInt(SP_CONSENT_KEY, consentStatus.getValue()).apply();

        if (consentDialogCallback != null) {
            consentDialogCallback.updateConsentStatus(consentStatus);
        }
    }

    private void addContentView(View view) {
        LinearLayout contentLayout = findViewById(R.id.consent_center_layout);
        contentLayout.removeAllViews();
        contentLayout.addView(view);
    }
}
