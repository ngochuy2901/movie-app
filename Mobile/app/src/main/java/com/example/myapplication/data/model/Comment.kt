package com.example.myapplication.data.model

data class Comment(
    val id: Long? = null,
    val userId: Long,
    val videoId: Long,
    val content: String,
    val createdAt: String?,
    val updatedAt: String?
)