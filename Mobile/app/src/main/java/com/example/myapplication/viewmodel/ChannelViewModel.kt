package com.example.myapplication.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.Channel


import com.example.myapplication.data.repository.ChannelRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChannelViewModel : ViewModel(){
    val channelRepository = ChannelRepository()
    private val _channels = MutableStateFlow<List<Channel>>(emptyList())
    val channels: StateFlow<List<Channel>> = _channels

    fun fetchChannel() {
        viewModelScope.launch {
            try {
                _channels.value = channelRepository.getChannelsByUser()
                Log.d("Channel ViewModel", "✅ Received ${_channels.value.size} videos from API")

            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("Channel ViewModel", "❌ Error fetching channels: ${e.message}", e)

            }
        }
    }
}