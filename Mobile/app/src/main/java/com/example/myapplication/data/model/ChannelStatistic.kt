package com.example.myapplication.data.model

data class ChannelStatistic(
    val id: Long? = null,
    val subscriberCount: Long = 0,
    val totalViews: Long = 0,
    val videoCount: Long = 0,
    val channel: Channel? = null
)
