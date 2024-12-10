package com.example.CyTrack.Startup.SignUp.Activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.CyTrack.R
import com.example.CyTrack.Utilities.StatusBarUtil

class CreatePasswordActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtil.setStatusBarColor(this, R.color.white)

        setContent{

        }
    }
}