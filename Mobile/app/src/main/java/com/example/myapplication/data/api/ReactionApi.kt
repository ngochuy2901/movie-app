package com.example.myapplication.data.api

import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ReactionApi {
    @GET("reaction/count_reactions_by_target_id/{targetId}")
    suspend fun countReactionByVideoId(@Path("targetId") videoId: Long): Long

    @POST("reaction/on_reaction_click")
    suspend fun onReactionClick(
        @Query("targetId") targetId: Long
    ): Unit

}