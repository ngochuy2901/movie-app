package com.example.myapplication.data.api

import com.example.myapplication.data.model.UserPlaylist
import retrofit2.http.GET

interface UserPlaylistApi {
    @GET("user_playlist/get_user_playlists")
    suspend fun getUserPlaylist() : List<UserPlaylist>
}