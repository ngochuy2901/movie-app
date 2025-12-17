package com.example.myapplication.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.auth.Auth
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navHostController: NavHostController) {
    val context = LocalContext.current   // <-- lấy context
    val auth : Auth = Auth(context)
    LaunchedEffect(true) {
        delay(1500)  // thời gian splash 1.5s (tùy chỉnh)

        if (auth.isLoggedIn()) {
            navHostController.navigate("home") {
                popUpTo("splash") { inclusive = true }
            }
        } else {
            navHostController.navigate("login") {
                popUpTo("splash") { inclusive = true }
            }
        }
    }

    // Splash trống (đại ca muốn để trống tạm thời)
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("SplashScreen")
        // Tạm thời không UI
    }
}

@Composable
@Preview
fun Preview() {
    SplashScreen(rememberNavController())
}

