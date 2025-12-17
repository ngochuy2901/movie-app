package com.example.myapplication.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.Comment
import com.example.myapplication.data.repository.CommentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PlayVideoViewModel(private val videoId: Long) : ViewModel() {
    val commentRepository = CommentRepository()

    private val _comments = MutableStateFlow<List<Comment>>(emptyList())

    val comments : StateFlow<List<Comment>> = _comments

    init {
        fetchComments()
    }
    fun fetchComments() {
        viewModelScope.launch {
            try {
                _comments.value = commentRepository.getCommentsByVideoId(videoId);
                Log.d("PlayVideoViewModel", "✅ Received ${_comments.value.size} videos from API")
            } catch (e : Exception) {
                e.printStackTrace()
                Log.e("PlayVideoViewModel", "❌ Error fetching comments: ${e.message}", e)
            }
        }
    }

    fun saveNewComment(content : String) {
        viewModelScope.launch {
            commentRepository.saveNewComment(content, videoId)
            fetchComments()
        }
    }
}