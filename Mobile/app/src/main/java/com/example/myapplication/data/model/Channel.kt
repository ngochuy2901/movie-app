package com.example.myapplication.data.model

data class Channel(
    val id: Long? = null,
    val userId: Long,
    val name: String,
    val description: String? = null,
    val avatarUrl: String? = null,
    val bannerUrl: String? = null,
    val createdAt: Long? = null,
    val statistic: ChannelStatistic? = null
)
