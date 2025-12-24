package com.example.myapplication.ui.screen

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.R
import com.example.myapplication.data.model.Video
import com.example.myapplication.data.model.VideoData.video
import com.example.myapplication.viewmodel.PlayVideoViewModel
import com.example.myapplication.viewmodel.SearchViewModel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Composable
fun SearchVideoScreen(
    navHostController: NavHostController,
    viewModel: SearchViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
        factory = SearchViewModelFactory()
    ),
    onBack: () -> Unit = { navHostController.popBackStack() },
    onMicClick: () -> Unit = {}
) {
//    val query by viewModel.keyword.collectAsState()
    val results by viewModel.searchResult.collectAsState()
    Column(modifier = Modifier.fillMaxSize()) {
        SearchBar(
            query = viewModel.keyword.collectAsState().value,
            onQueryChange = viewModel::onKeywordChange,
            onBack = onBack,
            onMicClick = onMicClick
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(top = 8.dp, bottom = 16.dp)
        ) {
            items(results) { video ->
                SearchVideoItem(video, navHostController)
            }
        }
    }

}


@Composable
@Preview
fun SearchVideoScreenPreview() {
    SearchVideoScreen(rememberNavController())
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onBack: () -> Unit = {},
    onMicClick: () -> Unit = {}
) {
//    var query by remember { mutableStateOf("") }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(8.dp)
    ) {
        // Back button
        IconButton(onClick = onBack) {
            Icon(
                painter = painterResource(R.drawable.icon_arrow_back),
                contentDescription = "Back"
            )
        }

        // Search field
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier
                .weight(1f),
//                .height(48.dp),
            placeholder = { Text("Tìm kiếm") },
//                singleLine = true,
            shape = RoundedCornerShape(24.dp),
            trailingIcon = {
                IconButton(onClick = onMicClick) {
                    Icon(
                        painter = painterResource(R.drawable.icon_micro),
                        contentDescription = "Voice search"
                    )
                }
            },
        )
    }
}

@Composable
fun SearchVideoItem(video: Video, navHostController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                val videoJson = Uri.encode(Json.encodeToString(video))
                navHostController.navigate("playvideo/$videoJson")
            }
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = video.title.orEmpty(),
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 2
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Tên kênh • 1M lượt xem",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }

    Divider(
        thickness = 0.5.dp,
        color = MaterialTheme.colorScheme.outlineVariant
    )
}

@Composable
@Preview
fun SearchVideoItemPreview() {
    SearchVideoItem(video, rememberNavController())
}

class SearchViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}