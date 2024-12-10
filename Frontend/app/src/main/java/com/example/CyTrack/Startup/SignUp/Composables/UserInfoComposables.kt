package com.example.CyTrack.Startup.SignUp.Composables

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.CyTrack.Utilities.ComposeUtils
import com.example.compose.CyRedMain
import com.example.compose.OffWhite
import com.example.compose.OnWhiteSecondary


@Composable
fun DropDownGender(
    userGender: MutableState<String>,
) {
    val isDropDownExpanded = remember { mutableStateOf(false) }
    val selectedGender = remember { mutableStateOf("Gender") }
    val genders = listOf("M", "F")

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = selectedGender.value,
            color = if (selectedGender.value == "M" || selectedGender.value == "F") Color.Black else OnWhiteSecondary,
            fontWeight = FontWeight.SemiBold,
            fontFamily = ComposeUtils.getCustomFontFamily(
                "Inter",
                FontWeight.SemiBold,
                FontStyle.Normal
            ),
            fontSize = 15.sp,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isDropDownExpanded.value = true }
                .background(OffWhite, RoundedCornerShape(10.dp))
                .padding(20.dp)
        )

        DropdownMenu(
            expanded = isDropDownExpanded.value,
            onDismissRequest = { isDropDownExpanded.value = false },
            shape = RoundedCornerShape(10.dp),
            containerColor = OffWhite,
        ) {
            genders.forEach { gender ->
                DropdownMenuItem(
                    text = { Text(gender) },
                    onClick = {
                        selectedGender.value = gender
                        isDropDownExpanded.value = false
                        userGender.value = gender
                    }
                )
            }
        }
    }
}

@Composable
private fun InputUserInfo(
    firstName: MutableState<String>,
    lastName: MutableState<String>,
    weight: MutableState<String>,
    height: MutableState<String>,
    age: MutableState<String>,
    gender: MutableState<String>,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 46.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            InputField(text = firstName, hint = "First Name", modifier = Modifier.width(145.dp))
            InputField(text = lastName, hint = "Last Name", modifier = Modifier.width(145.dp))
        }

        Spacer(modifier = Modifier.size(15.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            InputField(
                text = weight,
                hint = "Weight",
                modifier = Modifier.width(90.dp),
                keyboardType = KeyboardType.Number
            )

            InputField(
                text = height,
                hint = "Height",
                modifier = Modifier.width(90.dp),
                keyboardType = KeyboardType.Number
            )

            InputField(
                text = age,
                hint = "Age",
                modifier = Modifier.width(90.dp),
                keyboardType = KeyboardType.Number
            )
        }

        Spacer(modifier = Modifier.size(15.dp))

        DropDownGender(gender)

    }
}

@Composable
fun UserInfoScreen(
    firstName: MutableState<String>,
    lastName: MutableState<String>,
    weight: MutableState<String>,
    height: MutableState<String>,
    age: MutableState<String>,
    gender: MutableState<String>,
    onContinue: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    var allowContinue =
        firstName.value.isNotEmpty() && lastName.value.isNotEmpty() && weight.value.isNotEmpty() && height.value.isNotEmpty() && age.value.isNotEmpty() && gender.value.isNotEmpty()
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            UserInstruction(
                icon = Icons.Default.Contacts,
                text = "Let’s Get to Know You",
                modifier = Modifier.size(90.dp)
            )

            Spacer(modifier = Modifier.size(42.dp))

            InputUserInfo(
                firstName = firstName,
                lastName = lastName,
                weight = weight,
                height = height,
                age = age,
                gender = gender
            )

            Spacer(modifier = Modifier.size(31.dp))

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
                    .padding(horizontal = 45.dp)
                    .fillMaxWidth()
                    .then(if (!allowContinue) Modifier.alpha(0.5f) else Modifier)
            ) {
                Text(
                    text = "Continue",
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
fun PreviewUserInfoScreen() {
    UserInfoScreen(
        firstName = mutableStateOf(""),
        lastName = mutableStateOf(""),
        weight = mutableStateOf(""),
        height = mutableStateOf(""),
        age = mutableStateOf(""),
        gender = mutableStateOf("")
    )
}