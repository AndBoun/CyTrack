package com.example.CyTrack.Startup.ForgotPassword.Composables

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.CyTrack.Startup.SignUp.Composables.InputField
import com.example.CyTrack.Startup.SignUp.Composables.TextButtonLink
import com.example.CyTrack.Startup.SignUp.Composables.UserInstruction
import com.example.CyTrack.Utilities.ComposeUtils
import com.example.compose.CyRedMain

@Composable
fun CheckUsernameScreen(
    username: MutableState<String>,
    onContinue: () -> Unit,
    onBack: () -> Unit
) {
    val allowContinue = username.value.isNotEmpty()

    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 46.dp)
        ) {
            UserInstruction(
                icon = Icons.Default.Lock,
                text = "Let's Check for Your Username",
                modifier = Modifier.size(95.dp)
            )

            Spacer(modifier = Modifier.size(42.dp))

            InputField(
                text = username,
                hint = "Username",
//                modifier = Modifier.padding(horizontal = 46.dp)
            )

            Spacer(modifier = Modifier.size(46.dp))

            Button(
                onClick = {
                    if (allowContinue) {
                        onContinue()
                    } else {
                    }
                },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    containerColor = CyRedMain
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .then(if (!allowContinue) Modifier.alpha(0.5f) else Modifier)
            ) {
                Text(
                    text = "Check Username",
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
                text = "Back",
                onClick = { onBack() },
                modifier = Modifier.padding(bottom = 42.dp)
            )
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
private fun PreviewCheckUsernameScreen() {
    CheckUsernameScreen(
        username = mutableStateOf(""),
        onContinue = {},
        onBack = {}
    )
}
