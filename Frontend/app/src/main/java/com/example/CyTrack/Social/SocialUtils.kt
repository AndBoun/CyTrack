package com.example.CyTrack.Social

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.example.CyTrack.Social.Friends.AddFriends
import com.example.CyTrack.Social.Friends.Friend
import com.example.CyTrack.Social.Friends.FriendProfile
import com.example.CyTrack.Social.Messaging.DirectMessage
import com.example.CyTrack.Social.WebSockets.WebSocketManagerMessages
import com.example.CyTrack.Utilities.NetworkUtils
import com.example.CyTrack.Utilities.UrlHolder
import com.example.CyTrack.Utilities.User
import com.example.CyTrack.Utilities.VolleySingleton
import com.example.CyTrack.Utilities.WebSocketServiceUtil
import org.json.JSONException
import org.json.JSONObject

class SocialUtils {


    companion object {
        @JvmStatic
        fun messageUserScreen(user: User, recipient: Friend, context: Activity) {
//            val serviceIntent = Intent(context, WebSocketServiceUtil::class.java).apply {
//                action = "CONNECT"
//                putExtra("key", "${user.id}_DM_${recipient.userID}")
//                putExtra("url", "${UrlHolder.wsURL}/chat/${user.id}/${recipient.userID}")
//
////                Log.d("WebSocketServiceUtil", "Connecting to ${UrlHolder.wsURL}/chat/${user.id}/${recipient.userID}")
//            }
//            context.startService(serviceIntent)


            val intent = Intent(context, DirectMessage::class.java).apply {
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

        fun processMessage(msg: String): DirectMessage.Msg {
            try {
                val jsonObject = JSONObject(msg)
                val data = jsonObject.getJSONObject("data")

                val userID = data.getInt("userID")
                val msgContent = data.getString("contentofmessage")

                return DirectMessage.Msg(msgContent, userID)

            } catch (e: JSONException) {
                e.printStackTrace()
            }

           return DirectMessage.Msg("", 0)
        }


    }
}