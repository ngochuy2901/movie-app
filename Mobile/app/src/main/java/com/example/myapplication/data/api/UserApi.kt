package com.example.myapplication.data.api

import com.example.myapplication.data.dto.LoginRequest
import com.example.myapplication.data.dto.LoginResponse
import com.example.myapplication.data.dto.RegisterResponse
import com.example.myapplication.data.dto.UserDto
import com.example.myapplication.data.model.User
import okhttp3.MultipartBody
import okhttp3.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface UserApi {
    @POST("user/login")
    suspend fun login(@Body loginRequest: LoginRequest) : LoginResponse

    @POST("user/register")
    suspend fun register(@Body user: User) : RegisterResponse

    @GET("user/get_user_info_by_userid/{userId}")
    suspend fun getUserInfoByUserId(@Path("userId") userId : Long) : UserDto

    @GET("user/get_user_info")
    suspend fun getUserInfo() : UserDto

    @POST("user/update_user_avatar")
    @Multipart
    suspend fun updateUserAvatar(@Part file: MultipartBody.Part) : retrofit2.Response<Map<String, String>>
}