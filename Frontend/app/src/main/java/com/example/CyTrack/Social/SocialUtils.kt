package com.example.CyTrack.Social

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.example.CyTrack.Social.Friends.AddFriends
import com.example.CyTrack.Social.Friends.Friend
import com.example.CyTrack.Social.Friends.FriendProfile
import com.example.CyTrack.Social.Messaging.Activities.GroupChat
import com.example.CyTrack.Social.Messaging.DirectMessage
import com.example.CyTrack.Social.Messaging.Members
import com.example.CyTrack.Social.Messaging.MessageCardData
import com.example.CyTrack.Social.Messaging.MessageListData
import com.example.CyTrack.Utilities.NetworkUtils
import com.example.CyTrack.Utilities.UrlHolder
import com.example.CyTrack.Utilities.User
import com.example.CyTrack.Utilities.VolleySingleton
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat

/**
 * Utility class for social-related functionalities.
 */
class SocialUtils {

    /**
     * Companion object to hold static methods.
     */
    companion object {

        /**
         * Launches the DirectMessage activity to message a user.
         *
         * @param user The current user.
         * @param recipient The friend to whom the message is to be sent.
         * @param context The activity context.
         */
        @JvmStatic
        fun messageUserScreen(user: User, recipient: Friend, context: Activity) {
            val intent = Intent(context, DirectMessage::class.java).apply {
                putExtra("user", user)
                putExtra("recipientUser", recipient)
            }

            context.startActivity(intent)
        }

        @JvmStatic
        fun messageGroupScreen(user: User, recipient: Friend, context: Activity) {
            val intent = Intent(context, GroupChat::class.java).apply {
                putExtra("user", user)
                putExtra("recipientUser", recipient)
            }

            context.startActivity(intent)
        }


        /**
         * Switches to the AddFriends activity.
         *
         * @param context The activity context.
         * @param user The current user.
         */
        @JvmStatic
        fun switchToAddFriends(context: Activity, user: User) {
            val intent = Intent(context, AddFriends::class.java).apply {
                putExtra("user", user)
            }
            context.startActivity(intent)
        }


        /**
         * Switches to the FriendProfile activity.
         *
         * @param context The activity context.
         * @param user The current user.
         * @param friend The friend whose profile is to be viewed.
         */
        @JvmStatic
        fun switchToFriendProfile(context: Activity, user: User, friend: Friend) {
            val intent = Intent(context, FriendProfile::class.java).apply {
                putExtra("user", user)
                putExtra("friend", friend)
            }
            context.startActivity(intent)
        }


        /**
         * Fetches a list of friends from the server and updates the provided friendList.
         *
         * @param context The activity context.
         * @param friendList The list to be updated with friends.
         * @param url The URL to fetch the friends from.
         * @param arrName The name of the JSON array containing the friends.
         * @param onComplete A callback function to be executed after the request is completed.
         */
        @JvmStatic
        fun getListOfFriends(
            context: Activity,
            friendList: MutableList<Friend>,
            url: String,
            arrName: String,
            onComplete: () -> Unit = {}
        ) {
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                { response ->
                    try {
                        val message = response.getString("message")
                        val data = response.getJSONObject("data").getJSONArray(arrName)

                        for (i in 0 until data.length()) {
                            val friend = data.getJSONObject(i)
                            friendList.add(
                                Friend(
                                    friend.getString("firstname"),
                                    friend.getString("username"),
                                    friend.getInt("userID"),
                                    friend.getInt("friendID")
                                )
                            )
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    } finally {
                        onComplete()
                    }
                },
                { error ->
                    Toast.makeText(context, NetworkUtils.errorResponse(error), Toast.LENGTH_LONG)
                        .show()
                }

            )
            VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest)
        }


        /**
         * Deletes a friend from the server.
         *
         * @param context The activity context.
         * @param url The URL to send the delete request to.
         * @param onComplete A callback function to be executed after the request is completed.
         */
        @JvmStatic
        fun deleteFriend(
            context: Activity,
            url: String,
            onComplete: () -> Unit = {}
        ) {
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.DELETE,
                url,
                null,
                { response ->
                    try {
                        val message = response.getString("message")
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    } finally {
                        onComplete()
                    }
                },
                { error ->
                    Toast.makeText(context, NetworkUtils.errorResponse(error), Toast.LENGTH_LONG)
                        .show()
                }

            )
            VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest)
        }


        /**
         * Processes a JSON message string and converts it into a DirectMessage.Msg object.
         *
         * @param msg The JSON message string to be processed.
         * @return A DirectMessage.Msg object containing the message content and user ID.
         */
        fun processMessage(msg: String): DirectMessage.Msg {
            try {
                val tempMsg = msg.removePrefix("Received message: ")


                val jsonObject = JSONObject(tempMsg)
                val data = jsonObject.getJSONObject("data")

                val userID = data.getInt("userID")
                val msgContent = data.getString("contentofmessage")

                return DirectMessage.Msg(msgContent, userID)

            } catch (e: JSONException) {
                e.printStackTrace()
            }

            return DirectMessage.Msg("", 0)
        }

        fun processMessageListData(
            msg: String,
            messageList: MutableList<MessageListData> = mutableListOf(),
            avoidUserID: Int = 0
        ): DirectMessage.Msg {
            var message: DirectMessage.Msg = DirectMessage.Msg("", 0)
            try {
                val tempMsg = msg.removePrefix("Received message: ")
                val jsonObject = JSONObject(tempMsg)
                val data = jsonObject.getJSONObject("data")

                val tempData = MessageListData(
                    data.getString("chatType"),
                    data.getString("senderUsername"),
                    data.getString("receiverUsername"),
                    data.getString("groupName"),
                    data.getString("content"),
                    data.getString("time"),
                    data.getInt("groupOrReceiverID"),
                    data.getInt("userID")
                )

                // Remove any existing entry with the same chatType, groupOrReceiverID, and userID
                messageList.removeAll {
                    (it.chatType == tempData.chatType &&
                            it.groupOrReceiverID == tempData.groupOrReceiverID &&
                            it.userID == tempData.userID) ||
                            it.userID == avoidUserID
                }

                // Add the new entry at the beginning of the list
                messageList.add(0, tempData)

                message =
                    DirectMessage.Msg(tempData.content, tempData.userID, tempData.senderUsername)

            } catch (e: JSONException) {
                e.printStackTrace()
            }

            return message
        }


        /**
         * Fetches a list of conversations from the server and updates the provided cardList.
         *
         * @param context The activity context.
         * @param url The URL to fetch the conversations from.
         * @param cardList The list to be updated with conversations.
         */
        fun getConversations(
            context: Activity,
            url: String,
            cardList: MutableList<MessageCardData>,
        ) {
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                { response ->
                    try {
                        val message = response.getString("message")
                        val data = response.getJSONObject("data").getJSONArray("userConversations")

                        for (i in 0 until data.length()) {
                            val messageCardContent = data.getJSONObject(i)
                            cardList.add(
                                MessageCardData(
                                    messageCardContent.getString("username"),
                                    messageCardContent.getString("firstName"),
                                    messageCardContent.getString("content"),
                                    messageCardContent.getString("time"),
                                    messageCardContent.getInt("userID"),
                                    messageCardContent.getInt("friendEntityID"),
                                    messageCardContent.getInt("conversationID")
                                )
                            )
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                { error ->
                    Toast.makeText(context, NetworkUtils.errorResponse(error), Toast.LENGTH_LONG)
                        .show()
                }

            )
            VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest)
        }

        /**
         * Processes a JSON message string and updates the provided cardList with MessageCardData objects.
         *
         * @param msg The JSON message string to be processed.
         * @param cardList The list to be updated with MessageCardData objects.
         */
        fun processMessageCardData(msg: String, cardList: MutableList<MessageCardData>) {
            try {
                cardList.clear()
                val tempMsg = msg.removePrefix("Received message: ")
                val jsonObject = JSONObject(tempMsg)
                val data = jsonObject.getJSONObject("data").getJSONArray("userConversations")


                for (i in 0 until data.length()) {

                    val messageCardContent = data.getJSONObject(i)

                    Log.d("MessageCardData", messageCardContent.getInt("userID").toString())

                    cardList.add(
                        MessageCardData(
                            messageCardContent.getString("username"),
                            messageCardContent.getString("firstName"),
                            messageCardContent.getString("content"),
                            messageCardContent.getString("time"),
                            messageCardContent.getInt("userID"),
                            messageCardContent.getInt("friendEntityID"),
                            messageCardContent.getInt("conversationID")
                        )
                    )
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }


        fun getProfileImageUrl(userID: Int): String {
            return "${UrlHolder.URL}/user/${userID}/profileImage"
        }

        fun getGroupMembers(
            context: Activity,
            url: String,
            groupList: MutableList<Members>,
            adminID: MutableIntState
        ) {
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                { response ->
                    try {
                        val data = response.getJSONObject("data").getJSONArray("members")

                        adminID.intValue = response.getJSONObject("data").getInt("userID")


                        for (i in 0 until data.length()) {
                            val member = data.getJSONObject(i)
                            groupList.add(
                                Members(
                                    member.getInt("userID"),
                                    member.getString("username")
                                )
                            )
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                { error ->
                    Toast.makeText(context, NetworkUtils.errorResponse(error), Toast.LENGTH_LONG)
                        .show()
                }

            )

            VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest)
        }
    }
}