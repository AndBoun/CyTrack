package com.example.CyTrack.Startup.SignUp.Activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.CyTrack.R
import com.example.CyTrack.Startup.LoginActivity
import com.example.CyTrack.Startup.SignUp.Composables.UsernameScreen
import com.example.CyTrack.Utilities.StatusBarUtil
import com.example.CyTrack.Utilities.UrlHolder
import com.example.CyTrack.Utilities.User

class UsernameActivity : ComponentActivity() {
    private lateinit var user: User

    private lateinit var username: MutableState<String>

    private val URL = UrlHolder.URL + "/user"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtil.setStatusBarColor(this, R.color.white)
        setContent {
            username = remember { mutableStateOf("") }

            UsernameScreen(
                username = username,
                onContinue = {
                    // Continue to the next screen
                    user = User(0, username.value, null, null, 0, null, 0)
                    navigateToUserInfo()
                },
                onBack = {
                    finish()
                }
            )
        }

    }

    private fun navigateToUserInfo() {
        val intent = Intent(
            this@UsernameActivity,
            UserInfoActivity::class.java
        )
        intent.putExtra("user", user)
        startActivity(intent)
    }
}
