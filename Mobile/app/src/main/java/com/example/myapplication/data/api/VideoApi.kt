package com.example.myapplication.data.api

import com.example.myapplication.data.model.Video
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface VideoApi {
    @GET("video/all_videos")
    suspend fun getAllVideos(): List<Video>

    @Multipart
    @POST("video/upload")
    suspend fun uploadVideo(
        @Part file: MultipartBody.Part,
        @Part thumbnail: MultipartBody.Part,
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody
    ): Response<Map<String, String>>

}