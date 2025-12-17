package com.example.myapplication.data.dto

class UserDto (
    val id: Long? = null,
    val fullName: String?=null,
    val email: String?=null,
    val phoneNumber: String?=null,
    val username: String?=null,
    val imgUrl: String? = null,
    val gender: String?=null,
    val dateOfBirth: String?=null   // Format: yyyy-MM-dd (giống LocalDate)
)