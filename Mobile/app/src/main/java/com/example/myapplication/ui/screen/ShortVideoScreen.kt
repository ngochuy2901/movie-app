package com.example.myapplication.ui.screen

import android.R.attr.contentDescription
import android.net.Uri
import android.util.Log
import android.widget.MediaController
import android.widget.VideoView
import androidx.annotation.Nullable
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toString
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.myapplication.R
import com.example.myapplication.auth.Auth
import com.example.myapplication.data.dto.UserDto
import com.example.myapplication.data.model.Comment
import com.example.myapplication.utils.ConfigLoader
import com.example.myapplication.viewmodel.PlayVideoViewModel
import com.example.myapplication.viewmodel.ShortVideoViewModel

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ShortVideoScreen(
    viewModel: ShortVideoViewModel = viewModel()
) {
    val imgUrl = ConfigLoader.get(LocalContext.current, "IMG_URL_PUBLIC")
    val imgUrlHeadPoint = ConfigLoader.get(LocalContext.current, "URL_SERVER")
    //
    val videos by viewModel.videos.collectAsState()
    val comments by viewModel.comments.collectAsState()
    val numberOfComments by viewModel.numberOfComments.collectAsState()
    val numberOfReactions by viewModel.numberOfReactions.collectAsState()
    val commentUsers by viewModel.commentUsers.collectAsState()
    val userAvatarUrl by viewModel.userAvatarUrl.collectAsState()
    val pagerState = rememberPagerState(pageCount = { videos.size })
    var showSheet by remember { mutableStateOf(false) }
    var currentVideoId by remember { mutableStateOf<Long?>(null) }
    val sheetState = rememberModalBottomSheetState()
    LaunchedEffect(Unit) { viewModel.loadData() }
    if (videos.isEmpty()) return

    // Bottom sheet outside pager — uses currentVideoId to show comments
    if (showSheet && currentVideoId != null) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = { showSheet = false },
            dragHandle = { BottomSheetDefaults.DragHandle() }
        ) {
            // Pass lists from ViewModel (they were already fetched)
            CommentBottom(
                listComments = comments,
                commentUsers = commentUsers,
                imgUrlHeadPoint = imgUrl,
                viewModel = viewModel,
                videoId = currentVideoId!!,
                url = imgUrl + userAvatarUrl
            )
        }
    }

    VerticalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize()
    ) { page ->        // <-- page only valid here
        val fileName = videos.getOrNull(page)?.url ?: return@VerticalPager
        val videoUrl = imgUrlHeadPoint.trimEnd('/') + "/video/play_video/" + fileName
        viewModel.loadReaction(videos.getOrNull(page)?.id ?: return@VerticalPager)
        val videoId = videos.getOrNull(page)?.id
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            VideoPlayer(videoUrl = videoUrl, modifier = Modifier.fillMaxSize())

            UserActionBar(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 16.dp, bottom = 60.dp),
                onLikeClick = {
                    viewModel.onLikeReaction(videoId!!)
                    viewModel.loadReaction(videoId)
                },
                onCommentClick = {
                    // When user taps comment: capture the current page safely

//                    val videoId = videos.getOrNull(page)?.id
                    if (videoId != null) {
                        currentVideoId = videoId
                        viewModel.loadComments(videoId)     // load comments for that video
                        showSheet = true                  // open sheet
                    }
                },
                onShareClick = { /*...*/ },
                numberOfComments = numberOfComments,
                numberOfReactions = numberOfReactions
            )
        }
    }
}


@Composable
@Preview(showBackground = true)
fun ShortVideoPreview() {
    ShortVideoScreen()
}

@Composable
fun VideoPlayer(
    videoUrl: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val exoPlayer = remember(videoUrl) {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(videoUrl))
            prepare()
            playWhenReady = true
            repeatMode = Player.REPEAT_MODE_ONE
        }
    }

    DisposableEffect(exoPlayer) {
        onDispose {
            exoPlayer.release()
        }
    }

    AndroidView(
        modifier = modifier, //
        factory = {
            PlayerView(context).apply {
//                useController = false
                player = exoPlayer
            }
        },
        update = { view ->
            view.player = exoPlayer
        }
    )
}

@Composable
fun UserActionBar(
    modifier: Modifier = Modifier, onLikeClick: () -> Unit,
    onCommentClick: () -> Unit,
    onShareClick: () -> Unit,
    numberOfComments: Long,
    numberOfReactions: Long
) {
    Column(
        modifier = modifier.wrapContentSize(),
        verticalArrangement = Arrangement.spacedBy(28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
//        AsyncImage(
//            model = ImageRequest.Builder(context)
//                .data(url)
//                .addHeader("Authorization", "Bearer $token")   // 🌟 THÊM TOKEN VÀO HEADER
//                .crossfade(true)
//                .build(),
//            contentDescription = null,
//            modifier = Modifier.clip(CircleShape)
//        )
        UserActionBarItem(R.drawable.icon_thumb_up, numberOfReactions.toString(), onClick = { onLikeClick() })
        UserActionBarItem(
            R.drawable.icon_comment,
            numberOfComments.toString(),
            onClick = { onCommentClick() })
        UserActionBarItem(R.drawable.icon_share, "10", onClick = { onShareClick() })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserActionBarItem(resourceIcon: Int, reactionCount: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .wrapContentSize()
            .clickable { },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        IconButton(onClick) {
            Icon(
                painter = painterResource(id = resourceIcon),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(40.dp)
            )
        }
        Text(
            text = reactionCount,
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}


@Composable
@Preview
fun UserActionBarPreview() {
    UserActionBar(onLikeClick = {}, onCommentClick = {}, onShareClick = {}, numberOfComments = 0, numberOfReactions = 0)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentBottom(
    listComments: List<Comment>,
    commentUsers: Map<Long, UserDto>,
//    token: String,
    imgUrlHeadPoint: String,
    viewModel: ShortVideoViewModel,
    videoId: Long,
    url: String
) {
    Log.d("Url user comment", url)
    viewModel.loadUsersForComments(listComments)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        Text(
            text = "Bình luận",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.size(12.dp))

        // Danh sách comment
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(listComments.size) { index ->
                val user = commentUsers[listComments[index].userId]
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(imgUrlHeadPoint + (user?.imgUrl ?: ""))
                            .crossfade(true)
                            .build(),
                        contentDescription = null,
                        modifier = Modifier.clip(CircleShape)
                    )
                    Spacer(Modifier.size(8.dp))
                    Column {
                        Text(user?.fullName ?: "Unknown user")
                        Text(listComments.get(index).content)
                    }
                }
            }
        }

        Spacer(Modifier.size(20.dp))
        UserCommentInShortVideo(viewModel, videoId, url)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun CommentBottomSheetPreview() {
//    CommentBottomSheet(rememberModalBottomSheetState(), {}, comments, )
}

@Composable
fun UserCommentInShortVideo(
    viewModel: ShortVideoViewModel,
    videoId: Long,
    url: String
) {
    var commentText by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // Avatar
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(url)
                .crossfade(true)
                .build(),
            contentDescription = "user avatar",
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
        )

        Spacer(Modifier.width(8.dp))

        // Input
        TextField(
            value = commentText,
            onValueChange = { commentText = it },
            placeholder = { Text("Viết bình luận…") },
            singleLine = true,
            modifier = Modifier
                .weight(1f)
                .height(48.dp),
            shape = RoundedCornerShape(30.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                cursorColor = Color.White,
                focusedContainerColor = Color(0x22000000),
                unfocusedContainerColor = Color(0x11000000),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )

        Spacer(Modifier.width(8.dp))

        // Send button
        Icon(
            painter = painterResource(id = R.drawable.icon_send),
            contentDescription = null,
            tint = if (commentText.isBlank()) Color.Gray else Color.White,
            modifier = Modifier
                .size(32.dp)
                .clickable(
                    enabled = commentText.isNotBlank()
                ) {
                    viewModel.saveNewComment(commentText, videoId)
                    keyboardController?.hide()
                    commentText = ""
                }
        )
    }
}


