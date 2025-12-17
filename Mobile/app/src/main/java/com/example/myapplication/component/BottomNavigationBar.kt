package com.example.myapplication.component


import android.graphics.Bitmap
import android.net.Uri
import android.os.FileUtils

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.R


@Composable
fun BottomNavigationBar(navHostController: NavHostController) {
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        BottomNavigationBarItem(
            R.drawable.icon_home,
            "Trang chủ",
            currentRoute == "home",
            modifier = Modifier.clickable {
                navHostController.navigate("home")
//                if (currentRoute != "home") {
//                    navHostController.navigate("home")
//                }
            })
        BottomNavigationBarItem(
            R.drawable.icon_smart_display,
            "Shorts",
            currentRoute == "short",
            modifier = Modifier.clickable {
                navHostController.navigate("short")
            })
        BottomNavigationBarItem(
            R.drawable.icon_add,
            "",
            isSelected = false,
            isCenter = true,
            modifier = Modifier.clickable {
                navHostController.navigate("upload_video")
            })
        BottomNavigationBarItem(
            R.drawable.icon_smart_display,
            "Kênh đăng ký",
            currentRoute == "channel",
            modifier = Modifier.clickable {
                navHostController.navigate("channel")
            })
        BottomNavigationBarItem(
            R.drawable.icon_person,
            "Bạn",
            currentRoute == "profile",
            modifier = Modifier.clickable {
                navHostController.navigate("profile")
            })
    }
}

@Composable
fun BottomNavigationBarItem(
    iconRes: Int,
    title: String,
    isSelected: Boolean,
    isCenter: Boolean = false,
    modifier: Modifier = Modifier
) {
    val iconTint = if (isSelected) Color.White else Color.Gray

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .then(modifier)
//            .clickable { /* TODO: Handle navigation */ }
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = title,
            tint = iconTint,
            modifier = Modifier
                .size(if (isCenter) 36.dp else 26.dp)
                .padding(bottom = if (isCenter) 0.dp else 2.dp)
        )
        if (title.isNotEmpty()) {
            Text(
                text = title,
                fontSize = 11.sp,
                color = iconTint,
                textAlign = TextAlign.Center
            )
        }
    }
}


@Composable
@Preview
fun BottomNavigationBarPreview() {
    BottomNavigationBar(rememberNavController())
}

