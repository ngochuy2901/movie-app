package com.example.myapplication.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.dto.UserDto
import com.example.myapplication.data.model.Comment
import com.example.myapplication.data.model.Video
import com.example.myapplication.data.repository.CommentRepository
import com.example.myapplication.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class PlayVideoViewModel(private val video: Video) : ViewModel() {
    //repository
    private val commentRepository = CommentRepository()
    private val userRepository = UserRepository()
    //data
    private val _comments = MutableStateFlow<List<Comment>>(emptyList())
    val comments : StateFlow<List<Comment>> = _comments
    private val _author = MutableStateFlow<UserDto?>(null)
    val author : StateFlow<UserDto?> = _author
    private val _commentUsers = MutableStateFlow<Map<Long, UserDto>>(emptyMap())
    val commentUsers: StateFlow<Map<Long, UserDto>> = _commentUsers
    private val  videoId = video.id

    init {
        fetchData()
    }
    fun fetchData() {
        viewModelScope.launch {
            try {
                _comments.value = commentRepository.getCommentsByVideoId(videoId!!);
                _author.value = userRepository.getUserInfoByUserId(video.userId)
                //load commenter info
                val userIds = comments.value.map { it.userId }.distinct()
                Log.d("User info:", _author.value?.fullName?: "")
                _commentUsers.value = userIds.associateWith { userId ->
                    userRepository.getUserInfoByUserId(userId)
                }
                Log.d("PlayVideoViewModel", "✅ Received ${_comments.value.size} videos from API")
            } catch (e : Exception) {
                e.printStackTrace()
                Log.e("PlayVideoViewModel", "❌ Error fetching comments: ${e.message}", e)
            }
        }
    }

    fun saveNewComment(content : String) {
        viewModelScope.launch {
            commentRepository.saveNewComment(content, videoId!!)
            fetchData()
        }
    }
}