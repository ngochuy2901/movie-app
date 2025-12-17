package com.example.myapplication.data.api

import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitInstance {
//    private const val BASE_URL = "http://10.0.2.2:8080/" // nếu chạy trên emulator
//
//
//    val api: VideoApi by lazy {
//        Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//            .create(VideoApi::class.java)
//    }
//
//    val commentApi: CommentApi by lazy {
//        Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//            .create(CommentApi::class.java)
//    }
//
//    val userApi: UserApi by lazy {
//        Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//            .create(UserApi::class.java)
//    }
//
//    private val client = OkHttpClient.Builder()
//        .addInterceptor(TokenInterceptor(context))
//        .build()
//
//    val retrofit: Retrofit = Retrofit.Builder()
//        .baseUrl("http://10.0.2.2:8080/")
//        .addConverterFactory(GsonConverterFactory.create())
//        .client(client)
//        .build()
fun init(context: Context) {
    client = OkHttpClient.Builder()
        .addInterceptor(TokenInterceptor(context))
        .build()

    retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8080/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
}

    private lateinit var client: OkHttpClient
    lateinit var retrofit: Retrofit

    val commentApi: CommentApi by lazy {
        retrofit.create(CommentApi::class.java)
    }

    val userApi: UserApi by lazy {
        retrofit.create(UserApi::class.java)
    }

    val videoApi: VideoApi by lazy {
        retrofit.create(VideoApi::class.java)
    }

    val userPlaylistApi :UserPlaylistApi by lazy {
        retrofit.create(UserPlaylistApi::class.java)
    }

    val channelApi : ChannelApi by lazy {
        retrofit.create(ChannelApi::class.java)
    }

    val reactionApi : ReactionApi by lazy {
        retrofit.create(ReactionApi::class.java)
    }
}
