package com.example.CyTrack.Startup.SignUp.Composables

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.CyTrack.Social.ImagePicker
import com.example.CyTrack.Social.ProfileImage
import com.example.CyTrack.Utilities.ComposeUtils
import com.example.compose.CyRedMain
import com.example.compose.OnWhiteSecondary



@Composable
fun CompleteSignUpScreen(
    profileImg: MutableState<String>,
    onContinue: () -> Unit,
    onBack: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

           ProfileImageUpdate(
                profileImg = profileImg,
                modifier = Modifier.size(170.dp)
           )



            Spacer(modifier = Modifier.size(33.dp))

            Text(
                text = "Upload Your Profile Picture",
                color = OnWhiteSecondary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = ComposeUtils.getCustomFontFamily("Inter")
            )

            Spacer(modifier = Modifier.size(70.dp))

            Button(
                onClick = {
                    onContinue()
                },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    containerColor = CyRedMain
                ),
                modifier = Modifier
                    .padding(horizontal = 45.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Create Account",
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
private fun PreviewCompleteSignUpScreen() {
    CompleteSignUpScreen(
        profileImg = mutableStateOf(""),
        onContinue = {},
        onBack = {}
    )
}