package com.example.CyTrack.Startup.Login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.CyTrack.Dashboard.MainDashboardActivity
import com.example.CyTrack.R
import com.example.CyTrack.Startup.ForgotPasswordActivity
import com.example.CyTrack.Startup.SignUp.Activities.UsernameActivity
import com.example.CyTrack.Utilities.NetworkUtils
import com.example.CyTrack.Utilities.NetworkUtils.fetchUserDataCallback
import com.example.CyTrack.Utilities.NetworkUtils.postUserAndGetIDCallback
import com.example.CyTrack.Utilities.StatusBarUtil
import com.example.CyTrack.Utilities.UrlHolder
import com.example.CyTrack.Utilities.User

class LoginActivity : ComponentActivity() {

    private lateinit var user: User

    private lateinit var username: MutableState<String>

    private lateinit var password: MutableState<String>

    private val URL_LOGIN = "${UrlHolder.URL}/user/login"

    private val URL_GET_USER = "${UrlHolder.URL}/user"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtil.setStatusBarColor(this, R.color.white)
        setContent {
            username = remember { mutableStateOf("") }
            password = remember { mutableStateOf("") }

            LoginScreen(
                username = username,
                password = password,
                onLogin = {
                    login(username.value, password.value)
                },
                onForgotPassword = {
                    navigateToForgotPassword()
                },
                onSignUp = {
                    navigateToSignUp()
                }
            )
        }

    }

    /**
     * Initiates the login process by sending the username and password to the server.
     *
     * @param username The username entered by the user.
     * @param password The password entered by the user.
     */
    private fun login(username: String, password: String) {
        val params: MutableMap<String, String> = HashMap()
        params["username"] = username
        params["password"] = password

        NetworkUtils.postUserAndGetID(
            applicationContext,
            URL_LOGIN,
            params,
            object : postUserAndGetIDCallback {
                override fun onSuccess(id: Int, message: String) {
                    if (id != 0) fetchUserData(URL_GET_USER + "/" + id)
                }

                override fun onError(error: String) {
                    Toast.makeText(applicationContext, error, Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun fetchUserData(url: String) {
        NetworkUtils.fetchUserData(this, url, object : fetchUserDataCallback {
            override fun onSuccess(user: User, message: String) {
                this@LoginActivity.user = user
                Toast.makeText(applicationContext, "Signing In", Toast.LENGTH_LONG).show()
                navigateToMainDashboard()
            }

            override fun onError(error: String) {
                Toast.makeText(applicationContext, error, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun navigateToMainDashboard() {
        val intent = Intent(
            this@LoginActivity,
            MainDashboardActivity::class.java
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.putExtra("user", user)
        startActivity(intent)
    }

    private fun navigateToForgotPassword() {
        val intent = Intent(
            this@LoginActivity,
            ForgotPasswordActivity::class.java
        )
        startActivity(intent)
    }

    private fun navigateToSignUp() {

        val intent = Intent(
            this@LoginActivity,
            UsernameActivity::class.java
        )
        startActivity(intent)
    }
}
