package com.example.CyTrack.Social.Messaging.Activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.CyTrack.Social.Messaging.Members
import com.example.CyTrack.Social.SocialUtils
import com.example.CyTrack.Utilities.NetworkUtils
import com.example.CyTrack.Utilities.UrlHolder
import com.example.CyTrack.Utilities.User
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
            members = remember { mutableListOf() }
            users = remember { mutableListOf() }

            getMembers()
            getUsers()

            val isAdmin = user.id == adminID.intValue

        }
    }

    private fun getMembers() {
        val getURL = "${UrlHolder.URL}/${user.id}/groupchat/getMembers/${groupChatID}"
        SocialUtils.getGroupMembers(this, getURL, members, adminID)
    }

    private fun getUsers() {
        for (member in members) {
            val getURL = "${UrlHolder.URL}/user/${member.userID}"

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