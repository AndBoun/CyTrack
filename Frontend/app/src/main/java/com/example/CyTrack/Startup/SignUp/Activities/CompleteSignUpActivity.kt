package com.example.CyTrack.Startup.SignUp.Activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.CyTrack.R
import com.example.CyTrack.Startup.Login.LoginActivity
import com.example.CyTrack.Startup.SignUp.Composables.CompleteSignUpScreen
import com.example.CyTrack.Utilities.KtorClientSingleton
import com.example.CyTrack.Utilities.NetworkUtils
import com.example.CyTrack.Utilities.StatusBarUtil
import com.example.CyTrack.Utilities.UrlHolder
import com.example.CyTrack.Utilities.User
import kotlin.math.sign

class CompleteSignUpActivity : ComponentActivity() {

    lateinit var user: User
    lateinit var oldUser: User
    lateinit var password: String

    private val URL = UrlHolder.URL + "/user"

    // TODO
    private val IMAGE_URL = UrlHolder.URL


    lateinit var profileImg: MutableState<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtil.setStatusBarColor(this, R.color.white)

        oldUser = intent.getSerializableExtra("user") as User
        password = intent.getStringExtra("password").toString()
        setContent{
            profileImg = remember { mutableStateOf("") }

            CompleteSignUpScreen(
                profileImg = profileImg,
                onContinue = {
                    signUpUser(
                        oldUser.username,
                        password,
                        oldUser.firstName,
                        oldUser.lastName,
                        oldUser.age.toString(),
                        oldUser.gender,
                        oldUser.weight.toString(),
                        oldUser.height.toString(),
                        profileImg.value
                    )
                },
                onBack = {
                    finish()
                }
            )


        }
    }

    private fun signUpUser(
        username: String,
        password: String,
        firstName: String,
        lastName: String,
        age: String,
        gender: String,
        weight: String,
        height: String,
        profileImg: String
    ) {
        val params: MutableMap<String, String> = HashMap()
        params["username"] = username
        params["password"] = password
        params["firstName"] = firstName
        params["lastName"] = lastName
        params["age"] = age
        params["gender"] = gender
        params["weight"] = weight
        params["height"] = height
        params["profileImg"] = profileImg

        NetworkUtils.postData(
            applicationContext,
            URL,
            params,
            object : NetworkUtils.callbackMessage {
                override fun onSuccess(response: String) {
                    Toast.makeText(applicationContext, "Signed Up", Toast.LENGTH_LONG).show()
                    KtorClientSingleton.uploadImage(applicationContext, IMAGE_URL, Uri.parse(profileImg))
                    navigateToLogin()
                }

                override fun onError(error: String) {
                    Toast.makeText(applicationContext, error, Toast.LENGTH_LONG).show()
                }
            })

        Log.d("SignUpActivity", "Request added to queue")
    }

    private fun navigateToLogin(){
        val intent = Intent(
            this@CompleteSignUpActivity,
            LoginActivity::class.java
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }
}