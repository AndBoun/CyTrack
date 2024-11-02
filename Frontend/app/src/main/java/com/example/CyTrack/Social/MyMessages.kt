package com.example.CyTrack.Social

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import com.example.CyTrack.Utilities.User
import com.example.CyTrack.Utilities.StatusBarUtil

class MyMessages : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val user = intent.getSerializableExtra("user") as User?
            if (user != null) {
            }
        }

        StatusBarUtil.setStatusBarColor(this, R.color.CyRed)
    }
}

@Composable
fun ListMessageCard(
    name: String,
    message: String,
    img: String,
    modifier: Modifier = Modifier
) {
    Surface (
        modifier = Modifier.fillMaxWidth()
    ){
        Row (
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
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
    Surface(
        modifier = Modifier.fillMaxWidth()
            .height(120.dp),
        color = Color(LocalContext.current.resources.getColor(R.color.CyRed)),
    ){
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
                .offset(y = (-4).dp)
        ){
          Image(
              painter = painterResource(R.drawable.social_messages_header),
                contentDescription = "Messages",
          )
        }
    }
}

//@Composable
//fun MessageCardLazyList(messages: List<Message>) {
//    Column {
//        for (message in messages) {
//            ListMessageCard(message.author, message.body, "generic_avatar")
//            Spacer(modifier = Modifier.height(10.dp))
//        }
//    }
//}

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