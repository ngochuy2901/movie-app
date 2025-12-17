package com.example.myapplication

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.data.api.RetrofitInstance
import com.example.myapplication.ui.screen.LoginScreen
import com.example.myapplication.ui.screen.MainScreen
import com.example.myapplication.ui.screen.SplashScreen
import com.example.myapplication.ui.theme.MyApplicationTheme

@RequiresApi(Build.VERSION_CODES.O)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RetrofitInstance.init(this)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainScreen(modifier = Modifier.fillMaxSize().navigationBarsPadding())
            }
        }
    }
}

