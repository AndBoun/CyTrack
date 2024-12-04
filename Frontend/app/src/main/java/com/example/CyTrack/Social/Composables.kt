package com.example.CyTrack.Social

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil3.compose.AsyncImage
import coil3.network.NetworkHeaders
import coil3.network.httpHeaders
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.CyTrack.R


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
