package com.example.myapplication.data.dto

import com.example.myapplication.data.model.User

data class LoginResponse(
    val status: Int,
    val message: String?,
    val token: String?
)
