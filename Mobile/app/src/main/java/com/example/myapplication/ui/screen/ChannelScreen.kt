package com.example.myapplication.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.R
import com.example.myapplication.data.model.Channel
import com.example.myapplication.viewmodel.ChannelViewModel

@Composable
fun ChannelScreen(
    viewModel: ChannelViewModel = viewModel(),
    navHostController: NavHostController
) {
    val channels by viewModel.channels.collectAsState();
    var isRefreshing by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        viewModel.fetchChannel()
    }
    LazyColumn {
        items(channels) { channel ->
            ChannelItem(channel)
        }
    }
}

@Composable
fun ChannelItem(channel: Channel) {
    Row(
        modifier = Modifier
            .padding(12.dp)
    ) {
        // Avatar / Thumbnail
        Image(
            painter = painterResource(R.drawable.ic_launcher_background),
            contentDescription = null,
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = channel.name ?: "Tên kênh",
                style = MaterialTheme.typography.titleMedium,
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = channel.description ?: "",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.Gray
                ),
                maxLines = 2
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChannelItemPreview() {
    val channel = Channel(
        id = 1,
        1,
        name = "Kênh Demo",
        description = "Đây là mô tả cho kênh demo"
    )

    ChannelItem(channel)
}


@Composable
@Preview
fun ChannelScreenPreview() {
    val navController = rememberNavController()
    ChannelScreen(
        viewModel = androidx.lifecycle.viewmodel.compose.viewModel(), // previewViewModel
        navHostController = navController
    )
}