package com.example.myapplication.data.model

import kotlinx.serialization.Serializable


@Serializable
data class User(
    val id: Long? = null,
    val fullName: String,
    val email: String,
    val phoneNumber: String,
    val username: String,
    val password: String,
    val imgUrl: String? = null,
    val gender: String,
    val dateOfBirth: String   // Format: yyyy-MM-dd (giống LocalDate)
)