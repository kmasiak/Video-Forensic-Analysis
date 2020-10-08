package com.example.videoforensicanalysis.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import org.json.JSONException;

import java.util.List;

import com.example.videoforensicanalysis.R;
import com.example.videoforensicanalysis.api.VideoForensicApiService;
import com.example.videoforensicanalysis.api.RetrofitClientInstance;
import com.example.videoforensicanalysis.model.JwtAuthenticationResponse;
import com.example.videoforensicanalysis.model.SignInRequest;
import com.example.videoforensicanalysis.utils.ApiUtils;
import com.example.videoforensicanalysis.utils.Constants;
import com.example.videoforensicanalysis.utils.Preferences;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends BaseActivity implements View.OnClickListener, Validator.ValidationListener {
    private static final String TAG = "SignInActivity";

    @NotEmpty @Email
    EditText enterEmailAddress;
    @NotEmpty
    EditText enterPassword;
    Button submitBtn, notRegisteredBtn;
    Preferences preferences;
    Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_in);

        validator = new Validator(this);
        validator.setValidationListener(this);

        preferences = new Preferences(this);

        if(preferences.isUserLoggedIn()){
            Intent intentMain = new Intent(this, MainActivity.class);
            startActivity(intentMain);
        } else {
            getViewReferences();
            submitBtn.setOnClickListener(this);
            notRegisteredBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intentMain = new Intent(SignInActivity.this, SignUpActivity.class);
                    startActivity(intentMain);
                }
            });
        }

        TextView txt = findViewById(R.id.forgotPassword); //txt is object of TextView
        txt.setMovementMethod(LinkMovementMethod.getInstance());
        txt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(Uri.parse(Constants.API_BASE_URL+"/api/auth/forgot-password"));
                startActivity(browserIntent);
            }
        });
    }

    private void getViewReferences() {
        enterEmailAddress = findViewById(R.id.enterEmailAddress);
        enterPassword = findViewById(R.id.enterPassword);
        submitBtn = findViewById(R.id.submitBtn);
        notRegisteredBtn = findViewById(R.id.notRegisteredBtn);
    }

    @Override
    public void onClick(View view) {
        validator.validate();
    }

    private void handleSignin() {
        SignInRequest user = new SignInRequest(enterEmailAddress.getText().toString(), enterPassword.getText().toString());

        VideoForensicApiService service = RetrofitClientInstance.getRetrofitInstance(this).create(VideoForensicApiService.class);
        Call<JwtAuthenticationResponse> call = service.signIn(user);

        call.enqueue(new Callback<JwtAuthenticationResponse>() {
            @Override
            public void onResponse(Call<JwtAuthenticationResponse> call, Response<JwtAuthenticationResponse> response) {
                if(response.code() == 200){
                    hideProgressDialog();
                    Toast.makeText(SignInActivity.this, "Login successful!", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "Login successful! Response Code: " + response.code());
                    preferences.setToken(response.body().getAccessToken());
                    preferences.setUserLoggedIn(true);
                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(intent);
                }else {
                    hideProgressDialog();
                    try {
                        alertErrorView(ApiUtils.errorResponseParser(response.errorBody()).getString("message"), SignInActivity.this);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    if(response.code() == 401){
//                        Log.d(TAG, "onResponse: " + response.toString() + "\t" + response.code());
//                        Toast.makeText(SignInActivity.this, "Username or Password was incorrect. ", Toast.LENGTH_LONG).show();
//                    }
//                    if(response.code() == 400 && response.body() != null){
//                        Log.d(TAG, "onResponse: 400 " + response.toString() + "\t" + response.code());
//                        Toast.makeText(SignInActivity.this, "Login Failed. " + response.body().getMessage(), Toast.LENGTH_LONG).show();
//                    }
                    Log.d(TAG, "onResponse: " + response.toString() + "\t" + response.code());
                    //Toast.makeText(SignInActivity.this, "Something went wrong. Please try again later.", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<JwtAuthenticationResponse> call, Throwable t) {
                hideProgressDialog();
                alertErrorView("Something went wrong.\nPlease try again later.", SignInActivity.this);
                //Toast.makeText(SignInActivity.this, "Something went wrong. Please try again later!", Toast.LENGTH_LONG).show();
                Log.d(TAG, "t.getMessage() " + t.getMessage() + " CAUSE " + t.getCause());
            }
        });
    }

    @Override
    public void onValidationSucceeded() {
        showProgressDialog();
        handleSignin();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);
            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }
    protected void alertErrorView(String message, Context context) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("Error")
                .setIcon(R.drawable.ic_document_box)
                .setMessage(message)
                //     .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                //      public void onClick(DialogInterface dialoginterface, int i) {
                //          dialoginterface.cancel();
                //          }})
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        dialoginterface.cancel();
//                        Intent intent = new Intent(getBaseContext(), SignInActivity.class);
//                        startActivity(intent);
                    }
                }).show();
    }
}

