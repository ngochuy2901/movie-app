package com.example.myapplication.data.model

data class Image(
    val id: Long? = null,
    val userId: Long? = null,
//    val title: String? = null,
//    val description: String? = null,
    val url: String? = null,
    val status: String? = null,       // draft / processing / published / private / deleted / blocked
    val visibility: String? = null,   // public / unlisted / private
    val isPrivate: Boolean? = false,
    val publishedAt: Long? = null,    // timestamp
    val createdAt: Long? = null,      // timestamp
    val updatedAt: Long? = null       // timestamp
)
