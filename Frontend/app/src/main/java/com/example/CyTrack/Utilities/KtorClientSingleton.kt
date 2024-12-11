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
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
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

    /**
     * Singleton instance of HttpClient configured with OkHttp engine.
     * It installs ContentNegotiation and Logging plugins.
     */
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

//    fun uploadImage(context: Context, url: String, imageUri: Uri) = runBlocking {
//        try {
//            val imageData = convertImageUriToBytes(imageUri, context)
//            val imageName = imageUri.lastPathSegment
//
//            val response: HttpResponse = getClient().submitFormWithBinaryData(
//                url = url,
//                formData = formData {
//                    append("description", "Profile Image")
//                    if (imageData != null) {
//                        Log.d("Image", "Attempting to upload image")
//                        append("image", imageData, Headers.build {
//                            append(HttpHeaders.ContentType, "image/png")
//                            append(HttpHeaders.ContentDisposition, "filename=\"${imageName}.png\"")
//                        })
//                    }
//                }
//            )
//
//            if (response.status == HttpStatusCode.OK) {
//                Toast.makeText(context, "Image uploaded successfully", Toast.LENGTH_LONG).show()
//            } else {
//                Toast.makeText(context, "Image upload failed", Toast.LENGTH_LONG).show()
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//            Toast.makeText(context, "An error occurred during image upload", Toast.LENGTH_LONG)
//                .show()
//        }
//    }

    fun uploadImage(context: Context, url: String, imageUri: Uri) = runBlocking {
        try {
            Log.d("Image Url", url)
            val imageData = convertImageUriToBytes(imageUri, context)
            val imageName = imageUri.lastPathSegment

            if (imageData == null || imageData.isEmpty()) {
                Toast.makeText(context, "Invalid image data", Toast.LENGTH_LONG).show()
                Log.d("Image", "Invalid image data")
                return@runBlocking
            }

            val response: HttpResponse = getClient().put(url) {
                    setBody(
                        MultiPartFormDataContent(
                            formData {
                                Log.d("Image", "Attempting to upload image")

                                append("file", imageData, Headers.build {
                                        append(HttpHeaders.ContentType, "image/png")
                                        append(
                                            HttpHeaders.ContentDisposition,
                                            "filename=${imageName}.png"
                                        )
                                    })

                            })
                    )
                }


            if (response.status == HttpStatusCode.OK) {
                Toast.makeText(context, "Image uploaded successfully", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(context, "Image upload failed", Toast.LENGTH_LONG).show()
                Log.e(
                    "UploadError",
                    "Response status: ${response.status}, Response: ${response.bodyAsText()}"
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "An error occurred during image upload", Toast.LENGTH_LONG)
                .show()
            Log.e("UploadError", "Exception: ${e.message}", e)
        }
    }

    /**
     * Converts an image URI to a byte array.
     *
     * @param imageUri The URI of the image to convert.
     * @param context The context used to access the content resolver.
     * @return A byte array representing the image, or null if an error occurs.
     */
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
}

