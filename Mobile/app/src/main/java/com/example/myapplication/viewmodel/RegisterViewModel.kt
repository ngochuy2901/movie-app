package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import com.example.myapplication.data.repository.UserRepository

class RegisterViewModel : ViewModel() {

    val userRepository = UserRepository()

}