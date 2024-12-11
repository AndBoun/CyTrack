package com.example.CyTrack.Startup.ForgotPassword.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import com.example.CyTrack.Dashboard.MainDashboardActivity
import com.example.CyTrack.R
import com.example.CyTrack.Startup.ForgotPassword.Composables.CheckUsernameScreen
import com.example.CyTrack.Startup.SignUp.Composables.BasicRedCircularLoadingScreen
import com.example.CyTrack.Utilities.NetworkUtils
import com.example.CyTrack.Utilities.NetworkUtils.postUserAndGetIDCallback
import com.example.CyTrack.Utilities.StatusBarUtil
import com.example.CyTrack.Utilities.UrlHolder

class CheckUsernameActivity : ComponentActivity() {

    private lateinit var username: MutableState<String>

    private lateinit var loading: MutableState<Boolean>

    private val URL_RESET_PASSWORD = UrlHolder.URL + "/user/resetPassword"

    private var tempID = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtil.setStatusBarColor(this, R.color.white)

        setContent {
            username = remember { mutableStateOf("") }
            loading = remember { mutableStateOf(false) }

            if (loading.value) {
                BasicRedCircularLoadingScreen(loading)
            }

            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .then(if (loading.value) Modifier.alpha(0.5f) else Modifier)
            ) {
                CheckUsernameScreen(
                    username = username,
                    onContinue = {
                        loading.value = true
                        checkUsernameAndPassword(username.value, "")
                    },
                    onBack = {
                        finish()
                    }
                )
            }


        }
    }

    /**
     * Checks the username and password, and initiates the password reset process.
     *
     * @param username The username entered by the user.
     * @param password The new password entered by the user.
     */
    private fun checkUsernameAndPassword(username: String, password: String) {
        val params: MutableMap<String, String> = HashMap()
        params["username"] = username
        params["password"] = password

        NetworkUtils.postUserAndGetID(
            applicationContext,
            URL_RESET_PASSWORD,
            params,
            object : postUserAndGetIDCallback {
                override fun onSuccess(userID: Int, message: String) {
                    loading.value = false

                    if (userID != 0) {
                        // Navigate to ResetPasswordActivity
                        tempID = userID
                        navigateToResetPasswordActivity()
                    }
                }

                override fun onError(error: String) {
                    loading.value = false
                    Toast.makeText(applicationContext, error, Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun navigateToResetPasswordActivity() {
        val intent = Intent(
            this@CheckUsernameActivity,
            ResetPasswordActivity::class.java
        )
        intent.putExtra("username", username.value)
        intent.putExtra("userID", tempID.toString())
        startActivity(intent)
    }
}