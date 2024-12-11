package com.example.CyTrack.Social.Messaging.Activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.CyTrack.R
import com.example.CyTrack.Social.Messaging.MessageCardData
import com.example.CyTrack.Utilities.StatusBarUtil
import com.example.CyTrack.Utilities.User

class MyGroupChats : ComponentActivity() {

    private lateinit var user: User

    private var messageCards: MutableList<MessageCardData> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        StatusBarUtil.setStatusBarColor(this, R.color.white)
        super.onCreate(savedInstanceState)

        user = intent.getSerializableExtra("user") as User

        setContent{

        }
    }

}