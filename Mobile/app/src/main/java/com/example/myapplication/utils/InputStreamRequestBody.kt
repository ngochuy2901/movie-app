package com.example.myapplication.utils

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.BufferedSink
import okio.source
import java.io.InputStream

class InputStreamRequestBody(
    private val contentType: String,
    private val inputStream: InputStream
) : RequestBody() {

    override fun contentType() = contentType.toMediaTypeOrNull()

    override fun writeTo(sink: BufferedSink) {
        inputStream.use { input ->
            val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
            var bytesRead: Int

            while (input.read(buffer).also { bytesRead = it } != -1) {
                sink.write(buffer, 0, bytesRead)
            }
        }
    }
}

