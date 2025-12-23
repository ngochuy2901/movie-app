package com.example.myapplication.ui.screen

import android.annotation.SuppressLint
import android.content.Context
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.myapplication.R
import com.example.myapplication.data.model.Comment
import com.example.myapplication.data.model.Video
import com.example.myapplication.utils.ConfigLoader
import com.example.myapplication.viewmodel.PlayVideoViewModel
import androidx.core.net.toUri
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.myapplication.data.dto.UserDto
import com.example.myapplication.data.model.PlayVideoScreenData
import com.example.myapplication.data.model.UserDtoData.userDto

@Composable
fun PlayVideoScreen(
    video: Video, viewModel: PlayVideoViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
        factory = PlayVideoViewModelFactory(video)
    )
) {
    val context = LocalContext.current
    val url = ConfigLoader.get(context, "URL_SERVER")
    val urlEndpointImg = ConfigLoader.get(context, "IMG_URL_PUBLIC")
    val videoUrl = url + "video/play_video/" + video.url
    val comments by viewModel.comments.collectAsState()
    val author by viewModel.author.collectAsState()
    val commentUsers = viewModel.commentUsers.collectAsState()

    if (author == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Text("Đang tải...")
        }
    } else {
        Log.d("Author info:", author?.fullName ?: "")
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
                }
                Box(modifier = Modifier.fillMaxWidth()) {
                    VideoInfo(video, author!!, context, urlEndpointImg)
                }
                Box(modifier = Modifier.fillMaxWidth()) {
                    ListComment(comments, commentUsers.value)
                }

            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface) // màu nền input
                    .padding(8.dp)
            ) {
                UserComment(viewModel, context, urlEndpointImg, author!!)
            }
        }
    }


}

class PlayVideoViewModelFactory(
    private val video: Video
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlayVideoViewModel::class.java)) {
            return PlayVideoViewModel(video) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

@Composable
@Preview
fun PlayVideoScreenPreview() {
    PlayVideoScreen(PlayVideoScreenData.video)
}

@Composable
fun VideoPlayer(
    videoUri: String
) {
    Box(
        modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
    ) {
        AndroidView(factory = { context ->
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
        }, update = { videoView ->
            videoView.setVideoURI(videoUri.toUri())
//                videoView.start()
        })
    }
}

@Composable
fun ExoVideoPlayer(url: String) {
    val context = LocalContext.current
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val mediaItem = MediaItem.fromUri(url)
            setMediaItem(mediaItem)
            prepare()
//                playWhenReady = true
        }
    }
    Box(
        modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
    ) {
        AndroidView(
            factory = {
                PlayerView(context).apply {
                    player = exoPlayer
                }
            })
    }
}

@Composable
fun VideoInfo(video: Video, author: UserDto, context: Context, urlEndpointImg: String) {
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
            AsyncImage(
                model = ImageRequest.Builder(context).data(urlEndpointImg + author.imgUrl)
                    .crossfade(true).build(),
                contentDescription = null,
                placeholder = painterResource(R.drawable.icon_person),
                error = painterResource(R.drawable.icon_person),
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
            )

            // Tên kênh + subscriber
            Column(
                verticalArrangement = Arrangement.Center, modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = author.fullName!!,
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
                text = reactionType, style = MaterialTheme.typography.bodySmall
            )
        }
    }
}


@Composable
@Preview
fun VideoInfoPreview() {
    VideoInfo(PlayVideoScreenData.video, PlayVideoScreenData.author, LocalContext.current, "")
}

@Composable
fun CommentItem(comment: Comment, userDto: UserDto) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp), verticalAlignment = Alignment.Top
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
            modifier = Modifier.weight(1f)
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
fun UserComment(viewModel: PlayVideoViewModel, context: Context, urlEndpointImg: String, author: UserDto) {
    var commentText by remember { mutableStateOf("") }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp) // khoảng cách giữa các item
    ) {
        // Avatar
        Log.d("Image in comment:", urlEndpointImg + author.imgUrl)
        AsyncImage(
            model = ImageRequest.Builder(context).data(urlEndpointImg + author.imgUrl)
                .crossfade(true).build(),
            contentDescription = null,
            placeholder = painterResource(R.drawable.icon_person),
            error = painterResource(R.drawable.icon_person),
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
                })
    }
}


@SuppressLint("ViewModelConstructorInComposable")
@Composable
@Preview
fun UserCommentPreview() {
    val fakeViewModel = PlayVideoViewModel(PlayVideoScreenData.video)
    UserComment(viewModel = fakeViewModel, LocalContext.current, "", PlayVideoScreenData.author)
}


@Composable
@Preview
fun CommentItemPreview() {
    CommentItem(PlayVideoScreenData.comment, userDto)
}
@Composable
fun ListComment(
    comments: List<Comment>,
    commentUsers: Map<Long, UserDto>
) {
    if (comments.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("Chưa có bình luận nào")
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            items(
                items = comments,
                key = { it.id ?: it.hashCode() }
            ) { comment ->
                val user = commentUsers[comment.userId]

                if (user != null) {
                    CommentItem(comment, user)
                } else {
                    Text(
                        text = "Đang tải thông tin người dùng...",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
//        Column {
//            comments.forEach { comment ->
//                val user = commentUsers[comment.userId]
//
//                if (user != null) {
//                    CommentItem(comment, user)
//                } else {
//                    Text(
//                        text = "Đang tải thông tin người dùng...",
//                        modifier = Modifier.padding(16.dp),
//                        style = MaterialTheme.typography.bodySmall
//                    )
//                }
//            }
//        }
    }
}


@Composable
@Preview
fun ListCommentPreview() {
    ListComment(PlayVideoScreenData.comments,  emptyMap<Long, UserDto>())
}
