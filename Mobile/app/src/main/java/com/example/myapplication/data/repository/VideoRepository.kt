package com.example.myapplication.data.repository

import com.example.myapplication.data.api.RetrofitInstance
import okhttp3.MultipartBody
import okhttp3.RequestBody

class VideoRepository {
    suspend fun getAllVideos() = RetrofitInstance.videoApi.getAllVideos()

    suspend fun uploadVideo(
        file: MultipartBody.Part,
        thumbnailFile: MultipartBody.Part,
        title: RequestBody,
        description: RequestBody
    ) = RetrofitInstance.videoApi.uploadVideo(file, thumbnailFile, title, description)

    suspend fun liveSearchVideoByTitle(keyword: String) = RetrofitInstance.videoApi.searchVideo(keyword)
}