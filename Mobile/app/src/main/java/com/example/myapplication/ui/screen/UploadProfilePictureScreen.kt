package com.example.myapplication.ui.screen

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
import com.example.myapplication.data.repository.UserRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

@Composable
fun UploadProfilePictureScreen(navHostController: NavHostController) {
    val userRepository = UserRepository()
    var imgUri by remember { mutableStateOf<Uri?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        imgUri = uri
    }

    LazyColumn(modifier = Modifier.padding(16.dp)) {

        // Chọn ảnh
        item {
            Box(
                modifier = Modifier
                    .clickable { launcher.launch("video/*") }
                    .padding(8.dp)
            ) {
                Column(modifier = Modifier.padding(start = 12.dp)) {
                    Row {
                        Text(
                            text = "Chọn ảnh",
                            modifier = Modifier.clickable {
                                launcher.launch("image/*")
                            }
                        )
//                        Image(
//                            painter = painterResource(R.drawable.icon_video_file),
//                            contentDescription = null,
//                            modifier = Modifier
//                                .clickable { launcher.launch("video/*") }
//                        )
                    }
                }
            }
        }
        item {
            Image(
                painter = painterResource(R.drawable.icon_video_file),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clickable {
                        launcher.launch("image/*")
                    }
            )
        }
        item {
            Button(
                onClick = {
                    coroutineScope.launch {
                        if (imgUri == null) {
                            Toast.makeText(
                                context,
                                "Vui lòng chọn ảnh trước!",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@launch
                        }

                        try {
                            val inputStream = context.contentResolver.openInputStream(imgUri!!)
                            val bytes = inputStream?.readBytes()
                            inputStream?.close()

                            if (bytes == null) {
                                Toast.makeText(context, "Không đọc được ảnh!", Toast.LENGTH_SHORT)
                                    .show()
                                return@launch
                            }

                            val requestFile = bytes.toRequestBody("image/*".toMediaType())
                            val filePart = MultipartBody.Part.createFormData(
                                "file",
                                "avatar.jpg",
                                requestFile
                            )

                            // CALL API
                            val response = userRepository.updateUserAvatar(filePart)

                            if (response.isSuccessful) {
                                Toast.makeText(context, "Tải ảnh thành công!", Toast.LENGTH_SHORT)
                                    .show()
                                Log.d("UPLOAD", "Success: ${response.body()}")
                                navHostController.navigate("profile")
                            } else {
                                Toast.makeText(context, "Upload thất bại!", Toast.LENGTH_SHORT)
                                    .show()
                                Log.e("UPLOAD", "Error: ${response.errorBody()?.string()}")
                            }

                        } catch (e: Exception) {
                            Log.e("UPLOAD", "Exception: ${e.message}")
                            Toast.makeText(context, "Lỗi: ${e.message}", Toast.LENGTH_LONG).show()
                        }

                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            ) {
                Text("Tải lên")
            }
        }
    }
}

@Composable
@Preview
fun UploadProfilePictureScreenPreview() {
    UploadProfilePictureScreen(rememberNavController())
}