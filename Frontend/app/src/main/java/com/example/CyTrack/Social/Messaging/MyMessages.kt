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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import com.example.CyTrack.Social.ProfileImage
import com.example.CyTrack.Social.SocialUtils
import com.example.CyTrack.Social.SocialUtils.Companion.processMessageCardData
import com.example.CyTrack.Social.SocialUtils.Companion.processMessageListData
import com.example.CyTrack.Social.WebSockets.WebSocketManagerMessages
import com.example.CyTrack.Utilities.ComposeUtils.Companion.getCustomFontFamily
import com.example.CyTrack.Utilities.StatusBarUtil
import com.example.CyTrack.Utilities.UrlHolder
import com.example.CyTrack.Utilities.User
import com.example.CyTrack.Utilities.WebSocketListener
import org.java_websocket.handshake.ServerHandshake

/**
 * Activity to handle displaying and managing messages.
 */
class MyMessages : ComponentActivity(), WebSocketListener {

    /**
     * The user object representing the current user.
     */
    private lateinit var user: User

    /**
     * The URL for conversations.
     */
    private val URL = "${UrlHolder.URL}/conversations"

    /**
     * A mutable list to hold message card data.
     */
    private var messageCards: MutableList<MessageListData> = mutableListOf()

    private var messageList: MutableList<MessageListData> = mutableListOf()

    /**
     * Called when the activity is first created.
     * Sets up the content view and initializes WebSocket connection.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        user = intent.getSerializableExtra("user") as User

        setContent {
            messageCards = remember { mutableStateListOf() }
            messageList = remember { mutableStateListOf() }

            val serverUrl = "${UrlHolder.wsURL}/notifications/${user.id}"
            Log.d("WebSocketServiceUtil", "Connecting to $serverUrl")
            WebSocketManagerMessages.getInstance().connectWebSocket(serverUrl)
            WebSocketManagerMessages.getInstance().setWebSocketListener(this@MyMessages)

            Column {
                MyMessageTopCard()
                Spacer(modifier = Modifier.height(20.dp))
                MessageCardLazyList(
                    messageList,
                    onMessageClick = {


                        if(it.chatType == "direct"){
                            val friend = Friend(it.senderUsername, it.senderUsername, it.userID, 0)
                            switchToMessagePage(friend)
                        } else {
                            val friend = Friend(it.groupName, it.groupName, it.groupOrReceiverID, 0)
                            switchToGroupChatPage(friend)
                        }
                    }
                )
            }
        }

        StatusBarUtil.setStatusBarColor(this, R.color.CyRed)
    }


    /**
     * Switches to the message page for the given friend.
     *
     * @param friend The friend to message.
     */
    private fun switchToMessagePage(friend: Friend) {
        SocialUtils.messageUserScreen(user, friend, this)
    }

    private fun switchToGroupChatPage(friend: Friend) {
        SocialUtils.messageGroupScreen(user, friend, this)
    }


    /**
     * Called when the WebSocket connection is opened.
     *
     * @param handshakedata The handshake data.
     */
    override fun onWebSocketOpen(handshakedata: ServerHandshake?) {
    }

    /**
     * Called when a message is received from the WebSocket.
     *
     * @param message The message received.
     */
    override fun onWebSocketMessage(message: String) {
        runOnUiThread(Runnable {
            Log.d("Mymessages", "Message: $message")
            processMessageListData(message, messageList)
        })
    }

    /**
     * Called when the WebSocket connection is closed.
     *
     * @param code The close code.
     * @param reason The reason for the closure.
     * @param remote Whether the closure was initiated by the remote server.
     */
    override fun onWebSocketClose(code: Int, reason: String?, remote: Boolean) {
        val closedBy = if (remote) "server" else "local"
        runOnUiThread(Runnable {
            Toast.makeText(this, "Connection closed", Toast.LENGTH_LONG).show()
            Log.d("Mymessages", "Connection closed $reason $closedBy")
        })
    }

    /**
     * Called when an error occurs on the WebSocket.
     *
     * @param ex The exception that occurred.
     */
    override fun onWebSocketError(ex: Exception?) {
        runOnUiThread(Runnable {
            Toast.makeText(this, "Error: ${ex?.message}", Toast.LENGTH_LONG).show()
            Log.d("Mymessages", "Error: ${ex?.message}")
        })
    }
}

/**
 * Data class representing a message card.
 *
 * @param username The username of the sender.
 * @param firstname The first name of the sender.
 * @param content The content of the message.
 * @param time The time the message was sent.
 * @param userID The user ID of the sender.
 * @param friendEntityID The friend entity ID.
 * @param conversationID The conversation ID.
 */
data class MessageCardData(
    val username: String,
    val firstname: String,
    val content: String,
    val time: String,
    val userID: Int,
    val friendEntityID: Int,
    val conversationID: Int
)

/**
 * Composable function to display a message card.
 *
 * @param name The name of the sender.
 * @param message The message content.
 * @param img The image resource.
 * @param modifier The modifier to be applied to the card.
 * @param onMessageClick The callback to be invoked when the card is clicked.
 */
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
            ProfileImage(
                img,
                modifier = Modifier.size(40.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column {
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

/**
 * Composable function to display the top card of the message screen.
 *
 * @param modifier The modifier to be applied to the card.
 * @param onMessageClick The callback to be invoked when the card is clicked.
 */
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

/**
 * Composable function to display a list of message cards.
 *
 * @param messages The list of message cards to display.
 * @param onMessageClick The callback to be invoked when a card is clicked.
 * @param modifier The modifier to be applied to the list.
 */
@Composable
fun MessageCardLazyList(
    messages: List<MessageListData>,
    onMessageClick: (MessageListData) -> Unit = {},
    modifier: Modifier = Modifier
) {
    for (message in messages) {

        if (message.chatType == "direct"){
            ListMessageCard(
                message.senderUsername,
                message.content,
                SocialUtils.getProfileImageUrl(message.userID),
                onMessageClick = {
                    onMessageClick(message)
                })
        } else {
            ListMessageCard(
                message.groupName,
                message.content,
                "https://cdn-icons-png.flaticon.com/512/5677/5677749.png",
                onMessageClick = {
                    onMessageClick(message)
                })
        }




        Log.d("MessageCardLazyList", message.userID.toString())
        HorizontalDivider(thickness = 1.dp, color = Color.Gray)
    }
}

/**
 * @suppress
 */
@Preview
@Composable
fun PreviewListMessageCard() {
    ListMessageCard("John Doe", "Hello", "generic_avatar")
}

/**
 * @suppress
 */
@Preview
@Composable
fun PreviewMessageCardLazyList() {
    Surface {
        MessageCardLazyList(
            listOf(
                MessageListData("john", "John Doe", "Jane Doe", "Group", "Hello", "12/1/12", 1, 2),
                MessageListData("john", "John Doe", "Jane Doe", "Group", "Hello", "12/1/12", 1, 2),
                MessageListData("john", "John Doe", "Jane Doe", "Group", "Hello", "12/1/12", 1, 2),

                )
        )
    }
}

/**
 * @suppress
 */
@Preview
@Composable
fun PreviewMyMessagesScreen() {
    Surface {
        Column {
            MyMessageTopCard()
            Spacer(modifier = Modifier.height(20.dp))
            MessageCardLazyList(
                listOf(
                    MessageListData(
                        "john",
                        "John Doe",
                        "Jane Doe",
                        "Group",
                        "Hello",
                        "12/1/12",
                        1,
                        2
                    ),
                    MessageListData(
                        "john",
                        "John Doe",
                        "Jane Doe",
                        "Group",
                        "Hello",
                        "12/1/12",
                        1,
                        2
                    ),
                    MessageListData(
                        "john",
                        "John Doe",
                        "Jane Doe",
                        "Group",
                        "Hello",
                        "12/1/12",
                        1,
                        2
                    ),
                ),
            )
        }
    }
}