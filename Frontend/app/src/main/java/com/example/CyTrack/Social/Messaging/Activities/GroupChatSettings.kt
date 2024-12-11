package com.example.CyTrack.Social.Messaging.Activities

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.example.CyTrack.Social.Friends.Friend
import com.example.CyTrack.Social.Messaging.Composables.GroupChatSettingsLazyList
import com.example.CyTrack.Social.Messaging.Members
import com.example.CyTrack.Social.SocialUtils
import com.example.CyTrack.Utilities.NetworkUtils
import com.example.CyTrack.Utilities.UrlHolder
import com.example.CyTrack.Utilities.User
import com.example.CyTrack.Utilities.VolleySingleton
import org.json.JSONException
import kotlin.properties.Delegates

class GroupChatSettings : ComponentActivity() {

    private lateinit var user: User

    private var groupChatID by Delegates.notNull<Int>()

    private lateinit var groupName: String

    private lateinit var adminID: MutableIntState

    private var members: MutableList<Members> = mutableListOf()

    private var users: MutableList<User> = mutableListOf()

    private val URL = UrlHolder.URL

//    private val URL = "${UrlHolder.URL}/${user.id}/groupchat/getMembers/${groupChatID}"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        user = intent.getSerializableExtra("user") as User
        groupChatID = intent.getIntExtra("groupChatID", -1)
        groupName = intent.getStringExtra("groupName") as String


        setContent {
            adminID = remember { mutableIntStateOf(-1) }
//            members = remember { mutableStateListOf() }
            users = remember { mutableStateListOf() }

            getMembers()
            getUsers()

            val isAdmin = user.id == adminID.intValue

            Surface(
                modifier = Modifier.fillMaxSize()
            ) {
                GroupChatSettingsLazyList(
                    users = users,
                    isAdmin = isAdmin,
                    onProfileClick = {
                        SocialUtils.switchToFriendProfile(
                            this,
                            user,
                            Friend(it.firstName, it.username, it.id, -1)
                        )
                    },
                    onDelete = {
                        removeMember(it.id)
                        users.remove(it)
                    },
                    modifier = Modifier.fillMaxSize()
                        .padding(horizontal = 30.dp)
                )
            }

        }
    }

    private fun removeMember(userID: Int) {
        val deleteURL = "${UrlHolder.URL}/${user.id}/groupchat/removeMember/${groupChatID}/${userID}"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.DELETE,
            deleteURL,
            null,
            { response ->
                try {
                    val message = response.getString("message")
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { error ->
                Toast.makeText(this, NetworkUtils.errorResponse(error), Toast.LENGTH_LONG)
                    .show()
            }

        )
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    private fun getMembers() {
        val getURL = "${UrlHolder.URL}/${user.id}/groupchat/getMembers/${groupChatID}"
        Log.d("getMembers", getURL)
        SocialUtils.getGroupMembers(this, getURL, members, adminID)
    }

    private fun getUsers() {
        for (member in members) {
            val getURL = "${UrlHolder.URL}/user/${member.userID}"
            Log.d("getUsers", getURL)

            NetworkUtils.fetchUserData(this, getURL, object : NetworkUtils.fetchUserDataCallback {
                override fun onSuccess(user: User, message: String) {
                    users.add(user)
                }

                override fun onError(error: String) {
                    Toast.makeText(applicationContext, error, Toast.LENGTH_LONG).show()
                }
            })
        }

    }
}