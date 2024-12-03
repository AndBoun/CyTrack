package com.example.CyTrack.Utilities;


import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.ContentResolver;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import androidx.compose.ui.graphics.painter.BitmapPainter;


/**
 * Utility class for handling image requests.
 */
public class ImageRequestUtils {


    /**
     * Making image request
     * <p>
     *    THIS METHOD NEEDS TO BE REWRITTEN IN KOTLIN OF A DIFFERENT LIBRARY
     */
    public static Bitmap makeImageRequest(String URL_IMAGE, Context context) {
        Log.d("Volley", "Making image request");
        CompletableFuture<Bitmap> future = new CompletableFuture<>();

        ImageRequest imageRequest = new ImageRequest(
                URL_IMAGE,
                response -> {
                    future.complete(response);
                    Log.d("Volley Success", "Image loaded successfully");
                },

                0, // Width, set to 0 to get the original width
                0, // Height, set to 0 to get the original height
                ImageView.ScaleType.FIT_XY, // ScaleType
                Bitmap.Config.RGB_565, // Bitmap config

                error -> {
                    future.completeExceptionally(error);
                    Log.e("Volley Error", error.toString());
                }
        );

        // Adding request to request queue
        VolleySingleton.getInstance(context).addToRequestQueue(imageRequest);

        try {
            return future.get(5, TimeUnit.SECONDS); // Timeout after 30 seconds
        } catch (TimeoutException e) {
            Log.e("Volley Timeout", "Image request timed out");
            return null;
        } catch (Exception e) {
            Log.e("Volley Error", "Error occurred: " + e.getMessage());
            return null;
        }
    }


    /**
     * Uploads an image to a remote server using a multipart Volley request.
     * <p>
     * This method creates and executes a multipart request using the Volley library to upload
     * an image to a predefined server endpoint. The image data is sent as a byte array and the
     * request is configured to handle multipart/form-data content type. The server is expected
     * to accept the image with a specific key ("image") in the request.
     */
    private static void uploadImage(Uri selectiedUri, String uploadURL, Context context) {

        byte[] imageData = convertImageUriToBytes(selectiedUri, context);
        MultipartRequest multipartRequest = new MultipartRequest(
                Request.Method.POST,
                uploadURL,
                imageData,
                response -> {
                    // Handle response
                    Toast.makeText(context, response, Toast.LENGTH_LONG).show();
                    Log.d("Upload", "Response: " + response);
                },
                error -> {
                    // Handle error
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("Upload", "Error: " + error.getMessage());
                }
        );

        VolleySingleton.getInstance(context).addToRequestQueue(multipartRequest);
    }

    /**
     * Converts the given image URI to a byte array.
     * <p>
     * This method takes a URI pointing to an image and converts it into a byte array. The conversion
     * involves opening an InputStream from the content resolver using the provided URI, and then
     * reading the content into a byte array. This byte array represents the binary data of the image,
     * which can be used for various purposes such as uploading the image to a server.
     *
     * @param imageUri The URI of the image to be converted. This should be a content URI that points
     *                 to an image resource accessible through the content resolver.
     * @return A byte array representing the image data, or null if the conversion fails.
     * @throws IOException If an I/O error occurs while reading from the InputStream.
     */
    private static byte[] convertImageUriToBytes(Uri imageUri, Context context) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(imageUri);
            ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

            int bufferSize = 10000;
            byte[] buffer = new byte[bufferSize];

            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                byteBuffer.write(buffer, 0, len);
            }

            return byteBuffer.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "image not supported", Toast.LENGTH_LONG).show();
        }
        return null;
    }
}
