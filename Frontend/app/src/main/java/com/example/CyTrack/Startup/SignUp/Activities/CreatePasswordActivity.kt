package com.example.CyTrack.Startup.SignUp.Activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.CyTrack.R
import com.example.CyTrack.Startup.SignUp.Composables.CreatePasswordScreen
import com.example.CyTrack.Utilities.StatusBarUtil
import com.example.CyTrack.Utilities.User

class CreatePasswordActivity : ComponentActivity() {

    private lateinit var user: User
    private lateinit var oldUser: User

    private lateinit var password: MutableState<String>
    private lateinit var confirmPassword: MutableState<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        oldUser = intent.getSerializableExtra("user") as User
        StatusBarUtil.setStatusBarColor(this, R.color.white)

        setContent{
            password = remember { mutableStateOf("") }
            confirmPassword = remember { mutableStateOf("") }

            CreatePasswordScreen(
                password = password,
                confirmPassword = confirmPassword,
                onContinue = {
                    // Continue to the next screen
                    user = User(
                        0,
                        oldUser.username,
                        oldUser.firstName,
                        oldUser.lastName,
                        oldUser.age,
                        oldUser.gender,
                        0,
                        oldUser.weight,
                        oldUser.height,
                        ""
                    )
                    navigateToUploadCompleteSignUpActivity()
                },
                onBack = {
                    finish()
                }
            )
        }
    }

    private fun navigateToUploadCompleteSignUpActivity() {
        // Navigate to the next screen
        val intent = Intent(
            this@CreatePasswordActivity,
            CompleteSignUpActivity::class.java
        )
        intent.putExtra("user", user)
        intent.putExtra("password", password.value)
        startActivity(intent)
    }
}
