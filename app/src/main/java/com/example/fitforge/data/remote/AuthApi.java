package com.example.fitforge.data.remote;

import com.example.fitforge.data.remote.dto.RegisterRequest;
import com.example.fitforge.data.remote.dto.RegisterResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApi {

    @POST("auth/register")
    Call<RegisterResponse> register(@Body RegisterRequest request);

    @POST("auth/login")
    Call<RegisterResponse> login(@Body com.example.fitforge.data.remote.dto.LoginRequest request);

}