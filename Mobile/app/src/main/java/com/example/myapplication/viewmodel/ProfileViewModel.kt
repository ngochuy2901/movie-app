package com.example.myapplication.viewmodel

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.auth.Auth
import com.example.myapplication.data.dto.UserDto
import com.example.myapplication.data.model.UserPlaylist
import com.example.myapplication.data.repository.UserPlaylistRepository
import com.example.myapplication.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(private val context : Context)  : ViewModel(){
    val userRepository = UserRepository()
    val userPlaylistRepository = UserPlaylistRepository()
    val auth = Auth(context)
    private val _userDto = MutableStateFlow<UserDto?>(null)
    private val _userPlaylist = MutableStateFlow<List<UserPlaylist>>(emptyList())
    val userDto = _userDto
    val userPlaylist = _userPlaylist

    fun fetchUserInfo() {
        viewModelScope.launch {
            try {
                _userDto.value = userRepository.getUserInfo()
                _userPlaylist.value = userPlaylistRepository.getUserPlaylist()
            } catch (e : Exception) {
                e.printStackTrace()
            }
        }
    }
}

