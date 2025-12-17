package com.example.myapplication.ui.screen

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.myapplication.R
import com.example.myapplication.auth.Auth
import com.example.myapplication.data.model.Video
import com.example.myapplication.utils.ConfigLoader
import com.example.myapplication.viewmodel.HomeViewModel

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    navHostController: NavHostController
) {
    val imgUrlHeadPoint = ConfigLoader.get(LocalContext.current, "IMG_URL_PUBLIC")
    val token = Auth(LocalContext.current).getToken()
    val listVideos by viewModel.videos.collectAsState();
    var isRefreshing by remember { mutableStateOf(false) }
    val urlAvatarOfAuthor by viewModel.urlAvatarOfAuthor.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchData()
    }
    Column(modifier = Modifier.fillMaxSize()) {
        HomeHeader(navHostController)
        ListVideo(
            modifier = Modifier.padding(10.dp, 0.dp),
            videoList = listVideos,
            isRefreshing = isRefreshing,
            onRefresh = {
                isRefreshing = true
                viewModel.fetchData()
                isRefreshing = false
            },
            navHostController = navHostController,
            thumbnailEndpoint = imgUrlHeadPoint,
            urlAvatarsOfAuthors = urlAvatarOfAuthor
//            token = token!!
        )
    }
}

@Composable
@Preview
fun HomeScreenPreview() {
//    HomeScreen(viewModel = HomeViewModel(), rememberNavController())
}

@Composable
fun HomeHeader(navHostController: NavHostController) {
    Row {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.End, // khoảng cách đều giữa icon
            verticalAlignment = Alignment.CenterVertically,
        ) {

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
        }
    }
}

@Composable
@Preview
fun HomeHeaderPreview() {
    HomeHeader(rememberNavController())
}

@Preview
@Composable
fun VideoItemPreview() {
//    HomeScreen()
    val video = Video(
        id = 1,
        userId = 1,
        title = "Hướng dẫn làm app xem video",
        description = "Video hướng dẫn làm ứng dụng xem video tương tự YouTube bằng Kotlin",
        url = "huong-dan-lam-app-xem-video",
        urlThumbnail = "huong-dan-lam-app-xem-video",
        status = "published",
        visibility = "public",
    )
    VideoItem(video, modifier = Modifier.fillMaxWidth(), rememberNavController(), "", "")
}

@Composable
fun VideoItem(
    video: Video,
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    thumbnailEndpoint: String,
    urlAvatarOfAuthor: String
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .clickable {
                val videoJson = Uri.encode(Json.encodeToString(video))
                navHostController.navigate("playvideo/$videoJson")
            }
    ) {

        // Ảnh thumbnail video
        Log.d("Receive image from:", thumbnailEndpoint + video.urlThumbnail)
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(thumbnailEndpoint + video.urlThumbnail)
                .crossfade(true)
                .build(),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )


        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Ảnh avatar channel
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(thumbnailEndpoint + urlAvatarOfAuthor)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                placeholder = painterResource(R.drawable.icon_person),
                error = painterResource(R.drawable.icon_person),
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
            )


            Spacer(modifier = Modifier.width(8.dp))

            // Tiêu đề + mô tả
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            ) {
                Text(
                    text = video.title + "",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = video.description + "",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Nút menu (3 chấm)
            IconButton(onClick = { /* TODO: Show bottom sheet */ }) {
                Icon(
                    painter = painterResource(R.drawable.icon_more_vert),
                    contentDescription = null,
                    tint = Color.Gray
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListVideo(
    modifier: Modifier,
    videoList: List<Video>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    navHostController: NavHostController,
    thumbnailEndpoint: String,
    urlAvatarsOfAuthors: Map<Long, String>
) {
    val state = rememberPullToRefreshState()
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        state = state,
        indicator = {
            Indicator(
                modifier = Modifier.align(Alignment.TopCenter),
                isRefreshing = isRefreshing,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                state = state
            )
        },
    ) {
        LazyColumn(modifier = modifier.fillMaxSize()) {
            items(videoList) { video ->
                VideoItem(
                    video, navHostController = navHostController,
                    thumbnailEndpoint = thumbnailEndpoint,
                    urlAvatarOfAuthor = urlAvatarsOfAuthors[video.userId] ?: ""
                )
            }
        }
    }

}

@Composable
@Preview
fun ListVideoPreview() {
    ListVideo(
        modifier = Modifier.padding(5.dp),
        videoList, isRefreshing = false,
        onRefresh = {}, rememberNavController(), "",
        urlAvatarsOfAuthors = emptyMap()
    )
}

val videoList = listOf(
    Video(
        id = 1,
        userId = 1,
        title = "Hướng dẫn làm app xem video bằng Kotlin",
        description = "Video hướng dẫn chi tiết cách làm app xem video như YouTube.",
        url = "huong-dan-app-xem-video",
        urlThumbnail = "huong-dan-lam-app-xem-video",
        status = "published",
        visibility = "public",
    ),
    Video(
        id = 2,
        userId = 1,
        title = "Top 10 video hot nhất tuần",
        description = "Tổng hợp những video nổi bật được xem nhiều nhất trong tuần này.",
        url = "top-10-video-hot-nhat-tuan",
        urlThumbnail = "huong-dan-lam-app-xem-video",
        status = "published",
        visibility = "public",
    ),
    Video(
        id = 3,
        userId = 1,
        title = "Cách tích hợp Firebase Storage vào app",
        description = "Hướng dẫn upload và phát video từ Firebase Storage.",
        url = "tich-hop-firebase-storage",
        urlThumbnail = "huong-dan-lam-app-xem-video",
        status = "published",
        visibility = "public",
    ),
    Video(
        id = 4,
        userId = 1,
        title = "Làm UI giống YouTube bằng Jetpack Compose",
        description = "Xây dựng giao diện YouTube hiện đại chỉ với vài dòng code.",
        url = "ui-giong-youtube-compose",
        urlThumbnail = "huong-dan-lam-app-xem-video",
        status = "published",
        visibility = "public",
    ),
    Video(
        id = 5,
        userId = 1,
        title = "Phát video từ Google Drive",
        description = "Hướng dẫn lấy video từ Google Drive và phát trong Android.",
        url = "phat-video-tu-google-drive",
        urlThumbnail = "huong-dan-lam-app-xem-video",
        status = "published",
        visibility = "public",
    ),
    Video(
        id = 6,
        userId = 1,
        title = "Sử dụng ExoPlayer trong Android",
        description = "Cách tích hợp ExoPlayer để phát video mượt mà, hỗ trợ nhiều định dạng.",
        url = "su-dung-exoplayer",
        urlThumbnail = "huong-dan-lam-app-xem-video",
        status = "published",
        visibility = "public",
    ),
    Video(
        id = 7,
        userId = 1,
        title = "Tối ưu tốc độ load video",
        description = "Các mẹo giúp video load nhanh và không bị giật lag.",
        url = "toi-uu-toc-do-load-video",
        urlThumbnail = "huong-dan-lam-app-xem-video",
        status = "published",
        visibility = "public",
    ),
    Video(
        id = 8,
        userId = 1,
        title = "Hiển thị thumbnail video bằng Glide",
        description = "Dùng Glide để hiển thị ảnh đại diện video nhanh chóng.",
        url = "thumbnail-video-glide",
        urlThumbnail = "huong-dan-lam-app-xem-video",
        status = "published",
        visibility = "public",
    ),
    Video(
        id = 9,
        userId = 1,
        title = "Tạo danh sách phát (Playlist)",
        description = "Cách nhóm video thành playlist giống YouTube.",
        url = "tao-playlist-video",
        urlThumbnail = "huong-dan-lam-app-xem-video",
        status = "published",
        visibility = "public",
    ),
    Video(
        id = 10,
        userId = 1,
        title = "Xử lý khi video bị lỗi mạng",
        description = "Hiển thị thông báo hoặc retry khi video không tải được.",
        url = "xu-ly-loi-video",
        urlThumbnail = "huong-dan-lam-app-xem-video",
        status = "published",
        visibility = "public",
    )
)