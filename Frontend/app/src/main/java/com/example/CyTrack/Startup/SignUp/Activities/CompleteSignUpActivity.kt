package com.example.CyTrack.Startup.SignUp.Activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import com.example.CyTrack.R
import com.example.CyTrack.Startup.Login.LoginActivity
import com.example.CyTrack.Startup.SignUp.Composables.CompleteSignUpScreen
import com.example.CyTrack.Utilities.KtorClientSingleton
import com.example.CyTrack.Utilities.NetworkUtils
import com.example.CyTrack.Utilities.StatusBarUtil
import com.example.CyTrack.Utilities.UrlHolder
import com.example.CyTrack.Utilities.User
import com.example.compose.CyRedMain
import kotlin.math.sign

class CompleteSignUpActivity : ComponentActivity() {

    lateinit var user: User
    lateinit var oldUser: User
    lateinit var password: String

    private val URL = UrlHolder.URL + "/user"



    lateinit var profileImg: MutableState<String>
    lateinit var loading: MutableState<Boolean>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtil.setStatusBarColor(this, R.color.white)

        oldUser = intent.getSerializableExtra("user") as User
        password = intent.getStringExtra("password").toString()
        setContent {
            profileImg = remember { mutableStateOf("") }
            loading = remember { mutableStateOf(false) }

            if (loading.value) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    // Loading screen
                    CircularProgressIndicator(
                        color = CyRedMain,
                        modifier = Modifier.size(50.dp)
                    )
                }
            }

            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .then(if (loading.value) Modifier.alpha(0.5f) else Modifier)
            ) {
                CompleteSignUpScreen(
                    profileImg = profileImg,
                    onContinue = {
                        loading.value = true
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

        val imageUrl = "${URL}/${user.id}/profileImage"

        Log.d("CompleteSignUpActivity", "Image URI: $profileImg")


        NetworkUtils.postData(
            applicationContext,
            URL,
            params,
            object : NetworkUtils.callbackMessage {
                override fun onSuccess(response: String) {
                    Toast.makeText(applicationContext, "Signed Up", Toast.LENGTH_LONG).show()
                    KtorClientSingleton.uploadImage(
                        applicationContext,
                        imageUrl,
                        Uri.parse(profileImg)
                    )
                    loading.value = false
                    navigateToLogin()
                }

                override fun onError(error: String) {
                    loading.value = false
                    Toast.makeText(applicationContext, error, Toast.LENGTH_LONG).show()
                }
            })

        Log.d("SignUpActivity", "Request added to queue")
    }

    private fun navigateToLogin() {
        val intent = Intent(
            this@CompleteSignUpActivity,
            LoginActivity::class.java
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }
}