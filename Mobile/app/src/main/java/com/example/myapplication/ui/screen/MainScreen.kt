package com.example.myapplication.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.component.BottomNavigationBar
import com.example.myapplication.navigation.NavHostController

@Composable
@RequiresApi(Build.VERSION_CODES.O)
fun MainScreen(modifier: Modifier) {
    val navController = rememberNavController()

    Scaffold(
        topBar = {},
        bottomBar = { BottomNavigationBar(navController) },
        modifier = modifier
    ) { innerPadding ->
        NavHostController(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            navController = navController
        )

    }
}

@Composable
@Preview
@RequiresApi(Build.VERSION_CODES.O)
fun MainPreview() {
    MainScreen(modifier = Modifier.fillMaxSize())
}