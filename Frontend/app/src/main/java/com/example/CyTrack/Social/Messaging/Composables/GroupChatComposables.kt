package com.example.CyTrack.Social.Messaging.Composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.CyTrack.Utilities.ComposeUtils
import com.example.compose.OnWhiteSecondary
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Alignment
import com.example.CyTrack.Social.Messaging.ConversationMessageCard
import com.example.CyTrack.Social.ProfileImage


@Composable
fun GroupChatMessageCard(
    isFirst: Boolean,
    profileImg: String,
    message: String,
    name: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = name,
            color = OnWhiteSecondary,
            fontFamily = ComposeUtils.getCustomFontFamily("inter"),
            modifier = Modifier.offset(x = 27.dp)
        )

        Spacer(modifier = Modifier.size(2.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProfileImage(
                imageUrl = profileImg,
                modifier = Modifier.size(22.dp)
            )


            ConversationMessageCard("Hello")
        }

    }
}

@Preview
@Composable
fun PreviewGroupChatMessageCard() {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        GroupChatMessageCard(
            isFirst = true,
            profileImg = "profileImg",
            message = "message",
            name = "name",
            modifier = Modifier.padding(start = 15.dp, top = 15.dp)
        )
    }

}