package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.api.VideoApi
import com.example.myapplication.data.repository.VideoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

class SearchViewModel() : ViewModel() {
    private val videoRepository: VideoRepository = VideoRepository()
    private val _keyword = MutableStateFlow("")
    val keyword = _keyword

    val searchResult = _keyword
        .debounce(400)
        .flatMapLatest { key ->
            flow {
                if (key.isBlank()) {
                    emit(emptyList())
                } else {
                    emit(videoRepository.liveSearchVideoByTitle(key))
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun onKeywordChange(text: String) {
        _keyword.value = text
    }
}
