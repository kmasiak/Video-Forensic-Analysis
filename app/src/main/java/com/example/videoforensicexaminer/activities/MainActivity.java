package com.example.videoforensicexaminer.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.videoforensicexaminer.R;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // function to show a dialog to select video file
    public void showVideoChooserDialog(View view) {
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        intent.setType("video/*");
        startActivityForResult(intent, 2);
    }

    // on activity result to get file from intent data
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri selectedImage = data.getData();
        String[] filePath = {MediaStore.Video.Media.DATA};
        Cursor c = getContentResolver().query(selectedImage, filePath,
                null, null, null);
        c.moveToFirst();
        int columnIndex = c.getColumnIndex(filePath[0]);
        String videoPath = c.getString(columnIndex);
        c.close();
        Log.d("SelectedVideoPath", videoPath);
        TextView txt = findViewById(R.id.file);
        txt.setText(videoPath);
    }

    public void onSubmit(View view) {

    }

    //Will return with empty String "" if no recording env is selected
    public String getRecordingEnv() {
        String[] recordingEnvOptions = {"Classroom/Lab", "Office", "Bedroom/Living Room", "Kitchen", "Balcony/Outdoor",
                "Ground/Open Environment", "Cafeteria",  "Anechoic Room", "Studio", "Vehicle", "Other"};
        RadioGroup recordingEnv = findViewById(R.id.environment_selection);
        int id = recordingEnv.getCheckedRadioButtonId();
        String recordEnv;
        if(id == -1) {
            recordEnv = "";
            System.out.println("No Recording Environment Is Selected");
        } else {
            recordEnv = recordingEnvOptions[--id];
        }
        return recordEnv;
    }

    //Will return with empty String "" if no mask is selected
    public String getMaskType() {
        String[] maskTypeOptions = {"Cloth", "Surgical", "N95", "FFP2", "FFP3", "Other"};
        RadioGroup maskSelection = findViewById(R.id.mask_selection);
        int id = maskSelection.getCheckedRadioButtonId();
        String maskType;
        if(id == -1) {
            maskType = "";
            System.out.println("No Mask Is Selected");
        } else {
            id -= 12;
            maskType = maskTypeOptions[id];
        }
        return maskType;
    }
}