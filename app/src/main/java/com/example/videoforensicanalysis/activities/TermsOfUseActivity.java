package com.example.videoforensicanalysis.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.videoforensicanalysis.R;
import com.example.videoforensicanalysis.utils.Constants;
import com.example.videoforensicanalysis.utils.Preferences;


public class TermsOfUseActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_of_service);
        final Preferences preferences = new Preferences(this);

        if (preferences.getConsent().equals("YES") && preferences.getTerms().equals("YES")) {
            Intent intentLauncher2 = new Intent(this, PrivacyPolicyActivity.class);
            startActivity(intentLauncher2);
        } else {
            //show terms to accept
            Button agreeBtn = findViewById(R.id.termsAgreeBtn);
            Button disagreeBtn = findViewById(R.id.termsDisagreeBtn);
            ScrollView scrollView = findViewById(R.id.termsScrollView);

//            TextView privacyTxt = new TextView(this);
//            privacyTxt.setText(Html.fromHtml(getString(R.string.terms_of_use)));
//            scrollView.addView(privacyTxt);

            WebView myWebView = findViewById(R.id.TosWebview);
            myWebView.loadUrl(Constants.API_BASE_URL+"/tos.html");

            myWebView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon){
                    super.onPageStarted(view, url, favicon);
                    showProgressDialog();
                }

                @Override
                public void onPageFinished(WebView view, String url){
                    super.onPageFinished(view, url);
                    hideProgressDialog();
                }
            });

            agreeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    preferences.setTerms("YES");
                    Intent intent = new Intent(getBaseContext(), PrivacyPolicyActivity.class);
                    startActivity(intent);
                }
            });

            disagreeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    preferences.setTerms("NO");
                    Toast.makeText(getApplicationContext(), "You need to agree to the consent form, terms of use and privacy policy if you wish to use the app.", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
