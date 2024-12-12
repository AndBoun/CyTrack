package com.example.CyTrack.Social.Messaging.Activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.CyTrack.R
import com.example.CyTrack.Social.Friends.Friend
import com.example.CyTrack.Social.Messaging.ConversationLazyList
import com.example.CyTrack.Social.Messaging.DirectMessage.Msg
import com.example.CyTrack.Social.Messaging.DirectMessageTopCard
import com.example.CyTrack.Social.Messaging.InputMessageBar
import com.example.CyTrack.Social.SocialUtils
import com.example.CyTrack.Social.WebSockets.WebSocketManagerMessages
import com.example.CyTrack.Utilities.StatusBarUtil
import com.example.CyTrack.Utilities.UrlHolder
import com.example.CyTrack.Utilities.User
import com.example.CyTrack.Utilities.WebSocketListener
import org.java_websocket.handshake.ServerHandshake

class GroupChat : ComponentActivity(), WebSocketListener {

    private lateinit var user: User

    private lateinit var recipientUser: Friend

    private var messageList: MutableList<Msg> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtil.setStatusBarColor(this, R.color.CyRed)

        user = intent.getSerializableExtra("user") as User
        recipientUser = intent.getSerializableExtra("recipientUser") as Friend

        val serverUrl = "${UrlHolder.wsURL}/chat/group/${recipientUser.userID}/${user.id}"
        Log.d("WebSocketServiceUtil", "Connecting to $serverUrl")
        WebSocketManagerMessages.getInstance().connectWebSocket(serverUrl)
        WebSocketManagerMessages.getInstance().setWebSocketListener(this@GroupChat)


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
                        "Group Chat",
                        isGroupChat = true,
                        img = "https://cdn-icons-png.flaticon.com/512/5677/5677749.png",
                        onGroupSettings = {
                            navigateToGroupChatSettings()
                        }
                    )

                    ConversationLazyList(
                        messageList, messageAlignment = {
                            it.senderID != user.id
                        },
                        isGroupChat = true
                    )
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
        }
    }

    private fun navigateToGroupChatSettings() {
        val intent = Intent(this, GroupChatSettings::class.java).apply {
            putExtra("user", user)
            putExtra("groupChatID", recipientUser.userID)
            putExtra("groupName", recipientUser.firstName)
        }
        startActivity(intent)
    }


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
//                            recipientUser.userID
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

    override fun onWebSocketClose(code: Int, reason: String?, remote: Boolean) {
        runOnUiThread(Runnable {
            val closedBy = if (remote) "Server" else "Client"
            Toast.makeText(this, "Connection closed by $closedBy", Toast.LENGTH_SHORT).show()
        })
    }

    override fun onWebSocketError(ex: Exception?) {
    }
}