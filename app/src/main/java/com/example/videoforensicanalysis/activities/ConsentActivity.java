package com.example.videoforensicanalysis.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.videoforensicanalysis.utils.Constants;
import com.example.videoforensicanalysis.utils.Preferences;
import com.example.videoforensicanalysis.R;


public class ConsentActivity extends BaseActivity {
    private static final String TAG = "ConsentActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Preferences preferences = new Preferences(this);
        String consent = preferences.getConsent();
        Log.d("CONSENT, LOGIN",consent + preferences.isUserLoggedIn());
        if (consent.equals("YES")) {
            Intent intentLauncher = new Intent(this, SignUpActivity.class);
            startActivity(intentLauncher);
        } else {
            //show terms to accept
            setContentView(R.layout.activity_consent);
            Button agreeBtn = findViewById(R.id.consentAgreeBtn);
            Button disagreeBtn = findViewById(R.id.consentDisagreeBtn);
            ScrollView scrollView =  findViewById(R.id.consentScrollView);

            WebView myWebView = findViewById(R.id.consentWebview);
            myWebView.setWebChromeClient(webChromeClientWithProgressDialog);
            myWebView.setWebViewClient(defaultWebViewClient);
            myWebView.loadUrl(Constants.API_BASE_URL+"/videoConsent.html");
            agreeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    preferences.setConsent("YES");
                    preferences.setPrivacy("YES");
                    preferences.setTerms("YES");
                    Intent intent = new Intent(getBaseContext(), SignUpActivity.class);
                    startActivity(intent);
                }
            });

            disagreeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    preferences.setConsent("NO");
                    Toast.makeText(getApplicationContext(), "You need to agree to the consent form, terms of use and privacy policy if you wish to use the app.", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
