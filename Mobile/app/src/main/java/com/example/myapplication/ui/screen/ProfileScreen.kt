package com.example.myapplication.ui.screen

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Icon
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.myapplication.R
import com.example.myapplication.auth.Auth
import com.example.myapplication.data.dto.UserDto
import com.example.myapplication.data.model.UserPlaylist
import com.example.myapplication.utils.ConfigLoader
import com.example.myapplication.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    navHostController: NavHostController,
    onLoginClick: () -> Unit = { navHostController.navigate("login") },
    onRegisterClick: () -> Unit = { navHostController.navigate("register") },
) {

    val context = LocalContext.current
    val viewModel: ProfileViewModel = viewModel(
        factory = ProfileViewModelFactory(context)
    )
    val auth = Auth(context)
    val userDto by viewModel.userDto.collectAsState()
    val userPlaylist by viewModel.userPlaylist.collectAsState()



    if (auth.isLoggedIn()) {
        LaunchedEffect(Unit) {
            viewModel.fetchUserInfo()
        }
        Column() {
            if (userDto != null) {
                UserInformation(userDto!!, navHostController)
            }
            YouSection(userPlaylist)
        }
    } else {
        AlertDialog(
            onDismissRequest = { /* Không làm gì để bắt user chọn */ },
            title = {
                Text("Bạn chưa đăng nhập")
            },
            confirmButton = {
                TextButton(onClick = onLoginClick) {
                    Text("Đăng nhập")
                }
            },
            dismissButton = {
                TextButton(onClick = onRegisterClick) {
                    Text("Đăng ký")
                }
            }
        )
    }

}


@Composable
@Preview
fun ProfileScreenPreview() {
    ProfileScreen(rememberNavController())
}

@Composable
fun UserPlaylist(playlists: List<UserPlaylist>) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(playlists.size) { index ->
            UserPlaylistItem(playlists[index])
        }
    }
}

@Composable
fun UserPlaylistItem(userPlaylist: UserPlaylist) {
    Column(
        modifier = Modifier.width(160.dp)
    ) {
        // Thumbnail (fake)
        Image(
            painter = painterResource(R.drawable.ic_launcher_background),
            contentDescription = null,
            modifier = Modifier
                .height(90.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = userPlaylist.title ?: "",
                maxLines = 1,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )

            Icon(
                painter = painterResource(R.drawable.icon_more_vert),
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}


@Composable
@Preview
fun UserPlaylistPreview() {
    UserPlaylist(samplePlaylists)
}

@Composable
fun UserInformation(userDto: UserDto, navHostController: NavHostController) {
    val imgUrlHeadPoint = ConfigLoader.get(LocalContext.current, "IMG_URL_PUBLIC")
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        ProfileTopIcons(navHostController)
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            UserAvatar(
                imgUrlHeadPoint + userDto.imgUrl, modifier = Modifier
                    .clickable {
                        navHostController.navigate("upload_profile_picture")
                    }
                    .size(60.dp)
                    .clip(CircleShape))

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = userDto.fullName!!,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = userDto.username!!,
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- Hàng nút hành động (LazyRow) ---
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            item {
                UserActionItem(
                    iconRes = R.drawable.icon_switch_account,
                    title = "Chuyển đổi tài khoản"
                )
            }
            item {
                UserActionItem(
                    iconRes = R.drawable.icon_anonymous,
                    title = "Bật chế độ ẩn danh"
                )
            }
            item {
                UserActionItem(
                    iconRes = R.drawable.icon_share,
                    title = "Chia sẻ kênh"
                )
            }
        }
    }
}

@Composable
@Preview
fun UserInfoPreview() {
    UserInformation(userDto, rememberNavController())
}

@Composable
fun ProfileTopIcons(navHostController: NavHostController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.End, // khoảng cách đều giữa icon
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val iconSpacing = 16.dp

        IconButton(onClick = {}) {
            Icon(
                painterResource(R.drawable.icon_cast),
                contentDescription = "Cast",

                modifier = Modifier.size(28.dp)
            )
        }


        IconButton(onClick = {
            navHostController.navigate("notification")
        }) {
            Icon(
                painterResource(R.drawable.icon_notification),
                contentDescription = "Notification",

                modifier = Modifier.size(28.dp)
            )
        }


        IconButton(onClick = {}) {
            Icon(
                painterResource(R.drawable.icon_search),
                contentDescription = "Search",

                modifier = Modifier.size(28.dp)
            )
        }

        IconButton(onClick = {
            navHostController.navigate("setting")
        }) {
            Icon(
                painterResource(R.drawable.icon_setting),
                contentDescription = "Settings",

                modifier = Modifier.size(28.dp)
            )
        }
    }
}


@Composable
fun UserActionItem(iconRes: Int, title: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* TODO: handle click */ }
            .padding(vertical = 10.dp, horizontal = 16.dp)
    ) {
        // Icon trong vòng tròn nền xám
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,

                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Tiêu đề
        Text(
            text = title,
            fontSize = 15.sp,

            fontWeight = FontWeight.Medium
        )
    }
}


@Composable
fun YouSection(userPlaylist: List<UserPlaylist>) {
    Column {
        YouSectionItem(R.drawable.icon_smart_display, "Video cua ban")
        YouSectionItem(R.drawable.icon_playlist_play, "Danh sach phat")
        UserPlaylist(userPlaylist)
        YouSectionItem(R.drawable.icon_download, "Noi dung tai xuong")
        YouSectionItem(R.drawable.icon_school_cap, "Khoa hoc cua ban")
        Divider(
            color = Color(0xFFE0E0E0),
            thickness = 1.dp,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        YouSectionItem(R.drawable.icon_help, "Tro giup va phan hoi")
    }
}

@Composable
fun YouSectionItem(
    iconRes: Int,
    title: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium.copy(

                fontWeight = FontWeight.Medium
            )
        )
    }
}


@Composable
fun UserAvatar(url: String, modifier: Modifier) {
    val context = LocalContext.current
    val auth = Auth(context)
//    val token = auth.getToken()
    AsyncImage(
        model = ImageRequest.Builder(context)
            .data(url)
//            .addHeader("Authorization", "Bearer $token")   // 🌟 THÊM TOKEN VÀO HEADER
            .crossfade(true)
            .build(),
        contentDescription = null,
        placeholder = painterResource(R.drawable.icon_person),
        error = painterResource(R.drawable.icon_person),
        modifier = modifier
    )
}

class ProfileViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProfileViewModel(context) as T
    }
}

@Composable
@Preview
fun YouSectionPreview() {
    YouSection(samplePlaylists)
}

val userDto = UserDto(
    id = 1L, // có thể bỏ nếu muốn để null
    fullName = "Nguyễn Văn A",
    email = "nguyenvana@example.com",
    phoneNumber = "0123456789",
    username = "nguyenvana",
    imgUrl = "https://example.com/avatar.jpg", // có thể bỏ = null
    gender = "Male",
    dateOfBirth = "1990-01-01"
)
val samplePlaylists = listOf(
    UserPlaylist(
        1,
        1,
        "Video đã thích",
        "Danh sách video đã thích của người dùng",
        "PRIVATE",
        System.currentTimeMillis()
    ),
    UserPlaylist(
        2,
        1,
        "Lập trình Android",
        "Tổng hợp video hướng dẫn Android Kotlin",
        "PUBLIC",
        System.currentTimeMillis()
    ),
    UserPlaylist(
        3,
        1,
        "Nhạc Lofi",
        "Playlist nhạc chill nghe khi làm việc",
        "PUBLIC",
        System.currentTimeMillis()
    ),
    UserPlaylist(
        4,
        1,
        "Học Spring Boot",
        "Video backend, Spring Security, JPA",
        "UNLISTED",
        System.currentTimeMillis()
    ),
    UserPlaylist(5, 1,"Xem sau", "Videos lưu để xem sau", "PRIVATE", System.currentTimeMillis()),

    UserPlaylist(
        6,
        1,
        "Công thức nấu ăn",
        "Các món ăn dễ làm tại nhà",
        "PUBLIC",
        System.currentTimeMillis()
    ),
    UserPlaylist(
        7,
        1,
        "Travel Vlog",
        "Tổng hợp các video du lịch yêu thích",
        "PUBLIC",
        System.currentTimeMillis()
    ),
    UserPlaylist(
        8,
        1,
        "Nhạc Gym",
        "Playlist nghe khi tập luyện",
        "PUBLIC",
        System.currentTimeMillis()
    ),
    UserPlaylist(
        9,
        1,
        "Học SEO",
        "Các video hướng dẫn SEO & Marketing",
        "UNLISTED",
        System.currentTimeMillis()
    ),
    UserPlaylist(
        10,
        1,
        "Video đã thích",
        "Danh sách video đã thích",
        "PRIVATE",
        System.currentTimeMillis()
    )
)