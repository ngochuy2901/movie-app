package com.example.myapplication.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Video(
        val id: Long? = null,
        val userId : Long,
        val channelId : Long?=null,
        val playlistId : Long?=null,
        val title: String? = null,
        val description: String? = null,
        val url: String? = null,
        val urlThumbnail: String,
        val status: String? = null,        // draft / processing / published / private / deleted / blocked
        val visibility: String? = null,    // public / unlisted / private
        val publishedAt: Long? = null,     // timestamp
        val createdAt: Long? = null,       // timestamp
        val updatedAt: Long? = null        // timestamp
)