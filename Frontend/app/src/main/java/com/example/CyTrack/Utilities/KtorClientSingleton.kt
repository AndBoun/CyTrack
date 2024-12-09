package com.example.CyTrack.Utilities

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import java.io.ByteArrayOutputStream
import java.io.IOException

/**
 * Singleton object that provides a configured instance of HttpClient.
 */
object KtorClientSingleton {

    private val client = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json()
        }
        install(Logging)
    }

    /**
     * Returns the singleton instance of HttpClient.
     *
     * @return HttpClient instance
     */
    fun getClient(): HttpClient {
        return client
    }
}

fun uploadImage(context: Context, url: String, imageUri: Uri) = runBlocking {
    try {
        val imageData = convertImageUriToBytes(imageUri, context)
        val imageName = imageUri.lastPathSegment

        val response: HttpResponse = KtorClientSingleton.getClient().submitFormWithBinaryData(
            url = url,
            formData = formData {
                append("description", "Ktor logo")
                if (imageData != null) {
                    Log.d("Image", "Attempting to upload image")
                    append("image", imageData, Headers.build {
                        append(HttpHeaders.ContentType, "image/png")
                        append(HttpHeaders.ContentDisposition, "filename=\"${imageName}.png\"")
                    })
                }
            }
        )

        if (response.status == HttpStatusCode.OK) {
            Toast.makeText(context, "Image uploaded successfully", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(context, "Image upload failed", Toast.LENGTH_LONG).show()
        }
    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "An error occurred during image upload", Toast.LENGTH_LONG).show()
    }
}

private fun convertImageUriToBytes(imageUri: Uri, context: Context): ByteArray? {
    try {
        val inputStream = context.contentResolver.openInputStream(imageUri)
        val byteBuffer = ByteArrayOutputStream()

        val bufferSize = 10000
        val buffer = ByteArray(bufferSize)

        var len: Int
        while ((inputStream!!.read(buffer).also { len = it }) != -1) {
            byteBuffer.write(buffer, 0, len)
        }

        inputStream.close()

        Log.d("Image", "Image converted to bytes")
        return byteBuffer.toByteArray()
    } catch (e: IOException) {
        e.printStackTrace()
        Toast.makeText(context, "image not supported", Toast.LENGTH_LONG).show()
    }
    return null
}