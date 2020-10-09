package com.example.videoforensicanalysis.api;

import java.util.List;

import com.example.videoforensicanalysis.model.ApiResponse;
import com.example.videoforensicanalysis.model.JwtAuthenticationResponse;
import com.example.videoforensicanalysis.model.SignInRequest;
import com.example.videoforensicanalysis.model.SignUpRequest;
import com.example.videoforensicanalysis.model.UploadFileResponse;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface VideoForensicApiService {
    @POST("auth/signup")
    Call<ApiResponse> signUp(@Body SignUpRequest signUpRequest);

    @POST("auth/signin")
    Call<JwtAuthenticationResponse> signIn(@Body SignInRequest signInRequest);

    @Multipart
    @POST("video/files")
    Call<UploadFileResponse> uploadFile(@Header("recording-env") String recordingEnv,
                                        @Header("corpus-id") String corpusId,
                                        @Header("device-name") String deviceName,
                                        @Header("mask-type") String maskType,
                                        @Part MultipartBody.Part files);
    @GET("video/files")
    Call<List<String>> listFilesForLoggedInUser();
}