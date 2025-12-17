package com.example.myapplication.data.api

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor(private val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val sharedPref = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        val token = sharedPref.getString("auth_token", null)

        val originalRequest = chain.request()

        // Không có token → gửi request bình thường
        if (token == null) {
            return chain.proceed(originalRequest)
        }

        // Có token → thêm Authorization
        val newRequest = originalRequest.newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()

        return chain.proceed(newRequest)
    }
}