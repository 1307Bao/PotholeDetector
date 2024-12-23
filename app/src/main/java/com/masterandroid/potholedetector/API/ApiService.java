package com.masterandroid.potholedetector.API;

import com.masterandroid.potholedetector.API.DTO.Point;
import com.masterandroid.potholedetector.API.DTO.Request.ApiResponse;
import com.masterandroid.potholedetector.API.DTO.Request.DetectingRequest;
import com.masterandroid.potholedetector.API.DTO.Request.IntrospectRequest;
import com.masterandroid.potholedetector.API.DTO.Request.LoginByFacebookRequest;
import com.masterandroid.potholedetector.API.DTO.Request.LoginByGmailRequest;
import com.masterandroid.potholedetector.API.DTO.Request.LoginByUserRequest;
import com.masterandroid.potholedetector.API.DTO.Request.LogoutRequest;
import com.masterandroid.potholedetector.API.DTO.Request.PotholePotentialRequest;
import com.masterandroid.potholedetector.API.DTO.Request.PotholeRequest;
import com.masterandroid.potholedetector.API.DTO.Request.RegisterByFacebookRequest;
import com.masterandroid.potholedetector.API.DTO.Request.RegisterByGmailRequest;
import com.masterandroid.potholedetector.API.DTO.Request.RegisterByUserRequest;
import com.masterandroid.potholedetector.API.DTO.Request.ResetPasswordRequest;
import com.masterandroid.potholedetector.API.DTO.Request.RouteRequest;
import com.masterandroid.potholedetector.API.DTO.Response.AuthenticationResponse;
import com.masterandroid.potholedetector.API.DTO.Response.LoginResponse;
import com.masterandroid.potholedetector.API.DTO.Response.PotholeDetectedResponse;
import com.masterandroid.potholedetector.API.DTO.Response.PotholeInfoResponse;
import com.masterandroid.potholedetector.API.DTO.Response.PotholePotentialResponse;
import com.masterandroid.potholedetector.API.DTO.Response.PotholeResponse;
import com.masterandroid.potholedetector.API.DTO.Response.RegisterResponse;
import com.masterandroid.potholedetector.API.DTO.Response.ReportResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HEAD;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
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

    @GET("/pothole/potholes")
    Call<ApiResponse<List<PotholeResponse>>> getPotholes();

    @POST("/pothole/potholes/potholes-on-map")
    Call<ApiResponse<List<PotholeResponse>>> getAllPotholeOnMap(@Body List<RouteRequest> requests);

    @POST("/pothole/potholes/insert-potential-pothole")
    Call<ApiResponse<Void>> addMorePotentialPothole(@Query("longitude") double longitude,
                                          @Query("latitude") double latitude);

    @POST("/pothole/potholes/met-pothole")
    Call<Void> metPothole(@Body PotholeRequest request);

    @GET("/pothole/potholes/all-pothole-potential")
    Call<List<PotholePotentialResponse>> getAllPotholePotential();

    @POST("/pothole/potholes/create")
    Call<ApiResponse<Void>> createPothole(@Query("id") String id);

    @PATCH("/pothole/potholes/delete")
    Call<ApiResponse<Void>> deletePothole(@Query("id") String id);

    @GET("/pothole/users/my-pothole-info")
    Call<PotholeInfoResponse> getMyPotholeInfo();

    @GET("/pothole/users/my-pothole-detected")
    Call<List<PotholeDetectedResponse>> getMyPotholeDetected();

    @GET("/pothole/users/report")
    Call<ReportResponse> getReport();

}
