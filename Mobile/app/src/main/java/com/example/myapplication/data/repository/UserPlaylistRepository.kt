package com.example.myapplication.data.repository

import com.example.myapplication.data.api.RetrofitInstance
import com.example.myapplication.data.model.UserPlaylist

class UserPlaylistRepository {
    suspend fun getUserPlaylist() : List<UserPlaylist>{
        return RetrofitInstance.userPlaylistApi.getUserPlaylist()
    }
}