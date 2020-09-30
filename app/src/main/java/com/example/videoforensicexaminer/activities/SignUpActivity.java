package com.example.videoforensicexaminer.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Checked;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import org.json.JSONException;

import java.util.List;

import  com.example.videoforensicexaminer.R;
import com.example.videoforensicexaminer.api.VideoForensicApiService;
import com.example.videoforensicexaminer.api.RetrofitClientInstance;
import  com.example.videoforensicexaminer.model.ApiResponse;
import  com.example.videoforensicexaminer.model.SignUpRequest;
import  com.example.videoforensicexaminer.utils.ApiUtils;
import  com.example.videoforensicexaminer.utils.Preferences;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends BaseActivity implements View.OnClickListener, Validator.ValidationListener {

    private static final String TAG = "SignUpActivity";

    @NotEmpty
    @Length(min = 4, message = "Name can be 4 to 40 characters long")
    EditText enterName;
    @NotEmpty
    @Email
    EditText enterEmailAddress;
    @NotEmpty
    @Password(min = 6)
    EditText enterPassword;
    @NotEmpty
    @Checked(message = "You must confirm that you are over 18 years old")
    CheckBox isLegalAge;

    RadioButton selectedRadioBtn;
    RadioGroup genderRadioGroup;
    Button submitBtn, alreadyRegisteredBtn;
    Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);

        validator = new Validator(this);
        validator.setValidationListener(this);

        final Preferences preferences = new Preferences(this);

        if (preferences.isUserLoggedIn()) {
            Intent intentMain = new Intent(this, MainActivity.class);
            startActivity(intentMain);
        } else {
            getViewReferences();
            submitBtn.setOnClickListener(this);
            alreadyRegisteredBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intentMain = new Intent(SignUpActivity.this, SignInActivity.class);
                    startActivity(intentMain);
                }
            });
        }
    }

    private void getViewReferences() {
        enterName = findViewById(R.id.enterName);
        enterEmailAddress = findViewById(R.id.enterEmailAddress);
        enterPassword = findViewById(R.id.enterPassword);
        isLegalAge = findViewById(R.id.isLegalAge);
        genderRadioGroup = findViewById(R.id.genderRadioGroup);
        submitBtn = findViewById(R.id.submitBtn);
        alreadyRegisteredBtn = findViewById(R.id.alreadyRegisteredBtn);
    }

    @Override
    public void onClick(View view) {
        // Sign up is handled in the success method of validator
        if (genderRadioGroup.getCheckedRadioButtonId() == -1) {
            // no radio buttons are checked
            Toast.makeText(getApplicationContext(), "Please select Gender", Toast.LENGTH_SHORT).show();
        } else {
            validator.validate();
        }
    }

    private void handleSignup() {
        showProgressDialog();
        int selectedGenderId = genderRadioGroup.getCheckedRadioButtonId();
        selectedRadioBtn = findViewById(selectedGenderId);

//        Log.d(TAG, "onClick: " + enterName.getText().toString() + " " + enterEmailAddress.getText().toString().split("@")[0] + enterEmailAddress.getText().toString()
//                + " " + enterPassword.getText().toString()+ " " + selectedRadioBtn.getText().toString()+ " " + isLegalAge.isChecked());

        SignUpRequest signUpRequest = new SignUpRequest(enterName.getText().toString(), enterEmailAddress.getText().toString().split("@")[0], enterEmailAddress.getText().toString(),
                enterPassword.getText().toString(), selectedRadioBtn.getText().toString(), isLegalAge.isChecked());

        VideoForensicApiService service = RetrofitClientInstance.getRetrofitInstance(this).create(VideoForensicApiService.class);

        Call<ApiResponse> call = service.signUp(signUpRequest);

        Log.d(TAG, "onClick: " + call.request().url());

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                Log.d(TAG, "onResponse: Response code = " + response.code());
                if (response.code() == 200 || response.code() == 201) {
                    hideProgressDialog();
                    alertView("Please check your email for a confirmation link to activate your account.", SignUpActivity.this);
                    Toast.makeText(SignUpActivity.this, "Registration successful! Check your email.", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "API call successful. \n" + response);
                    //Toast.makeText(SignUpActivity.this, "Something went wrong. Please try later!", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        hideProgressDialog();
                        alertErrorView(ApiUtils.errorResponseParser(response.errorBody()).getString("message"), SignUpActivity.this);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //Toast.makeText(SignUpActivity.this, "Something went wrong. Please try later!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(SignUpActivity.this, "Something went wrong. Please try later!", Toast.LENGTH_LONG).show();
                Log.d("TAG", "API call failed. \n" + t.getCause());
            }
        });
    }

    @Override
    public void onValidationSucceeded() {
        handleSignup();
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

    protected void alertView(String message, Context context) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("Almost there!")
                .setIcon(R.drawable.ic_document_box)
                .setMessage(message)
                //     .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                //      public void onClick(DialogInterface dialoginterface, int i) {
                //          dialoginterface.cancel();
                //          }})
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        Intent intent = new Intent(getBaseContext(), SignInActivity.class);
                        startActivity(intent);
                    }
                }).show();
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
