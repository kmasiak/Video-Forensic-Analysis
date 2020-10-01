package com.example.videoforensicexaminer.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import androidx.fragment.app.DialogFragment;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.videoforensicexaminer.R;
import com.example.videoforensicexaminer.fragments.InputCorpusEnvFragment;
import com.example.videoforensicexaminer.fragments.InputMaskFragment;
import com.example.videoforensicexaminer.api.RetrofitClientInstance;
import com.example.videoforensicexaminer.api.VideoForensicApiService;
import com.example.videoforensicexaminer.model.UploadFileResponse;
import com.example.videoforensicexaminer.model.VideoFile;
import com.example.videoforensicexaminer.utils.ApiUtils;
import com.example.videoforensicexaminer.utils.Preferences;
import com.jaredrummler.android.device.DeviceName;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jaredrummler.android.device.DeviceName;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    VideoFile videofile;
    Context context;
    final StringBuilder makeAndModel = new StringBuilder();
    Preferences preferences;
    String maskType, recordingEnv, corpusID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.context = getApplicationContext();
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
            Intent intent = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
            intent.setType("video/*");
            startActivityForResult(intent, 2);
        }
    }
    


    public void buildChoiceAlertViewMask() {
        InputMaskFragment frag = new InputMaskFragment();
        frag.show(getSupportFragmentManager().beginTransaction(),"DialogFragment");
    }

    public void buildChoiceAlertViewEnv() {
        final String[] recordingEnvOptions = {"Classroom/Lab", "Office", "Bedroom/Living Room", "Kitchen", "Balcony/Outdoor",
                "Ground/Open Environment", "Cafeteria",  "Anechoic Room", "Studio", "Vehicle", "Other"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Where are you recording?");

        int checkedItem = 0; //this will checked the item when user open the dialog
        builder.setSingleChoiceItems(recordingEnvOptions, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "setPositiveButton: onChange " + which + " ENV " + recordingEnvOptions[which]);
                if (which == -1) which = 0;
                //((MainActivity) getActivity()).setRecordingEnv(recordingEnvOptions[which]);
                recordingEnv = recordingEnvOptions[which];
                //Toast.makeText(getActivity(), "recordingEnv default: " + ((MainActivity) getActivity()).getRecordingEnv(), Toast.LENGTH_LONG).show();
            }
        });

        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == -1) which = 0;
                Log.d(TAG, "setPositiveButton: done " + which + " ENV " + recordingEnvOptions[which]);
                if(recordingEnv == null) recordingEnv  = recordingEnvOptions[which];
                buildChoiceAlertViewCorpus();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void buildChoiceAlertViewCorpus() {
        InputCorpusEnvFragment frag = new InputCorpusEnvFragment();
        frag.setTargetFragment(frag, 1);
        frag.show(getSupportFragmentManager().beginTransaction(),"DialogFragment");
    }

    // on activity result to get file from intent data
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //maskType = data.getStringExtra("Mask Type");
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
        //buildChoiceAlertViewMask();
        //buildChoiceAlertViewEnv();
        videofile = new VideoFile(videoPath.split("/")[videoPath.split("/").length-1], vidfile, corpusID, recordingEnv, maskType);
    }

    public void onSubmit(View view) {
        if(videofile == null) {
            Toast.makeText(this, "Please upload your video before submitting.",
                    Toast.LENGTH_LONG).show();
        } else {
            handleUploadToServer(videofile);
        }
    }

    private void handleUploadToServer(final VideoFile videoFile) {
        preferences = new Preferences(context);
        makeAndModel.append("Android").append("-");
        DeviceName.with(context).request(new DeviceName.Callback() {
            @Override public void onFinished(DeviceName.DeviceInfo info, Exception error) {
                makeAndModel.append(info.manufacturer);
                Log.d(TAG, "onFinished: " + info.manufacturer + " == " + info.manufacturer);
                makeAndModel.append(info.model);          // "SM-G955W"
            }
        });

        File file = new File(videoFile.getFile().getAbsolutePath());

        VideoForensicApiService service = RetrofitClientInstance.getRetrofitInstance(context).create(VideoForensicApiService.class);

        RequestBody fileReqBody = RequestBody.create(MediaType.parse("audio/mpeg"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), fileReqBody);

        Log.d(TAG, "handleUploadToServer: " + makeAndModel);
        Call call;
        call = service.uploadFile(videoFile.getRecordingEnv(), videoFile.getCorpusId(), makeAndModel.toString(), part);
        Log.d("IsMasked", "is the audio masked?: \t" + videoFile.getIsMasked());
        call.enqueue(new Callback<UploadFileResponse>() {
            @Override
            public void onResponse(Call<UploadFileResponse> call, Response<UploadFileResponse> response) {
                if (response.code() == 200 || response.code() == 201) {
                    showAlertDialog("Success", "File uploaded successfully!", true);

                } else if (response.code() == 401) {
                    Toast.makeText(context, "Please log into your account.", Toast.LENGTH_SHORT).show();
                    preferences.setToken(null);
                    preferences.setUserLoggedIn(false);
                    Intent intentMain = new Intent(context, SignInActivity.class);
                    intentMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intentMain);
                } else if (response.code() == 409) {
                    Toast.makeText(context, "File already uploaded!", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        showAlertDialog("Upload Failed", ApiUtils.errorResponseParser(response.errorBody()).getString("message"), false);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<UploadFileResponse> call, Throwable t) {
                Log.d("TAG", "API call failed. \n" + t.getMessage());
                showAlertDialog("Upload failed :(", "Something went wrong.\nPlease try again later.", false);
            }
        });
    }

    private void showAlertDialog(String title, String message, final boolean isSuccess) {
        AlertDialog ad = new AlertDialog.Builder(context)
                .create();
        ad.setCancelable(false);
        ad.setTitle(title);
        ad.setMessage(message);
        ad.setButton(context.getString(R.string.ok_text), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        ad.show();
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
            id -= 18;
            corpus = maskTypeOptions[id];
        }
        return corpus;
    }
}
