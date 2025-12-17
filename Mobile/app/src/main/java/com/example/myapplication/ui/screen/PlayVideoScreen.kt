package com.example.myapplication.ui.screen

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import android.widget.MediaController
import android.widget.VideoView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.myapplication.R
import com.example.myapplication.data.model.Comment
import com.example.myapplication.data.model.Video
import com.example.myapplication.utils.ConfigLoader
import com.example.myapplication.viewmodel.PlayVideoViewModel
import androidx.core.net.toUri

@Composable
fun PlayVideoScreen(
    video: Video,
    viewModel: PlayVideoViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
        factory = PlayVideoViewModelFactory(video.id!!)
    )
) {
    val url = ConfigLoader.get(LocalContext.current, "URL_SERVER")
    val videoUrl = url + "video/play_video/" + video.url
    Log.d("VIDEO_URL", videoUrl)
    val comments by viewModel.comments.collectAsState()
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Video player
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
            ) {
//                VideoPlayer(videoUrl)
                ExoVideoPlayer(videoUrl)
//                VideoPlayer("http://10.0.2.2:8080/video/play_video/1.webm")
//                ExoVideoPlayer("http://10.0.2.2:8080/video/play_video/1.webm")
            }
            Box(modifier = Modifier.fillMaxWidth()) {
                VideoInfo(video)
            }

            // Comment list, chiếm không gian còn lại
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            ) {
                items(comments.size) { index ->
                    CommentItem(comments[index])
                }
            }
        }

        // User comment luôn cố định dưới cùng
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface) // màu nền input
                .padding(8.dp)
        ) {
            UserComment(viewModel)
        }
    }
}

class PlayVideoViewModelFactory(
    private val videoId: Long
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlayVideoViewModel::class.java)) {
            return PlayVideoViewModel(videoId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

@Composable
@Preview
fun PlayVideoScreenPreview() {
    PlayVideoScreen(video)
}

@Composable
fun VideoPlayer(
    videoUri: String
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        AndroidView(
            factory = { context ->
                VideoView(context).apply {
                    setVideoURI(videoUri.toUri())
                    setMediaController(MediaController(context).apply {
                        setAnchorView(this@apply)
                    })
                    setOnPreparedListener { mp ->
                        mp.isLooping = true // lặp lại nếu muốn
                        start() // tự động phát
                    }
                }
            },
            update = { videoView ->
                videoView.setVideoURI(videoUri.toUri())
//                videoView.start()
            }
        )
    }
}

@Composable
fun ExoVideoPlayer(url: String) {
    val context = LocalContext.current
    val exoPlayer = remember {
        ExoPlayer.Builder(context)
            .build()
            .apply {
                val mediaItem = MediaItem.fromUri(url)
                setMediaItem(mediaItem)
                prepare()
//                playWhenReady = true
            }
    }
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        AndroidView(
            factory = {
                PlayerView(context).apply {
                    player = exoPlayer
                }
            }
        )
    }
}

@Composable
fun VideoInfo(video: Video) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Tiêu đề video
        Text(
            text = video.title ?: "",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Row thông tin kênh + Follow + Reaction buttons
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Avatar kênh
            Image(
                painter = painterResource(R.drawable.ic_launcher_background),
                contentDescription = "Channel Avatar",
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
            )

            // Tên kênh + subscriber
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Tên kênh",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "1,000 người đăng ký",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Follow button
            Button(
                onClick = {},
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                shape = CircleShape
            ) {
                Text("Follow", style = MaterialTheme.typography.bodySmall)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Row Reaction buttons
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            ReactionItem(R.drawable.icon_thumb_up, "Thích")
            ReactionItem(R.drawable.icon_thumb_down, "Không thích")
            ReactionItem(R.drawable.icon_share, "Chia sẻ")
        }
    }
}

@Composable
fun ReactionItem(resourceIcon: Int, reactionType: String) {
    Button(
        onClick = {},
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
        shape = CircleShape
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                painter = painterResource(id = resourceIcon),
                contentDescription = reactionType,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = reactionType,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}


@Composable
@Preview
fun VideoInfoPreview() {
    VideoInfo(video)
}

@Composable
fun CommentItem(comment: Comment) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.Top
    ) {
        // Avatar
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = "Avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.width(8.dp))

        // Nội dung comment
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            Text(
                text = "Người bình luận", // có thể đổi thành comment.userName
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = comment.content,
            )
        }
        Icon(painterResource(R.drawable.icon_more_vert), null)
    }
}

@Composable
fun UserComment(viewModel: PlayVideoViewModel) {
    var commentText by remember { mutableStateOf("") }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp) // khoảng cách giữa các item
    ) {
        // Avatar
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = "Avatar",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
        )

        // Input comment
        TextField(
            value = commentText,
            onValueChange = { commentText = it },
            placeholder = { Text("Viết bình luận...") },
            modifier = Modifier
                .weight(1f) // chiếm không gian còn lại
                .height(50.dp),
//            singleLine = true
        )

        // Button gửi
        Icon(
            painter = painterResource(id = R.drawable.icon_send),
            contentDescription = "Bình luận",
            modifier = Modifier
                .size(40.dp)
                .clickable {
                    viewModel.saveNewComment(commentText)
                    commentText = ""
                }
        )
    }
}


@SuppressLint("ViewModelConstructorInComposable")
@Composable
@Preview
fun UserCommentPreview() {
    val fakeViewModel = PlayVideoViewModel(videoId = 1L)
    UserComment(viewModel = fakeViewModel)
}


@Composable
@Preview
fun CommentItemPreview() {
    CommentItem(comment)
}

@Composable
fun ListComment(comments: List<Comment>) {
    Column {
        comments.forEach { comment ->
            CommentItem(comment)
        }
    }
}

@Composable
@Preview
fun ListCommentPreview() {
    ListComment(comments)
}

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

val comment = Comment(
    id = null,
    userId = 1L,
    videoId = 3L,
    content = "Video hay quá bạn ơi!",
    createdAt = null,
    updatedAt = null
)

val comments = listOf(
    Comment(
        id = 1,
        userId = 1,
        videoId = 1,
        content = "Video hay quá bạn ơi!",
        createdAt = "2025-11-15T12:30:00",
        updatedAt = "2025-11-15T12:30:00"
    ),
    Comment(
        id = 2,
        userId = 2,
        videoId = 1,
        content = "Hướng dẫn rất dễ hiểu!",
        createdAt = "2025-11-15T12:35:00",
        updatedAt = "2025-11-15T12:35:00"
    ),
    Comment(
        id = 3,
        userId = 3,
        videoId = 1,
        content = "Cảm ơn bạn đã chia sẻ.",
        createdAt = "2025-11-15T12:40:00",
        updatedAt = "2025-11-15T12:40:00"
    ),
    Comment(
        id = 4,
        userId = 4,
        videoId = 1,
        content = "Mong bạn làm thêm nhiều video nữa!",
        createdAt = "2025-11-15T12:45:00",
        updatedAt = "2025-11-15T12:45:00"
    ),
    Comment(
        id = 5,
        userId = 5,
        videoId = 1,
        content = "Clip quá tuyệt!",
        createdAt = "2025-11-15T12:50:00",
        updatedAt = "2025-11-15T12:50:00"
    )
)


