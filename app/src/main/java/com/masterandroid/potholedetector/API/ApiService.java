package com.masterandroid.potholedetector.API;

import com.masterandroid.potholedetector.API.DTO.Request.ApiResponse;
import com.masterandroid.potholedetector.API.DTO.Request.RegisterByGmailRequest;
import com.masterandroid.potholedetector.API.DTO.Request.RegisterByUserRequest;
import com.masterandroid.potholedetector.API.DTO.Response.RegisterResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("/pothole/register/user")
    Call<ApiResponse<RegisterResponse>> registerByUser(@Body RegisterByUserRequest request);

    @POST("/pothole/register/gmail")
    Call<ApiResponse<RegisterResponse>> registerByGmail(@Body RegisterByGmailRequest request);
}
