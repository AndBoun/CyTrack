package com.example.CyTrack.Startup.SignUp.Composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.CyTrack.R
import com.example.CyTrack.Social.ImagePicker
import com.example.CyTrack.Social.ProfileImage
import com.example.CyTrack.Utilities.ComposeUtils
import com.example.compose.CyRedDark
import com.example.compose.CyRedMain
import com.example.compose.CyYellow
import com.example.compose.OffWhite
import com.example.compose.OnWhiteSecondary

@Composable
fun ProfileImageUpdate(
    profileImg: MutableState<String>,
    modifier: Modifier = Modifier,
){
    Box {
        ProfileImage(
            imageUrl = profileImg,
            modifier = modifier
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = (-6).dp, y = (-6).dp)
        ) {
            ImagePicker(
                profileImgUri = profileImg,
                buttonContent = { onClick ->
                    UploadImageButton(onClick)
                }
            )
        }
    }
}

@Composable
fun LogoIcon(
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(horizontal = 38.dp)
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.cylogo),
                contentDescription = "Logo",
                modifier = Modifier.size(87.dp)
            )

            Text(
                text = "CyTrack",
                fontFamily = ComposeUtils.getCustomFontFamily(
                    "Inter",
                    FontWeight.Black,
                    FontStyle.Italic
                ),
                color = CyYellow,
                fontSize = 40.sp,
                fontWeight = FontWeight.Black,
                fontStyle = FontStyle.Italic
            )
        }

        Spacer(modifier = Modifier.size(2.dp))

        HorizontalDivider(
            color = OnWhiteSecondary,
            thickness = 1.5.dp,
            modifier = Modifier.fillMaxWidth()
        )

    }
}

@Composable
fun InputField(
    text: MutableState<String>,
    hint: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
    modifier: Modifier = Modifier
) {
    TextField(
        value = text.value,
        onValueChange = { newValue ->
            if (keyboardType == KeyboardType.Number) {
                text.value = newValue.filter { it.isDigit() }
            } else {
                text.value = newValue
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        label = {
            Text(
                text = hint,
                fontFamily = ComposeUtils.getCustomFontFamily(
                    "Inter",
                    FontWeight.Black,
                    FontStyle.Italic
                ),
                color = OnWhiteSecondary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
            )
        },
        maxLines = 1,
        textStyle = TextStyle(
            color = Color.Black,
            fontSize = 15.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = ComposeUtils.getCustomFontFamily(
                "Inter",
                FontWeight.Black,
                FontStyle.Normal
            ),
        ),
        shape = RoundedCornerShape(10.dp),
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = OffWhite,
            focusedContainerColor = OffWhite,

            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,

            cursorColor = Color.Black
        ),
        modifier = modifier
            .fillMaxWidth()
    )
}

@Composable
fun TextButtonLink(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        fontFamily = ComposeUtils.getCustomFontFamily(
            "Inter",
            FontWeight.Black,
            FontStyle.Italic
        ),
        color = CyRedDark,
        fontSize = 10.sp,
        fontWeight = FontWeight.SemiBold,
        textDecoration = TextDecoration.Underline,
        modifier = modifier.clickable(onClick = onClick)
    )
}

@Composable
fun UserInstruction(
    icon: ImageVector = Icons.Default.Contacts,
    text: String = "INPUT",
    modifier: Modifier = Modifier
){
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Icon(
            imageVector = icon,
            contentDescription = "User Icon",
            tint = Color.Black,
            modifier = modifier
        )

        Spacer(modifier = Modifier.size(30.dp))

        Text(
            text = text,
            color = OnWhiteSecondary,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = ComposeUtils.getCustomFontFamily(
                "Inter",
                FontWeight.Medium,
                FontStyle.Normal)
        )
    }
}

@Composable
fun UploadImageButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = CyRedMain,
        shape = RoundedCornerShape(50.dp),
        modifier = modifier.size(30.dp),
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier.padding(5.dp)
        ) {
            Icon(
                imageVector = Icons.Default.UploadFile,
                contentDescription = "Upload Image",
                tint = Color.White
            )
        }
    }
}