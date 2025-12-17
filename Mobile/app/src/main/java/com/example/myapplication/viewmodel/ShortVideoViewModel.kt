package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.dto.UserDto
import com.example.myapplication.data.model.Comment
import com.example.myapplication.data.model.Video
import com.example.myapplication.data.repository.CommentRepository
import com.example.myapplication.data.repository.ReactionRepository
import com.example.myapplication.data.repository.UserRepository
import com.example.myapplication.data.repository.VideoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ShortVideoViewModel() : ViewModel() {
    //repository
    private val videoRepository: VideoRepository = VideoRepository()
    private val commentRepository: CommentRepository = CommentRepository()
    private val reactionRepository: ReactionRepository = ReactionRepository()

    //
    private val _videos = MutableStateFlow<List<Video>>(emptyList())
    val videos: StateFlow<List<Video>> = _videos
    //
    private val _numberOfReactions = MutableStateFlow(0L)
    val numberOfReactions: StateFlow<Long> = _numberOfReactions
    //
    private val _numberOfComments = MutableStateFlow(0L)
    val numberOfComments: StateFlow<Long> = _numberOfComments


    private val _comments = MutableStateFlow<List<Comment>>(emptyList())
    val comments: StateFlow<List<Comment>> = _comments
    private val _userAvatarUrl = MutableStateFlow("")
    val userAvatarUrl: StateFlow<String> = _userAvatarUrl

    private val userRepository: UserRepository = UserRepository()
    private val _commentUsers = MutableStateFlow<Map<Long, UserDto>>(emptyMap())
    val commentUsers: StateFlow<Map<Long, UserDto>> = _commentUsers


    fun loadData() {
        viewModelScope.launch {
            try {
                _videos.value = videoRepository.getAllVideos() // gọi API lấy tất cả video
                _userAvatarUrl.value = userRepository.getUserInfo().imgUrl ?: ""
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun saveNewComment(content: String, videoId: Long) {
        viewModelScope.launch {
            commentRepository.saveNewComment(content, videoId)
            loadComments(videoId)
        }
    }

    fun onLikeReaction(videoId : Long) {
        viewModelScope.launch {
            reactionRepository.onReactionClick(videoId)
        }
    }

    fun loadReaction(videoId: Long) {
        viewModelScope.launch {
            _numberOfComments.value = commentRepository.countCommentsByVideoId(videoId)
            _numberOfReactions.value = reactionRepository.countReactionByTargetId(videoId )
        }
    }

    fun loadComments(idVideo: Long) {
        viewModelScope.launch {
            try {
                _comments.value = commentRepository.getCommentsByVideoId(idVideo)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun loadUsersForComments(comments: List<Comment>) {
        viewModelScope.launch {
            try {
                val userIds = comments.map { it.userId }.distinct()

                val usersMap = userIds.associateWith { id ->
                    userRepository.getUserInfoByUserId(id)
                }

                _commentUsers.value = usersMap
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}