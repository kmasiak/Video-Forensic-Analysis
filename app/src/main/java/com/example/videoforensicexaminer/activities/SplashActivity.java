package com.example.videoforensicexaminer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.example.videoforensicexaminer.utils.Preferences;

public class SplashActivity extends BaseActivity {
    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 1000;
    Preferences preferences;
    private static final String TAG = "SplashActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = new Preferences(this);
        Log.d(TAG, "onCreate: " + preferences.toString());
        if(preferences.isUserLoggedIn()){
            Intent intentMain = new Intent(this, ConsentActivity.class);
            startActivity(intentMain);
            finish();
        } else {
            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this, ConsentActivity.class));
                    finish();
                }
            }, SPLASH_DISPLAY_LENGTH);
        }
    }
}
