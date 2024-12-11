package com.example.CyTrack.Social.Messaging.Activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.CyTrack.Social.SocialUtils
import com.example.CyTrack.Utilities.UrlHolder
import com.example.CyTrack.Utilities.User
import kotlin.properties.Delegates

class GroupChatSettings : ComponentActivity() {

    private lateinit var user: User

    private var groupChatID by Delegates.notNull<Int>()

    private lateinit var groupName: String

    private val URL = UrlHolder.URL

//    private val URL = "${UrlHolder.URL}/${user.id}/groupchat/getMembers/${groupChatID}"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        user = intent.getSerializableExtra("user") as User
        groupChatID = intent.getIntExtra("groupChatID", -1)
        groupName = intent.getStringExtra("groupName") as String

        setContent{

        }
    }

    private fun getMembers() {
        val getURL = "${UrlHolder.URL}/${user.id}/groupchat/getMembers/${groupChatID}"

        SocialUtils.

    }
}