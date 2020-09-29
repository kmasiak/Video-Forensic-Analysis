package com.example.videoforensicexaminer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.videoforensicexaminer.R;
import com.example.videoforensicexaminer.utils.Constants;
import com.example.videoforensicexaminer.utils.Preferences;

public class PrivacyPolicyActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        final Preferences preferences = new Preferences(this);
        String terms = preferences.getTerms();
        String privacy = preferences.getPrivacy();
        String consent = preferences.getConsent();

        if (consent.equals("YES") && terms.equals("YES") && privacy.equals("YES")) {
            Intent intentMain = new Intent(this, MainActivity.class);
            startActivity(intentMain);
        } else {
            //show privacy statement to accept
            Button agreeBtn = (Button) findViewById(R.id.privacyAgreeBtn);
            Button disagreeBtn = (Button) findViewById(R.id.privacyDisagreeBtn);
            ScrollView scrollView = (ScrollView) findViewById(R.id.privacyScrollView);

//            TextView privacyTxt = new TextView(this);
//            privacyTxt.setText(Html.fromHtml(getString(R.string.privacy_statement)));
//            scrollView.addView(privacyTxt);
            WebView myWebView = (WebView) findViewById(R.id.privacyPolicyWebview);
            myWebView.loadUrl(Constants.API_BASE_URL+"/privacy-policy.html");

            agreeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    preferences.setPrivacy("YES");
                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(intent);
                }
            });

            disagreeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    preferences.setPrivacy("NO");
                    Toast.makeText(getApplicationContext(), "You need to agree to the consent form, terms of use and privacy policy if you wish to use the app.", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
