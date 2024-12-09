package com.example.CyTrack.Social

import android.content.Context
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.tooling.preview.Preview
import coil3.Uri
import coil3.compose.AsyncImage
import coil3.network.NetworkHeaders
import coil3.network.httpHeaders
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.CyTrack.R
import com.example.CyTrack.Utilities.KtorClientSingleton
import com.example.CyTrack.Utilities.uploadImage


/**
 * A composable function that displays a profile image.
 *
 * @param imageUrl The URL of the image to be displayed. Defaults to an empty string.
 * @param modifier The modifier to be applied to the image. Defaults to an empty modifier.
 */
@Composable
fun ProfileImage(
    imageUrl: String = "",
    modifier: Modifier = Modifier,
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .crossfade(true)
            .build(),
        onError = { error ->
            Log.d("ProfileImage", "Error loading image: $error")
        },
        error = painterResource(R.drawable.general_generic_avatar),
        placeholder = painterResource(R.drawable.general_generic_avatar),
        contentDescription = "Profile picture",
        contentScale = ContentScale.Crop,
        modifier = modifier
            .aspectRatio(1f)
            .clip(CircleShape)
    )
}

@Composable
fun rememberImagePicker(onImageSelected: (android.net.Uri?) -> Unit): () -> Unit {
    val pickMedia = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            Log.d("PhotoPicker", "Selected URI: $uri")
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
        onImageSelected(uri)
    }

    return {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }
}

@Composable
fun SelectImageButton(
    url: String,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    val imagePicker = rememberImagePicker { uri ->
        if (uri != null) {
            uploadImage(context, url, uri)
        }
    }

    Button(onClick = { imagePicker() }) {
        Text("Select Image")
    }

}

@Preview
@Composable
fun SelectImageScreenPreview() {
    Surface {

        SelectImageButton(
            "",
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        )

    }
}

