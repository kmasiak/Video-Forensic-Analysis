package com.example.videoforensicexaminer.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import androidx.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.ProgressBar;

import com.example.videoforensicexaminer.activities.MainActivity;
import com.jaredrummler.android.device.DeviceName;

import org.json.JSONException;

import java.io.File;

import com.example.videoforensicexaminer.R;
import com.example.videoforensicexaminer.activities.SignInActivity;
import com.example.videoforensicexaminer.api.VideoForensicApiService;
import com.example.videoforensicexaminer.api.RetrofitClientInstance;
import com.example.videoforensicexaminer.model.VideoFile;
import com.example.videoforensicexaminer.model.UploadFileResponse;
import com.example.videoforensicexaminer.utils.ApiUtils;
import com.example.videoforensicexaminer.utils.Preferences;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VidedoFileAdapter{
    Context context;
    VideoFile videoFile = null;
    private MediaPlayer mPlayer = null;
    Preferences preferences;
    final StringBuilder makeAndModel = new StringBuilder();
    private static final String TAG = "VideoFileAdapter";



    public VidedoFileAdapter(@NonNull Context context, @NonNull VideoFile object) {
        this.context = context;
        this.videoFile = object;
        preferences = new Preferences(context);
        makeAndModel.append("Android").append("-");
        DeviceName.with(context).request(new DeviceName.Callback() {
            @Override public void onFinished(DeviceName.DeviceInfo info, Exception error) {
                makeAndModel.append(info.manufacturer);
                Log.d(TAG, "onFinished: " + info.manufacturer + " == " + info.manufacturer);
                makeAndModel.append(info.model);          // "SM-G955W"
            }
        });
    }


    public void handleUploadToServer() {
        File file = this.videoFile.getFile();
        MainActivity.progressBar.setVisibility(View.VISIBLE);
        MainActivity.progressText.setVisibility(View.VISIBLE);
        VideoForensicApiService service = RetrofitClientInstance.getRetrofitInstance(context).create(VideoForensicApiService.class);

        RequestBody fileReqBody = RequestBody.create(MediaType.parse("video/mp4"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), fileReqBody);

        Log.d(TAG, "handleUploadToServer: " + makeAndModel);
        Call call;
        call = service.uploadFile(videoFile.getRecordingEnv(), videoFile.getCorpusId(), makeAndModel.toString(), videoFile.getMaskType(), part);
        Log.d("IsMasked", "is the video masked?: \t" + videoFile.getIsMasked());
        call.enqueue(new Callback<UploadFileResponse>() {
            @Override
            public void onResponse(Call<UploadFileResponse> call, Response<UploadFileResponse> response) {
                MainActivity.progressBar.setVisibility(View.INVISIBLE);
                MainActivity.progressText.setVisibility(View.INVISIBLE);
                if (response.code() == 200 || response.code() == 201) {
                    showAlertDialog("Success",
                            "File uploaded successfully!", true);
                    }
                 else if (response.code() == 401) {
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


}
