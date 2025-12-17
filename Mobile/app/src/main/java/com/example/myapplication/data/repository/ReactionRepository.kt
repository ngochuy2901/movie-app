package com.example.myapplication.data.repository

import com.example.myapplication.data.api.RetrofitInstance

class ReactionRepository {
    suspend fun countReactionByTargetId(targerId : Long) : Long {
        return RetrofitInstance.reactionApi.countReactionByVideoId(targerId)
    }

    suspend fun onReactionClick(targetId : Long) {
        return RetrofitInstance.reactionApi.onReactionClick(targetId)
    }
}