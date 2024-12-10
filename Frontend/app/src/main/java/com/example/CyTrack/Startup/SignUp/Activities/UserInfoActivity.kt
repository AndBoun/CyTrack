package com.example.CyTrack.Startup.SignUp.Activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.CyTrack.R
import com.example.CyTrack.Startup.SignUp.Composables.UserInfoScreen
import com.example.CyTrack.Startup.SignUp.Composables.UsernameScreen
import com.example.CyTrack.Utilities.StatusBarUtil
import com.example.CyTrack.Utilities.UrlHolder
import com.example.CyTrack.Utilities.User

class UserInfoActivity : ComponentActivity() {
    private lateinit var user: User

    private lateinit var firstName: MutableState<String>
    private lateinit var lastName: MutableState<String>
    private lateinit var weight: MutableState<String>
    private lateinit var height: MutableState<String>
    private lateinit var age: MutableState<String>
    private lateinit var gender: MutableState<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtil.setStatusBarColor(this, R.color.white)
        setContent {

            UserInfoScreen(
                firstName = firstName,
                lastName = lastName,
                weight = weight,
                height = height,
                age = age,
                gender = gender,
                onContinue = {
                    // Continue to the next screen
                },
                onBack = {
                    finish()
                }
            )
        }

    }
}