package com.example.myapplication.data.repository

import com.example.myapplication.data.api.RetrofitInstance
import com.example.myapplication.data.model.Comment


class CommentRepository {

    suspend fun getCommentsByVideoId(videoId: Long): List<Comment> {
        return RetrofitInstance.commentApi.getCommentsByVideoId(videoId)
    }

    suspend fun sendComment(comment: Comment) : Comment {
        return RetrofitInstance.commentApi.sendComment(comment)
    }

    suspend fun saveNewComment(content : String, videoId : Long) : Comment {
        return RetrofitInstance.commentApi.saveNewComment(content, videoId);
    }

    suspend fun countCommentsByVideoId(videoId: Long) : Long {
        return RetrofitInstance.commentApi.countCommentsByVideoId(videoId)
    }
}