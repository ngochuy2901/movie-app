package com.example.myapplication.auth

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.myapplication.data.dto.LoginRequest
import com.example.myapplication.data.dto.LoginResponse
import com.example.myapplication.data.dto.RegisterResponse
import com.example.myapplication.data.model.User
import com.example.myapplication.data.repository.UserRepository
import kotlin.getValue

class Auth(context: Context) {

    companion object {
        private const val PREFS_NAME = "auth_prefs"
        private const val KEY_TOKEN = "auth_token"
    }

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    suspend fun login(loginRequest: LoginRequest): LoginResponse? {
        return try {
            val response = UserRepository().login(loginRequest)
            Log.d("AuthLogin", "Response: $response")

            // Kiểm tra token hợp lệ
            if (!response.token.isNullOrEmpty()) {
                // Lưu token vào SharedPreferences
                saveToken(response.token)
                Log.d("AuthLogin", "Login thành công, token: ${response.token}")
                response
            } else {
                Log.d("AuthLogin", "Login thất bại: token null")
                null
            }

        } catch (e: Exception) {
            Log.e("AuthLogin", "Lỗi login: ${e::class.java} - ${e.message}")
            null
        }
    }

    suspend fun register(user: User): RegisterResponse? {
        return try {
            val response = UserRepository().register(user)
            Log.d("AuthRegister", "Response: $response")
            if (response.id!!>0) {
                Log.d("Thanh cong", "Thanh cong")
                return response
            } else {
                Log.d("Else", "Else")
                return null
            }
        } catch (e: Exception) {
            Log.e("Catch", "Lỗi register: ${e::class.java} - ${e.message}")
            Log.d("Catch", e.message + "")
            null
        }
    }

    // Lưu token sau khi login
    fun saveToken(token: String) {
        prefs.edit().putString(KEY_TOKEN, token).apply()
    }

    // Lấy token (nếu có)
    fun getToken(): String? {
        return prefs.getString(KEY_TOKEN, null)
    }

    // Kiểm tra đã login chưa
    fun isLoggedIn(): Boolean {
        return !getToken().isNullOrEmpty()
    }

    // Xóa token khi logout
    fun logout() {
        prefs.edit().remove(KEY_TOKEN).apply()
    }
}