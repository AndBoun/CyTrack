package com.example.CyTrack.Social.Messaging

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.CyTrack.R
import com.example.CyTrack.Utilities.ComposeUtils.Companion.getCustomFontFamily
import com.example.CyTrack.Utilities.User
import com.example.CyTrack.Utilities.StatusBarUtil

class MyMessages : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val user = intent.getSerializableExtra("user") as User?
            if (user != null) {
            }

            Column {
                MyMessageTopCard()
                Spacer(modifier = Modifier.height(20.dp))
                MessageCardLazyList(
                    listOf(
                        Message("generic_avatar", "John Doe", "Hello"),
                        Message("generic_avatar", "Jane Doe", "Hi")
                    )
                )
            }
        }

        StatusBarUtil.setStatusBarColor(this, R.color.CyRed)
    }
}

data class Message(
    val imageUrl: String,
    val name: String,
    val body: String
)

@Composable
fun ListMessageCard(
    name: String,
    message: String,
    img: String,
    modifier: Modifier = Modifier
) {
    Surface (
        modifier = modifier.fillMaxWidth()
    ){
        Row (
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp)
                .fillMaxWidth()
        ){
            Image(
                painter = painterResource(R.drawable.general_generic_avatar),
                contentDescription = "Contact profile picture",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(
            ) { // Column for name and message
                Text(
                    text = name,
                    fontFamily = getCustomFontFamily("Inter", FontWeight.SemiBold, FontStyle.Normal),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = message,
                    fontFamily = getCustomFontFamily("Inter", FontWeight.Normal, FontStyle.Normal),
                    fontWeight = FontWeight.Normal,
                    fontSize = 11.sp,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
fun MyMessageTopCard(
    modifier: Modifier = Modifier
){
    val context = LocalContext.current

    Surface(
        modifier = Modifier.fillMaxWidth()
            .height(120.dp),
        color = Color(context.resources.getColor(R.color.CyRed)),
    ){
        Box {
            IconButton(
                onClick = {
                    (context as Activity).finish()
                },
                modifier = Modifier.align(Alignment.BottomStart)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.general_back_arrow_button),
                    contentDescription = "Back arrow",
                    modifier = Modifier.size(24.dp)
                )
            }

            Image(
                painter = painterResource(R.drawable.social_messages_header),
                contentDescription = "Messages",
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
fun MessageCardLazyList(messages: List<Message>) {
    Column {
        for (message in messages) {
            ListMessageCard(message.name, message.body, "generic_avatar")
            HorizontalDivider(thickness = 1.dp, color = Color.Gray)
        }
    }
}



@Preview
@Composable
fun PreviewListMessageCard() {
    ListMessageCard("John Doe", "Hello", "generic_avatar")
}

@Preview
@Composable
fun PreviewMyMessageTopCard() {
    MyMessageTopCard()
}

@Preview
@Composable
fun PreviewMessageCardLazyList() {
    Surface {
        MessageCardLazyList(
            listOf(
                Message("generic_avatar", "John Doe", "Hello"),
                Message("generic_avatar", "Jane Doe", "Hi")
            )
        )
    }
}

@Preview
@Composable
fun PreviewMyMessagesScreen() {
    Surface {
        Column {
            MyMessageTopCard()
            Spacer(modifier = Modifier.height(20.dp))
            MessageCardLazyList(
                listOf(
                    Message("generic_avatar", "John Doe", "Hello"),
                    Message("generic_avatar", "Jane Doe", "Hi")
                )
            )
        }
    }
}