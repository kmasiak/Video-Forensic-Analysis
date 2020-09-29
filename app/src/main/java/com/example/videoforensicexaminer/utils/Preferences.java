package com.example.videoforensicexaminer.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {

    private Context mContext;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private String FILE_NAME = "app.videoforensicanalysis.preferences";
    private int MODE = Context.MODE_PRIVATE;

    private String KEY_PRIVACY = "KEY_PRIVACY";
    private String KEY_TERMS = "KEY_TERMS";
    private String KEY_CONSENT = "KEY_CONSENT";
    private String KEY_GENDER = "KEY_GENER";
    private String KEY_IS_USER_LOGGED_IN = "KEY_IS_USER_LOGGED_IN";
    private String KEY_JWT_TOKEN = "KEY_JWT_TOKEN";
    private String KEY_FILES = "KEY_FILES";
    private String KEY_RECORDED_COUNT = "KEY_RECORDED_COUNT";
    private String KEY_CORPUS_NUM = "KEY_CORPUS_NUM";

    public Preferences(Context context) {
        mContext = context;
        preferences = mContext.getSharedPreferences(FILE_NAME, MODE);
        editor = preferences.edit();
    }

    //Terms
    public void setTerms(String value) {
        editor.putString(KEY_TERMS, value);
        editor.commit();
    }
    public String getTerms() {
        return preferences.getString(KEY_TERMS, "NO");
    }

    public String getFiles() {
        return preferences.getString(KEY_FILES, "");
    }

    public void setFiles(String value) {
        editor.putString(KEY_FILES, value);
        editor.commit();
    }

    //Privacy
    public void setPrivacy(String value) {
        editor.putString(KEY_PRIVACY, value);
        editor.commit();
    }
    public String getPrivacy() {
        return preferences.getString(KEY_PRIVACY, "NO");
    }

    //Consent
    public void setConsent(String value) {
        editor.putString(KEY_CONSENT, value);
        editor.commit();
    }
    public String getConsent() {
        return preferences.getString(KEY_CONSENT, "NO");
    }

    //Gender
    public void setGender(String value) {
        editor.putString(KEY_GENDER, value);
        editor.commit();
    }
    public String getGender (){
        return preferences.getString(KEY_GENDER, "NONE");
    }

    //Gender
    public void setToken(String value) {
        editor.putString(KEY_JWT_TOKEN, value);
        editor.commit();
    }
    public String getToken(){
        return preferences.getString(KEY_JWT_TOKEN, null);
    }

    public void setUserLoggedIn(Boolean value) {
        editor.putBoolean(KEY_IS_USER_LOGGED_IN, value);
        editor.commit();
    }

    public Boolean isUserLoggedIn() {return preferences.getBoolean(KEY_IS_USER_LOGGED_IN, false); }

    //Gender
    public void setCorpusNum(int value) {
        editor.putInt(KEY_CORPUS_NUM, value);
        editor.commit();
    }
    public int getCorpusNum(){
        return preferences.getInt(KEY_CORPUS_NUM, 1);
    }

    public void setRecordedCount(int value) {
        editor.putInt(KEY_RECORDED_COUNT, value);
        editor.commit();
    }
    public int getRecordedCount(){
        return preferences.getInt(KEY_RECORDED_COUNT, 0);
    }

}