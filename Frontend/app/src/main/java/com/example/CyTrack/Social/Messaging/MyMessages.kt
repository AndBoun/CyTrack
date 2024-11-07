package com.example.CyTrack.Social.Messaging

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.CyTrack.R
import com.example.CyTrack.Social.Friends.Friend
import com.example.CyTrack.Social.SocialUtils
import com.example.CyTrack.Social.SocialUtils.Companion.processMessageCardData
import com.example.CyTrack.Social.WebSockets.WebSocketManagerMessages
import com.example.CyTrack.Utilities.ComposeUtils.Companion.getCustomFontFamily
import com.example.CyTrack.Utilities.StatusBarUtil
import com.example.CyTrack.Utilities.UrlHolder
import com.example.CyTrack.Utilities.User
import com.example.CyTrack.Utilities.WebSocketListener
import org.java_websocket.handshake.ServerHandshake

class MyMessages : ComponentActivity(), WebSocketListener {

    private lateinit var user: User

    private val URL = "${UrlHolder.URL}/conversations"

    private var messageCards: MutableList<MessageCardData> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            user = intent.getSerializableExtra("user") as User
            messageCards = remember { mutableStateListOf() }

            val serverUrl = "${UrlHolder.wsURL}/conversations/${user.id}"
            Log.d("WebSocketServiceUtil", "Connecting to $serverUrl")
            WebSocketManagerMessages.getInstance().connectWebSocket(serverUrl);
            WebSocketManagerMessages.getInstance().setWebSocketListener(this@MyMessages);

            Column {
                MyMessageTopCard()
                Spacer(modifier = Modifier.height(20.dp))
                MessageCardLazyList(
                    messageCards,
                    onMessageClick = {
                        val friend = Friend(it.firstname, it.username, it.userID, it.friendEntityID)
                        switchToMessagePage(friend)
                    }
                )
            }
        }

        StatusBarUtil.setStatusBarColor(this, R.color.CyRed)
    }


//    private fun getMessageCards() {
//        val getURL = "${URL}/${user.id}"
//
//        SocialUtils.getConversations(this, getURL, messageCards)
//    }

    private fun switchToMessagePage(friend: Friend) {
        SocialUtils.messageUserScreen(user, friend, this)
    }

    override fun onWebSocketOpen(handshakedata: ServerHandshake?) {
    }

    override fun onWebSocketMessage(message: String) {
//        messageCards.clear()
        processMessageCardData(message, messageCards)
    }

    override fun onWebSocketClose(code: Int, reason: String?, remote: Boolean) {
        val closedBy = if (remote) "server" else "local"
        runOnUiThread(Runnable {
            Toast.makeText(this, "Connection closed", Toast.LENGTH_LONG).show()
            Log.d("Mymessages", "Connection closed $reason $closedBy")
        })
    }

    override fun onWebSocketError(ex: Exception?) {
        runOnUiThread(Runnable {
            Toast.makeText(this, "Error: ${ex?.message}", Toast.LENGTH_LONG).show()
            Log.d("Mymessages", "Error: ${ex?.message}")
        })
    }
}

data class MessageCardData(
    val username: String,
    val firstname: String,
    val content: String,
    val time: String,
    val userID: Int,
    val friendEntityID: Int,
    val conversationID: Int
)

@Composable
fun ListMessageCard(
    name: String,
    message: String,
    img: String,
    modifier: Modifier = Modifier,
    onMessageClick: () -> Unit = {}
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                onClick = onMessageClick
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
        ) {
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
                    fontFamily = getCustomFontFamily(
                        "Inter",
                        FontWeight.SemiBold,
                        FontStyle.Normal
                    ),
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
    modifier: Modifier = Modifier,
    onMessageClick: (MessageCardData) -> Unit = {}
) {
    val context = LocalContext.current

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        color = Color(context.resources.getColor(R.color.CyRed)),
    ) {
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
fun MessageCardLazyList(
    messages: List<MessageCardData>,
    onMessageClick: (MessageCardData) -> Unit = {},
    modifier: Modifier = Modifier
) {
    for (message in messages) {
        ListMessageCard(message.firstname, message.content, "generic_avatar",
            onMessageClick = {
                onMessageClick(message)
            })
        Log.d("MessageCardLazyList", message.userID.toString())
        HorizontalDivider(thickness = 1.dp, color = Color.Gray)

    }
}

    @Preview
    @Composable
    fun PreviewListMessageCard() {
        ListMessageCard("John Doe", "Hello", "generic_avatar")
    }


    @Preview
    @Composable
    fun PreviewMessageCardLazyList() {
        Surface {
            MessageCardLazyList(
                listOf(
                    MessageCardData("john", "John Doe", "Hello", "12/1/12", 1, 2, 1),
                    MessageCardData("jane", "Jane Doe", "Hi", "12/1/12", 1, 2, 1),
                    MessageCardData("john", "John Doe", "Hello", "12/1/12", 1, 2, 1),
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
                        MessageCardData("john", "John Doe", "Hello", "12/1/12", 1, 2, 1),
                        MessageCardData("jane", "Jane Doe", "Hi", "12/1/12", 1, 2, 1),
                        MessageCardData("john", "John Doe", "Hello", "12/1/12", 1, 2, 1),
                    ),
                )
            }
        }
    }