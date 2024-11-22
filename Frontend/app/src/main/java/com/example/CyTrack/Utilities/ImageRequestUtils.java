package com.example.CyTrack.Utilities;


import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.ContentResolver;

import com.android.volley.Request;


/**
 * Utility class for handling image requests.
 */
public class ImageRequestUtils {


    /**
     * Uploads an image to a remote server using a multipart Volley request.
     * <p>
     * This method creates and executes a multipart request using the Volley library to upload
     * an image to a predefined server endpoint. The image data is sent as a byte array and the
     * request is configured to handle multipart/form-data content type. The server is expected
     * to accept the image with a specific key ("image") in the request.
     */
    private void uploadImage(Uri selectiedUri, String uploadURL, Context context) {

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
    private byte[] convertImageUriToBytes(Uri imageUri, Context context) {
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
