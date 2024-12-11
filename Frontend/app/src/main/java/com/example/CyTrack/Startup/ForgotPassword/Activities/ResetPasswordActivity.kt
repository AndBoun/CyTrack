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
import com.example.CyTrack.Startup.ForgotPassword.Composables.ResetPasswordScreen
import com.example.CyTrack.Startup.Login.LoginActivity
import com.example.CyTrack.Startup.SignUp.Composables.BasicRedCircularLoadingScreen
import com.example.CyTrack.Utilities.NetworkUtils
import com.example.CyTrack.Utilities.StatusBarUtil
import com.example.CyTrack.Utilities.UrlHolder

class ResetPasswordActivity : ComponentActivity() {

    private lateinit var password: MutableState<String>

    private lateinit var confirmPassword: MutableState<String>

    private lateinit var loading: MutableState<Boolean>

    private lateinit var username: String

    private lateinit var userID: String

    private val URL_RESET_PASSWORD = UrlHolder.URL + "/user/resetPassword"

    override fun onCreate(savedInstanceState: Bundle?) {
        StatusBarUtil.setStatusBarColor(this, R.color.white)
        username = intent.getStringExtra("username")!!
        userID = intent.getStringExtra("userID")!!
        super.onCreate(savedInstanceState)

        setContent {
            password = remember { mutableStateOf("") }
            confirmPassword = remember { mutableStateOf("") }
            loading = remember { mutableStateOf(false) }

            if (loading.value) {
                BasicRedCircularLoadingScreen(loading)
            }

            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .then(if (loading.value) Modifier.alpha(0.5f) else Modifier)
            ) {
                ResetPasswordScreen(
                    password = password,
                    confirmPassword = confirmPassword,
                    onContinue = {
                        loading.value = true
                        resetPassword("${URL_RESET_PASSWORD}/${userID}", password.value)
                    },
                    onBack = {
                        finish()
                    }
                )
            }

        }
    }

    /**
     * Resets the password for the user.
     *
     * @param url      The URL for resetting the password.
     * @param password The new password to be set.
     */
    private fun resetPassword(url: String, password: String) {
        val params: MutableMap<String, String> = HashMap()
        params["password"] = password

        NetworkUtils.modifyData(
            applicationContext,
            url,
            params,
            object : NetworkUtils.callbackMessage {
                override fun onSuccess(response: String) {
                    loading.value = false
                    Toast.makeText(applicationContext, "Password Reset", Toast.LENGTH_LONG).show()
                    navigateToLogin()
                }

                override fun onError(error: String) {
                    loading.value = false

                    Toast.makeText(applicationContext, error, Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun navigateToLogin() {
        val intent = Intent(
            this@ResetPasswordActivity,
            LoginActivity::class.java
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }
}