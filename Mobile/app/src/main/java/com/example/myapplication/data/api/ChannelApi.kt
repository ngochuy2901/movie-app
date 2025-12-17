package com.example.myapplication.data.api

import com.example.myapplication.data.model.Channel
import retrofit2.http.GET

interface ChannelApi {
    @GET("channel/find_channel_by_token")
    suspend fun getChannelsByUser() : List<Channel>
}