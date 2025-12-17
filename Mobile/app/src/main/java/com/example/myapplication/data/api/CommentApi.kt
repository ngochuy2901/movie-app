package com.example.myapplication.data.api

import com.example.myapplication.data.model.Comment
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


interface CommentApi {
    @GET("comment/{videoId}")
    suspend fun getCommentsByVideoId(@Path("videoId") videoId: Long): List<Comment>;

    @GET("comment/number_of_comments/{videoId}")
    suspend fun countCommentsByVideoId(@Path("videoId") videoId: Long) : Long

    @POST("comment/send_comment")
    suspend fun sendComment(@Body comment: Comment): Comment

    @POST("comment/save_new_comment")
    suspend fun saveNewComment(
        @Query("content") content: String,
        @Query("videoId") videoId: Long
    ): Comment
}
