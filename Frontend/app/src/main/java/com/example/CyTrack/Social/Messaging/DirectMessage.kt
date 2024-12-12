package com.example.CyTrack.Social.Messaging

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.CyTrack.R
import com.example.CyTrack.Social.Friends.Friend
import com.example.CyTrack.Social.Messaging.Composables.GroupChatMessageCard
import com.example.CyTrack.Social.ProfileImage
import com.example.CyTrack.Social.SocialUtils
import com.example.CyTrack.Social.WebSockets.WebSocketManagerMessages
import com.example.CyTrack.Utilities.ComposeUtils.Companion.getCustomFontFamily
import com.example.CyTrack.Utilities.StatusBarUtil
import com.example.CyTrack.Utilities.UrlHolder
import com.example.CyTrack.Utilities.User
import com.example.CyTrack.Utilities.WebSocketListener
import org.java_websocket.handshake.ServerHandshake
import kotlin.math.log


/**
 * Activity for handling direct messages between users.
 */
class DirectMessage : ComponentActivity(), WebSocketListener {

    /**
     * The user whose profile is being displayed.
     */
    private lateinit var user: User

    /**
     * The recipient user.
     */
    private lateinit var recipientUser: Friend

    /**
     * The key for the conversation.
     */
    private lateinit var conversationKey: String

    /**
     * List of messages in the conversation.
     */
    private var messageList: MutableList<Msg> = mutableListOf()

    /**
     * Called when the activity is starting.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        user = intent.getSerializableExtra("user") as User
        recipientUser = intent.getSerializableExtra("recipientUser") as Friend
        conversationKey = "${user.id}_DM_${recipientUser.userID}"

        val serverUrl = "${UrlHolder.wsURL}/chat/direct/${recipientUser.userID}/${user.id}"
        Log.d("WebSocketServiceUtil", "Connecting to $serverUrl")
        WebSocketManagerMessages.getInstance().connectWebSocket(serverUrl)
        WebSocketManagerMessages.getInstance().setWebSocketListener(this@DirectMessage)

        setContent {
            messageList = remember { mutableStateListOf() }

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    DirectMessageTopCard(
                        recipientUser.firstName,
                        recipientUser.username,
                        SocialUtils.getProfileImageUrl(recipientUser.userID)
                    )

                    ConversationLazyList(messageList, messageAlignment = {
                        it.senderID != user.id
                    })
                }

                InputMessageBar(
                    messageList,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .offset(y = (-50).dp),
                    onClickSend = {
                        sendMessage(it)
                    }
                )
            }

            StatusBarUtil.setStatusBarColor(this, R.color.CyRed)
        }
    }

    /**
     * Data class representing a message.
     * @param message The content of the message.
     * @param senderID The ID of the sender.
     */
    data class Msg(
        val message: String,
        val senderID: Int,
        val senderUsername: String = ""
    )

    /**
     * Sends a message.
     * @param message The content of the message to be sent.
     */
    private fun sendMessage(message: String) {
        if (message.isBlank()) return
        try {
            WebSocketManagerMessages.getInstance().sendMessage(message)
            messageList.add(Msg(message, user.id))
        } catch (e: Exception) {
            Log.d("ExceptionSendMessage:", e.message.toString())
            Toast.makeText(this, "Failed to send message", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Called when the WebSocket connection is opened.
     * @param handshakedata The handshake data.
     */
    override fun onWebSocketOpen(handshakedata: ServerHandshake) {}

    /**
     * Called when a message is received from the WebSocket.
     * @param message The message received.
     */
    override fun onWebSocketMessage(message: String) {
        runOnUiThread(Runnable {
            try {
                if (message.substring(0, 4) == "You:" || !message.startsWith("{\"status\":")) {
//                } else if (message.substring(
//                        0,
//                        recipientUser.username.length + 1
//                    ) == "${recipientUser.username}:"
//                ) {
//                    messageList.add(
//                        Msg(
//                            message.substring(recipientUser.username.length + 1).trim(),
//                            recipientUser.userID,
//                            recipientUser.username
//                        )
//                    )
                } else {
                    // Handle message received
                    Log.d("MessageReceivedFromOther", message)
                    val tempMsg = SocialUtils.processMessageListData(message, avoidUserID = user.id)
                    if (tempMsg.message.isNotEmpty()) messageList.add(tempMsg)
                }
            } catch (e: Exception) {
                Log.d("Exception", e.message.toString())
            }
        })
    }

    /**
     * Called when the WebSocket connection is closed.
     * @param code The close code.
     * @param reason The reason for the closure.
     * @param remote Whether the closure was initiated by the remote peer.
     */
    override fun onWebSocketClose(code: Int, reason: String?, remote: Boolean) {
        runOnUiThread(Runnable {
            val closedBy = if (remote) "Server" else "Client"
            Toast.makeText(this, "Connection closed by $closedBy", Toast.LENGTH_SHORT).show()
        })
    }

    /**
     * Called when an error occurs on the WebSocket.
     * @param ex The exception that occurred.
     */
    override fun onWebSocketError(ex: Exception?) {
    }
}

/**
 * Composable function for the input message bar.
 * @param messageList The list of messages.
 * @param modifier The modifier to be applied to the layout.
 * @param onClickSend The callback to be invoked when the send button is clicked.
 */
@Composable
fun InputMessageBar(
    messageList: MutableList<DirectMessage.Msg>,
    modifier: Modifier = Modifier,
    onClickSend: (String) -> Unit = {}
) {
    var message by remember { mutableStateOf("") } // Message state

    TextField(
        value = message,
        onValueChange = { message = it }, // Update the message value
        trailingIcon = {
            IconButton(
                onClick = {
                    onClickSend(message)
                    message = ""
                },
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
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

/**
 * Composable function for displaying a conversation message card.
 * @param msg The message to be displayed.
 * @param modifier The modifier to be applied to the layout.
 */
@Composable
fun ConversationMessageCard(
    msg: String,
    modifier: Modifier = Modifier
) {
    // Get the screen width
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    Surface(
        color = Color.White,
        shape = RoundedCornerShape(4.dp),
        modifier = modifier
            .padding(8.dp)
            .widthIn(max = screenWidth * 0.7f), // Set max width to 70% of screen width
        shadowElevation = 2.dp
    ) {
        Text(
            text = msg,
            fontFamily = getCustomFontFamily("Inter", FontWeight.Normal, FontStyle.Normal),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(15.dp)
        )
    }
}

/**
 * Composable function for displaying a list of conversation messages.
 * @param msg The list of messages.
 * @param modifier The modifier to be applied to the layout.
 * @param messageAlignment A lambda function to determine the alignment of the message.
 */
@Composable
fun ConversationLazyList(
    msg: List<DirectMessage.Msg>,
    modifier: Modifier = Modifier,
    isGroupChat: Boolean = false,
    messageAlignment: (DirectMessage.Msg) -> Boolean = { false }
) {
    LazyColumn(
        modifier = Modifier.padding(bottom = 100.dp)
    ) {
        items(msg) { message ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                if (messageAlignment(message)) { // Check if message is sent by the user
                    if (isGroupChat) {
                        GroupChatMessageCard(
                            isFirst = true,
                            profileImg = SocialUtils.getProfileImageUrl(message.senderID),
                            message = message.message,
                            name = message.senderUsername,
                            modifier = Modifier
                                .padding(start = 15.dp, top = 15.dp)
                                .align(Alignment.TopStart)
                        )
                    } else {
                        ConversationMessageCard(
                            msg = message.message,
                            modifier = Modifier.align(Alignment.TopStart)
                        )
                    }
                } else {
                    ConversationMessageCard(
                        msg = message.message,
                        modifier = Modifier.align(Alignment.TopEnd)
                    )
                }
            }
        }
    }
}

/**
 * Composable function for displaying the top card of the direct message screen.
 * @param name The name of the recipient.
 * @param username The username of the recipient.
 * @param img The image resource for the recipient's avatar.
 * @param modifier The modifier to be applied to the layout.
 */
@Composable
fun DirectMessageTopCard(
    name: String,
    username: String,
    img: String,
    isGroupChat: Boolean = false,
    onGroupSettings: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Surface(
        color = Color(LocalContext.current.resources.getColor(R.color.CyRed)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp)
    ) {
        Row(
            verticalAlignment = CenterVertically,
            modifier = Modifier
                .padding(8.dp)
                .padding(top = 20.dp)
        ) {
            IconButton(
                onClick = {
                    (context as ComponentActivity).finish()
                },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.general_back_arrow_button),
                    contentDescription = "Back arrow",
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.width(20.dp))

            ProfileImage(
                imageUrl = img,
                modifier = Modifier.size(50.dp)
            )

            Spacer(modifier = Modifier.width(20.dp))

            Column {
                Text(
                    text = name,
                    fontFamily = getCustomFontFamily("Inter", FontWeight.Normal, FontStyle.Normal),
                    color = Color.White,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "#$username",
                    fontFamily = getCustomFontFamily("Inter", FontWeight.Normal, FontStyle.Normal),
                    color = Color(0xFFF1BE48),
                    fontSize = 12.sp
                )
            }

            if (isGroupChat){
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    IconButton(
                        onClick = {
                            onGroupSettings()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Group settings",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Composable function for displaying the direct message screen.
 * @param user The user whose profile is being displayed.
 * @param messageList The list of messages in the conversation.
 * @param modifier The modifier to be applied to the layout.
 */
@Composable
fun DirectMessageScreen(
    user: User,
    messageList: MutableList<DirectMessage.Msg>,
    modifier: Modifier = Modifier
) {
    val insets = WindowInsets.systemBars.asPaddingValues()

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(insets) // Apply padding to avoid overlap with the status bar
        ) {
            Column {
                DirectMessageTopCard(user.firstName, user.username, "temp")

                ConversationLazyList(messageList)
            }
        }
    }
}

/**
 * @suppress
 */
@Preview
@Composable
fun InputMessageBarPreview() {
}

/**
 * @suppress
 */
@Preview
@Composable
fun ConversationMessageCardPreview() {
    ConversationMessageCard("Hello, World!")
}

/**
 * @suppress
 */
@Preview
@Composable
fun DirectMessageTopCardPreview() {
    DirectMessageTopCard("John Doe", "johndoe", "generic_avatar", isGroupChat = true)
}

/**
 * @suppress
 */
@Preview
@Composable
fun PreviewMessageScreen() {
    var messageList = remember {
        mutableStateListOf<DirectMessage.Msg>(
            DirectMessage.Msg("Hello", 1),
            DirectMessage.Msg("Hi", 2),
            DirectMessage.Msg("Hello", 1),
            DirectMessage.Msg("Hi", 2),
            DirectMessage.Msg("Hello", 1),
            DirectMessage.Msg("Hello", 1),
            DirectMessage.Msg("Hi", 2),
            DirectMessage.Msg("Hello", 1),
            DirectMessage.Msg("Hi", 2),
            DirectMessage.Msg("Hello", 1),
            DirectMessage.Msg("Hello", 1),
            DirectMessage.Msg("Hi", 2),
            DirectMessage.Msg("Hello", 1),
            DirectMessage.Msg("Hi", 2),
            DirectMessage.Msg("Hello", 1),
        )
    }

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
            ) {
                DirectMessageTopCard("John", "Doe", "generic_avatar")

                ConversationLazyList(messageList, messageAlignment = {
                    it.senderID != 1
                })
            }

            InputMessageBar(
                messageList,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = (-50).dp),
                onClickSend = {
                }
            )
        }
    }
}