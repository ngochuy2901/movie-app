package com.example.myapplication.data.dto

import java.time.LocalDate

data class RegisterResponse(
    val id: Long?,
    val fullname: String?,
    val username: String?,
    val email: String?,
    val phoneNumber: String?,
    val img_url: String?,
    val gender: String?,
    val date_of_birth: String?
)