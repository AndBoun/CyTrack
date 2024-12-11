package com.example.CyTrack.Social.Messaging.Activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.CyTrack.R
import com.example.CyTrack.Utilities.StatusBarUtil
import com.example.CyTrack.Utilities.User

class GroupChat : ComponentActivity() {

    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtil.setStatusBarColor(this, R.color.CyRed)

        user = intent.getSerializableExtra("user") as User

        setContent {

        }
    }
}