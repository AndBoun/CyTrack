package com.example.CyTrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class MyFriends : ComponentActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent() {
            val user = intent.getSerializableExtra("user") as User?
            if (user != null) {
                ProfileScreen(user.firstName, user.username, "temp")
            }
        }

        StatusBarUtil.setStatusBarColor(this, R.color.CyRed)
    }

}