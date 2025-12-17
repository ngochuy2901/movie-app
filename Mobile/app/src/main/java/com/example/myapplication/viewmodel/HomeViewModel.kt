package com.example.myapplication.viewmodel

import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.dto.UserDto
import com.example.myapplication.data.model.Video
import com.example.myapplication.data.repository.UserRepository
import com.example.myapplication.data.repository.VideoRepository
import com.example.myapplication.utils.ConfigLoader
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

class HomeViewModel : ViewModel() {
    //repository
    private val videoRepository = VideoRepository()
    private val userRepository = UserRepository()

    //value
    private val _videos = MutableStateFlow<List<Video>>(emptyList())
    val videos: StateFlow<List<Video>> = _videos

    //list url avatar
    private val _urlAvatarOfAuthor = MutableStateFlow<Map<Long, String>>(emptyMap())
    val urlAvatarOfAuthor: StateFlow<Map<Long, String>> = _urlAvatarOfAuthor


    fun fetchData() {
        viewModelScope.launch {
            try {
                //get videos
                _videos.value = videoRepository.getAllVideos()
                Log.d("HomeViewModel", "✅ Received ${_videos.value.size} videos from API")
                //get list avatar url of authors
                val userIds = _videos.value.map { it.userId }.distinct()
                val authorsAvatar = supervisorScope {
                    userIds.map { id ->
                        async {
                            try {
                                id to (userRepository.getUserInfoByUserId(id).imgUrl ?: "")
                            } catch (e: Exception) {
                                Log.e("HomeViewModel", "❌ Failed to load avatar for user $id", e)
                                id to "" // fallback avatar
                            }
                        }
                    }.awaitAll().toMap()
                }

                _urlAvatarOfAuthor.value = authorsAvatar

            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("HomeViewModel", "❌ Error fetching videos: ${e.message}", e)

            }
        }
    }
}