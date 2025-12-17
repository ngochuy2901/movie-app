package com.example.myapplication.data.model

data class UserPlaylist(
    val id: Long? = null,
    val userId: Long,
    val title: String? = null,
    val description: String? = null,
    val privacy: String? = null,   // PUBLIC, PRIVATE, UNLISTED
    val createdAt: Long? = null
)