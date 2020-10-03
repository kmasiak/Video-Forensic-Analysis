package com.example.videoforensicexaminer.activities;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.videoforensicexaminer.adapters.VidedoFileAdapter;

import android.Manifest;
import android.content.Context;
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
import android.widget.Toast;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.videoforensicexaminer.R;
import com.example.videoforensicexaminer.model.VideoFile;

import org.w3c.dom.Text;

import java.io.File;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private final int READ_FILE_REQUEST_CODE = 1;
    VideoFile videofile;
    Context context;
    String maskType, recordingEnv, corpusID;
    public static ProgressBar progressBar;
    public static TextView progressText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.context = getApplicationContext();
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        progressText = (TextView) findViewById(R.id.tView);
        progressBar.setVisibility(View.INVISIBLE);
        progressText.setVisibility(View.INVISIBLE);
    }

    // function to show a dialog to select video file
    public void showVideoChooserDialog(View view) {
        recordingEnv = getRecordingEnv();
        maskType = getMaskType();
        corpusID = getCorpus();
        if(recordingEnv == "" || maskType == "" || corpusID == "") {
            Toast.makeText(this, "Please fill in all the required information before uploading.",
                    Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{READ_EXTERNAL_STORAGE},READ_FILE_REQUEST_CODE);
            Intent intent = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
            intent.setType("video/*");
            startActivityForResult(intent, 2);
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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

        File vidfile = new File(videoPath);

        Log.d("FILE:", String.valueOf(vidfile.canRead()));
        videofile = new VideoFile(vidfile.getName(), vidfile, corpusID, recordingEnv, maskType);
    }

    public void onSubmit(View view) {
        if(videofile == null) {
            Toast.makeText(this, "Please upload your video before submitting.",
                    Toast.LENGTH_LONG).show();
        } else {
            VidedoFileAdapter videdoFileAdapter = new VidedoFileAdapter(MainActivity.this, videofile);
            Toast.makeText(MainActivity.this, "Uploading " + videofile.getFileName() , Toast.LENGTH_SHORT).show();
            videdoFileAdapter.handleUploadToServer();
        }
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
        String[] maskTypeOptions = {"Cloth", "Surgical", "N95", "FFP2", "FFP3", "Other", "None"};
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

    //Will return with empty String "" if no mask is selected
    public String getCorpus() {
        String[] maskTypeOptions = {"1", "2", "3", "4", "5", "6"};
        RadioGroup corpusSelection = findViewById(R.id.corpus_selection);
        int id = corpusSelection.getCheckedRadioButtonId();
        String corpus;
        if(id == -1) {
            corpus = "";
            System.out.println("No Corpus Is Selected");
        } else {
            id -= 19;
            corpus = maskTypeOptions[id];
        }
        return corpus;
    }

    public  void progressOn(){
            progressBar.setVisibility(View.VISIBLE);
            progressText.setVisibility(View.VISIBLE);

    }
    public  void progressOff() {
        progressBar.setVisibility(View.INVISIBLE);
        progressText.setVisibility(View.INVISIBLE);
    }

}
