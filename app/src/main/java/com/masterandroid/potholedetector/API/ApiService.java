package com.masterandroid.potholedetector.API;

import com.masterandroid.potholedetector.API.DTO.Request.ApiResponse;
import com.masterandroid.potholedetector.API.DTO.Request.IntrospectRequest;
import com.masterandroid.potholedetector.API.DTO.Request.LoginByFacebookRequest;
import com.masterandroid.potholedetector.API.DTO.Request.LoginByGmailRequest;
import com.masterandroid.potholedetector.API.DTO.Request.LoginByUserRequest;
import com.masterandroid.potholedetector.API.DTO.Request.LogoutRequest;
import com.masterandroid.potholedetector.API.DTO.Request.RegisterByFacebookRequest;
import com.masterandroid.potholedetector.API.DTO.Request.RegisterByGmailRequest;
import com.masterandroid.potholedetector.API.DTO.Request.RegisterByUserRequest;
import com.masterandroid.potholedetector.API.DTO.Request.ResetPasswordRequest;
import com.masterandroid.potholedetector.API.DTO.Response.AuthenticationResponse;
import com.masterandroid.potholedetector.API.DTO.Response.LoginResponse;
import com.masterandroid.potholedetector.API.DTO.Response.RegisterResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    @POST("/pothole/register/user")
    Call<ApiResponse<RegisterResponse>> registerByUser(@Body RegisterByUserRequest request);

    @POST("/pothole/register/gmail")
    Call<ApiResponse<RegisterResponse>> registerByGmail(@Body RegisterByGmailRequest request);

    @POST("/pothole/register/facebook")
    Call<ApiResponse<RegisterResponse>> registerByFacebook(@Body RegisterByFacebookRequest request);

    @POST("/pothole/login/user")
    Call<ApiResponse<LoginResponse>> loginByUser(@Body LoginByUserRequest request);

    @POST("/pothole/login/gmail")
    Call<ApiResponse<LoginResponse>> loginByGmail(@Body LoginByGmailRequest request);

    @POST("/pothole/login/facebook")
    Call<ApiResponse<LoginResponse>> loginByFacebook(@Body LoginByFacebookRequest request);

    @POST("/pothole/auth/forgot-password")
    Call<ApiResponse<String>> getOTPForResetPassword(@Query("email") String email);

    @POST("/pothole/auth/validate-otp")
    Call<ApiResponse<String>> validateOtp(@Query("email") String email, @Query("otp") String otp);
    
    @POST("/pothole/auth/reset-password")
    Call<ApiResponse<String>> resetPassword(@Body ResetPasswordRequest request);

    @POST("/pothole/auth/introspect")
    Call<ApiResponse<AuthenticationResponse>> introspect(@Body IntrospectRequest request);

    @POST("/pothole/auth/logout")
    Call<ApiResponse<AuthenticationResponse>> logout(@Body LogoutRequest logoutRequest);
}
