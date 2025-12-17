package com.example.myapplication.data.repository

import com.example.myapplication.data.api.RetrofitInstance
import com.example.myapplication.data.model.Channel

class ChannelRepository {
    suspend fun getChannelsByUser(): List<Channel> {
        return RetrofitInstance.channelApi.getChannelsByUser();
    }
}