package com.example.videoforensicanalysis.utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import okhttp3.ResponseBody;

public class ApiUtils {
    public static JSONObject errorResponseParser(ResponseBody responseErrorBody){
        JSONObject jo = null;
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(responseErrorBody.byteStream()));
            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String finallyError = sb.toString();

        try {
            jo = new JSONObject(finallyError);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jo;
    }

}
