package com.example.videoforensicanalysis.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.example.videoforensicanalysis.R;
import com.example.videoforensicanalysis.utils.Constants;
import com.example.videoforensicanalysis.utils.Preferences;

public class AboutActivity extends BaseActivity {
    private static final String TAG = "AboutActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Preferences preferences = new Preferences(this);

        if(preferences.isUserLoggedIn()){
            Intent intentMain = new Intent(this, MainActivity.class);
            startActivity(intentMain);
        } else {
            setContentView(R.layout.activity_about);
            Button getStartedBtn = findViewById(R.id.aboutGetStartedBtn);
            WebView myWebView = findViewById(R.id.aboutWebview);
            myWebView.setWebChromeClient(webChromeClientWithProgressDialog);
            myWebView.setWebViewClient(defaultWebViewClient);
            myWebView.loadUrl(Constants.API_BASE_URL+"/how-it-works.html");

            getStartedBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AboutActivity.this, SignUpActivity.class);
                    startActivity(intent);
                }
            });
        }
    }
}
