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
import com.example.CyTrack.Utilities.User
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

class FriendProfile : ComponentActivity() {

    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            user = intent.getSerializableExtra("user") as User
            if (user != null) {
            }

            Column {
                FriendProfileTopCard(user.firstName, user.username, "generic_avatar")
            }

        }
    }


}


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

@Preview
@Composable
fun PreviewFriendProfileTopCard() {
    FriendProfileTopCard("John Doe", "johndoe", "generic_avatar")
}