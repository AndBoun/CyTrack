package com.example.CyTrack.Social

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.CyTrack.R
import com.example.CyTrack.Utilities.User
import com.example.CyTrack.Utilities.StatusBarUtil


class DirectMessage : ComponentActivity(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val messageList = remember { mutableStateListOf<String>() }
//            user = intent.getSerializableExtra("user") as User

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Column {
                    DirectMessageTopCard("John Doe", "johndoe", "generic_avatar")

                    ConversationLazyList(messageList)
                }

                InputMessageBar(
                    messageList,
                    modifier = Modifier.align(Alignment.BottomCenter)
                        .offset(y = (-50).dp)
                )
            }

            StatusBarUtil.setStatusBarColor(this, R.color.CyRed)

        }
    }

}

@Composable
fun InputMessageBar(
    messageList: MutableList<String>,
    modifier: Modifier = Modifier
) {
    var message by remember { mutableStateOf("") } // Message state

    TextField(
        value = message,
        onValueChange = { message = it }, // Update the message value
        trailingIcon = {
            IconButton(
                onClick = {
                    // Handle message send
                    if (message.isNotBlank()) {
                        // Send message logic here
                        messageList.add(message)

                        message = "" // Clear the text field after sending
                    }
                },

            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = null,
                )
            }
        },
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedContainerColor = MaterialTheme.colorScheme.surface
        ),
        placeholder = {
            Text("Message")
        },
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
    )
}

@Composable
fun ConversationMessageCard(msg: String){

    // Get the screen width
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    Surface (
        color = Color.White,
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier.padding(8.dp)
            .widthIn(max = screenWidth * 0.7f), // Set max width to 70% of screen width
        shadowElevation = 2.dp
    ){
        Text(
            text = msg,
            fontFamily = getCustomFontFamily(),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(15.dp)
        )
    }
}

@Composable
fun ConversationLazyList(
    msg: List<String>,
    modifier: Modifier = Modifier
){
    LazyColumn { items(msg) { msg -> ConversationMessageCard(msg) } }
}

@Composable
fun DirectMessageTopCard(name: String, username : String , img : String){
    Surface(
        color = Color(LocalContext.current.resources.getColor(R.color.CyRed)),
        modifier = Modifier.fillMaxWidth()
            .padding(bottom = 20.dp)
        
    )
    {
        Row(
            verticalAlignment = CenterVertically,
            modifier = Modifier.padding(8.dp)
                .padding(top = 20.dp)
        ){
            IconButton(
                onClick = { /* Handle back navigation */ },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.general_back_arrow_button),
                    contentDescription = "Back arrow",
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.width(20.dp))

            Image(
                painter = painterResource(R.drawable.general_generic_avatar),
                contentDescription = "Contact profile picture",
                modifier = Modifier
                    .size(50.dp)
            )

            Spacer(modifier = Modifier.width(20.dp))
            
            Column{
                Text(
                    text = name,
                    fontFamily = getCustomFontFamily(),
                    color = Color.White,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "#$username",
                    fontFamily = getCustomFontFamily(),
                    color = Color(0xFFF1BE48),
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun DirectMessageScreen(user: User, messageList : MutableList<String>) {
    val insets = WindowInsets.systemBars.asPaddingValues()

    Surface (
        modifier = Modifier.fillMaxSize()
    ){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(insets) // Apply padding to avoid overlap with the status bar
        ) {
            Column {
                DirectMessageTopCard(user.firstName, user.username, "temp")

                ConversationLazyList(messageList)
            }

            InputMessageBar(
                messageList = remember { mutableStateListOf<String>() },
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

@Preview
@Composable
fun InputMessageBarPreview() {
    InputMessageBar(mutableListOf("Hello, World!"))
}

//@Preview
@Composable
fun ConversationMessageCardPreview() {
    ConversationMessageCard("Hello, World!")
}

//@Preview
@Composable
fun PreviewConversationLazyList() {
    ConversationLazyList(listOf("Hello, World!", "This is a test message", "This is another test message"))
}

@Preview
@Composable
fun DirectMessageTopCardPreview(){
    DirectMessageTopCard("John Doe", "johndoe", "generic_avatar")
}

@Preview
@Composable
fun PreviewMessageScreen(){
    Surface(
        modifier = Modifier.fillMaxSize()
    ){
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column {
                DirectMessageTopCard("John Doe", "johndoe", "generic_avatar")

                ConversationLazyList(
                    listOf("Hello, World!", "This is a test message", "This is another test message"),
//                    modifier = Modifier.align(Alignment.TopStart)
                )
            }


            InputMessageBar(
                messageList = mutableListOf("Hello, World!"),
                modifier = Modifier.align(Alignment.BottomCenter)
                    .offset(y = (-50).dp)
            )
        }
    }
}