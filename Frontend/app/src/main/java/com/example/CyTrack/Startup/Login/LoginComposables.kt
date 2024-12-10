package com.example.CyTrack.Startup.Login

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.CyTrack.R
import com.example.CyTrack.Startup.SignUp.Composables.InputField
import com.example.CyTrack.Startup.SignUp.Composables.LogoIcon
import com.example.CyTrack.Startup.SignUp.Composables.TextButtonLink
import com.example.CyTrack.Utilities.ComposeUtils
import com.example.compose.CyRedDark
import com.example.compose.CyRedMain
import com.example.compose.CyYellow
import com.example.compose.OffWhite
import com.example.compose.OnWhiteSecondary


@Preview
@Composable
private fun PreviewTextButtonLink() {
    Surface(
        color = Color.White
    ) {
        TextButtonLink(
            text = "Forgot Password?",
            onClick = {}
        )
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
private fun PreviewInputField() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White
    ) {
        InputField(
            text = mutableStateOf(""),
            hint = "Username"
        )
    }
}

@Preview
@Composable
private fun PreviewLogoIcon() {
    Surface(
        modifier = Modifier.fillMaxWidth()
    ) {
        LogoIcon(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .padding(bottom = 10.dp)
        )
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
private fun PreviewLoginScreen() {
    LoginScreen(
        username = mutableStateOf(""),
        password = mutableStateOf(""),
    )
}

@Composable
fun LoginScreen(
    username: MutableState<String> = mutableStateOf(""),
    password: MutableState<String> = mutableStateOf(""),
    onForgotPassword: () -> Unit = {},
    onLogin: () -> Unit = {},
    onSignUp: () -> Unit = {}
) {
    val allowLogin = username.value.isNotEmpty() && password.value.isNotEmpty()


    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            LogoIcon(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 10.dp)
            )

            Spacer(modifier = Modifier.size(30.dp))

            InputField(
                text = username,
                hint = "Username",
                modifier = Modifier.padding(horizontal = 45.dp)
            )

            Spacer(modifier = Modifier.size(20.dp))

            InputField(
                text = password,
                hint = "Password",
                isPassword = true,
                modifier = Modifier.padding(horizontal = 45.dp)
            )

            Spacer(modifier = Modifier.size(10.dp))

            TextButtonLink(
                text = "Forgot Password?",
                onClick = { onForgotPassword() },
                modifier = Modifier
                    .padding(horizontal = 56.dp)
                    .padding(bottom = 10.dp)
            )

            Spacer(modifier = Modifier.size(31.dp))


            Button(
                onClick = {
                    if (allowLogin) {
                        onLogin()
                    } else {
                    }
                },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    containerColor = CyRedMain
                ),
                modifier = Modifier
                    .padding(horizontal = 45.dp)
                    .fillMaxWidth()
                    .then(if (!allowLogin) Modifier.alpha(0.5f) else Modifier)
            ) {
                Text(
                    text = "Login",
                    fontFamily = ComposeUtils.getCustomFontFamily(
                        "Inter",
                        FontWeight.ExtraBold,
                        FontStyle.Normal
                    ),
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextButtonLink(
                text = "Don't have an account?",
                onClick = { onSignUp() },
                modifier = Modifier.padding(bottom = 42.dp)
            )
        }

    }
}