package com.example.CyTrack.Social.Friends

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.CyTrack.R
import com.example.CyTrack.Social.MainProfileCard
import com.example.CyTrack.Social.SocialUtils
import com.example.CyTrack.Utilities.User

/**
 * Activity representing a friend's profile.
 */
class FriendProfile : ComponentActivity() {

    /**
     * The user object representing the current user.
     */
    private lateinit var user: User

    /**
     * The friend object representing the friend's profile being viewed.
     */
    private lateinit var friend: Friend

    /**
     * Called when the activity is starting. Initializes the user and sets the content view.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            user = intent.getSerializableExtra("user") as User
            friend = intent.getSerializableExtra("friend") as Friend

            Column {
                FriendProfileTopCard(friend.firstName, friend.username, SocialUtils.getProfileImageUrl(friend.userID))
            }

        }
    }
}

/**
 * Composable function to display the top card of a friend's profile.
 *
 * @param name The name of the friend.
 * @param username The username of the friend.
 * @param img The image resource identifier for the friend's avatar.
 * @param modifier The modifier to be applied to the layout.
 */
@Composable
fun FriendProfileTopCard(
    name: String,
    username: String,
    img: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Surface(
        color = Color(context.resources.getColor(R.color.CyRed)),
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.Bottom
        ) {

            IconButton(
                onClick = {
                    (context as Activity).finish()
                },
                modifier = Modifier.align(Alignment.Start)
            ) {
                Image(
                    painter = painterResource(R.drawable.general_back_arrow_button),
                    contentDescription = "Back arrow",
                    modifier = Modifier.size(24.dp)
                )
            }

            MainProfileCard(
                name, username, img,
                modifier = Modifier
                    .padding(bottom = 20.dp)
                    .fillMaxWidth()
            )
        }

    }
}

/**
 * @suppress
 */
@Preview
@Composable
fun PreviewFriendProfileTopCard() {
    FriendProfileTopCard("John Doe", "johndoe", "generic_avatar")
}