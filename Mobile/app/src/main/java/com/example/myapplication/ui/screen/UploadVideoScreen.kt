package com.example.myapplication.ui.screen

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.R
import com.example.myapplication.data.repository.VideoRepository
import com.example.myapplication.utils.InputStreamRequestBody
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

@Composable
fun UploadVideoScreen(navHostController: NavHostController) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var videoUri by remember { mutableStateOf<Uri?>(null) }
    var thumbnail by remember { mutableStateOf<Bitmap?>(null) }


    val videoRepository = VideoRepository()

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        videoUri = uri
        thumbnail = getVideoThumbnail(context, uri!!)
    }

    LazyColumn(modifier = Modifier.padding(16.dp)) {

        // Chọn video
        item {
            Box(
                modifier = Modifier
                    .clickable { launcher.launch("video/*") }
                    .padding(8.dp)
            ) {
                Column(modifier = Modifier.padding(start = 12.dp)) {
                    Row {
                        Text(
                            text = "Chọn video để tải lên",
                            modifier = Modifier.clickable {
                                launcher.launch("video/*")
                            }
                        )
                        if (thumbnail != null) {
                            Image(
                                bitmap = thumbnail!!.asImageBitmap(),
                                contentDescription = "Thumbnail",
                                modifier = Modifier
                                    .padding(8.dp)
                                    .fillMaxWidth()
                                    .height(50.dp)
                            )
                        } else {
                            Image(
                                painter = painterResource(R.drawable.icon_video_file),
                                contentDescription = null,
                                modifier = Modifier
                                    .clickable { launcher.launch("video/*") }
                            )
                        }
                    }
                }
            }
        }

        // Title
        item {
            TextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Tiêu đề video") },
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth()
            )
        }

        // Description
        item {
            TextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Mô tả video") },
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth()
            )
        }

        // Upload button
        item {
            Button(
                onClick = {
                    val uri = videoUri ?: return@Button
                    val titleBody = title.toRequestBody("text/plain".toMediaType())
                    val descBody = description.toRequestBody("text/plain".toMediaType())
                    val filePart = uriToMultipart(context, uri)

                    coroutineScope.launch {
                        val response = videoRepository.uploadVideo(filePart, bitmapToMultipart(thumbnail!!), titleBody, descBody)
                        if (response.isSuccessful) {
                            Log.d("UPLOAD", "Upload thành công: ${response.body()}")
                            Toast.makeText(context, "Upload thành công!", Toast.LENGTH_SHORT).show()
                            navHostController.navigate("home")

                        } else {
                            Toast.makeText(context, "Upload that bai! ${response.code()} - ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                            Log.e("UPLOAD", "Upload lỗi: ${response.code()} - ${response.errorBody()?.string()}")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)
            ) {
                Text("Tải lên")
            }
        }
    }
}


@Composable
@Preview
fun UploadVideoPreview() {
    UploadVideoScreen(rememberNavController())
}

fun uriToMultipart(context: Context, uri: Uri): MultipartBody.Part {

    val inputStream = context.contentResolver.openInputStream(uri)
        ?: throw IllegalStateException("Không đọc được file từ URI")

    val mimeType = context.contentResolver.getType(uri) ?: "video/mp4"

    val requestBody = InputStreamRequestBody(mimeType, inputStream)

    return MultipartBody.Part.createFormData(
        "file",
        "upload_video.${mimeType.substringAfter('/')}",
        requestBody
    )
}


fun getVideoThumbnail(context: Context, uri: Uri): Bitmap? {
    return try {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(context, uri)
        val bitmap = retriever.frameAtTime    // lấy frame đầu tiên
        retriever.release()
        bitmap
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun bitmapToMultipart(bitmap: Bitmap, partName: String = "thumbnail"): MultipartBody.Part {

    val stream = java.io.ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
    val byteArray = stream.toByteArray()

    val requestBody = byteArray.toRequestBody("image/jpeg".toMediaType())
    return MultipartBody.Part.createFormData(
        partName,
        "thumbnail.jpg",
        requestBody
    )
}
