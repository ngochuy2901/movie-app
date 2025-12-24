package com.example.myapplication.navigation

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.myapplication.data.model.Video
import com.example.myapplication.ui.screen.ChannelScreen

import com.example.myapplication.ui.screen.HomeScreen
import com.example.myapplication.ui.screen.LoginScreen
import com.example.myapplication.ui.screen.NotificationScreen
import com.example.myapplication.ui.screen.PlayVideoScreen
import com.example.myapplication.ui.screen.ProfileScreen
import com.example.myapplication.ui.screen.RegisterScreen
import com.example.myapplication.ui.screen.SearchVideoScreen
import com.example.myapplication.ui.screen.SettingScreen
import com.example.myapplication.ui.screen.ShortVideoScreen
import com.example.myapplication.ui.screen.SplashScreen
import com.example.myapplication.ui.screen.UploadProfilePictureScreen
import com.example.myapplication.ui.screen.UploadVideoScreen
import kotlinx.serialization.json.Json


@Composable
@RequiresApi(Build.VERSION_CODES.O)
fun NavHostController(modifier: Modifier, navController: NavHostController) {
//    var currentRoute = navController.currentDestination?.route
    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = modifier
    ) {
        composable("home") { HomeScreen(navHostController = navController) }
        composable("short") { ShortVideoScreen() }
        composable("profile") { ProfileScreen(navController) }
        composable("channel") { ChannelScreen(navHostController = navController) }
        composable ( "login" ) { LoginScreen(navHostController = navController) }
        composable("splash") { SplashScreen(navController) }
        composable("upload_profile_picture") { UploadProfilePictureScreen(navController) }
        composable ( "register" ) { RegisterScreen(navHostController = navController) }
        composable("upload_video") { UploadVideoScreen(navHostController = navController) }
        composable ("setting") { SettingScreen(navController) }
        composable ("notification") { NotificationScreen(navController) }
        composable ("search_video") { SearchVideoScreen(navController) }
        composable("playvideo/{videoJson}") { backStackEntry ->
            val videoJson = backStackEntry.arguments?.getString("videoJson")
            val video = Json.decodeFromString<Video>(Uri.decode(videoJson ?: ""))
            PlayVideoScreen(video) }
    }

}

