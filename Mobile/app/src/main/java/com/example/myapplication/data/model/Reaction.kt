package com.example.myapplication.data.model

data class Reaction(
    val id: Long? = null,
    val userId: Long,
    val targetId: Long,
    val createdAt: String? = null
)