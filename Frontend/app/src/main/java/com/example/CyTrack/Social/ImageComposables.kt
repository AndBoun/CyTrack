package com.example.CyTrack.Social

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.aspectRatio

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.tooling.preview.Preview
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.CyTrack.R
import com.example.CyTrack.Utilities.KtorClientSingleton


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
    ProfileImageContent(imageUrl, modifier)
}

/**
 * A composable function that displays a profile image.
 *
 * @param imageUrl A MutableState containing the URL of the image to be displayed.
 * @param modifier The modifier to be applied to the image. Defaults to an empty modifier.
 */
@Composable
fun ProfileImage(
    imageUrl: MutableState<String>,
    modifier: Modifier = Modifier,
) {
    ProfileImageContent(imageUrl.value, modifier)
}

/**
 * A composable function that displays a profile image.
 *
 * @param imageUrl The URL of the image to be displayed. Defaults to an empty string.
 * @param modifier The modifier to be applied to the image. Defaults to an empty modifier.
 */
@Composable
private fun ProfileImageContent(
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

/**
 * A composable function that provides an image picker button.
 *
 * @param url The URL to which the selected image will be uploaded.
 * @param buttonContent A composable function that defines the content of the button.
 *                      The function receives an onClick lambda to trigger the image picker.
 */
@Composable
fun ImagePicker(
    url: String = "",
    profileImgUri: MutableState<String> = mutableStateOf(""),
    buttonContent: @Composable (onClick: () -> Unit) -> Unit,
) {
    val context = LocalContext.current

    val pickMedia =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                Log.d("PhotoPicker", "Selected URI: $uri")
                profileImgUri.value = uri.toString()

                if (url.isNotEmpty()) KtorClientSingleton.uploadImage(context, url, uri)
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }

    buttonContent {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }
}

/**
 * @suppress
 *
 * This is an example of how to use ImagePicker to select and upload an image.
 */
@Preview
@Composable
private fun SelectImageScreenPreview() {
    Surface {

        ImagePicker(
            url = "",
            buttonContent = { onClick ->
                Button(onClick = onClick) {
                    Text("Select Image")
                }
            }
        )
    }
}

