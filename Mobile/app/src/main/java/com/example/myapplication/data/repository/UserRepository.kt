package com.example.myapplication.data.repository

import com.example.myapplication.data.api.RetrofitInstance
import com.example.myapplication.data.dto.LoginRequest
import com.example.myapplication.data.dto.LoginResponse
import com.example.myapplication.data.dto.RegisterResponse
import com.example.myapplication.data.dto.UserDto
import com.example.myapplication.data.model.User
import okhttp3.MultipartBody

class UserRepository {
    suspend fun register(user: User) : RegisterResponse {
        return RetrofitInstance.userApi.register(user)
    }
    suspend fun login(loginRequest: LoginRequest) : LoginResponse {
        return RetrofitInstance.userApi.login(loginRequest)
    }

    suspend fun getUserInfoByUserId(userId : Long) : UserDto {
        return RetrofitInstance.userApi.getUserInfoByUserId(userId)
    }

    suspend fun getUserInfo() : UserDto {
        return RetrofitInstance.userApi.getUserInfo()
    }

    suspend fun updateUserAvatar(file: MultipartBody.Part) = RetrofitInstance.userApi.updateUserAvatar(file)
}