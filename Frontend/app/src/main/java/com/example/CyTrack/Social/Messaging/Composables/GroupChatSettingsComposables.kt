package com.example.CyTrack.Social.Messaging.Composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.CyTrack.Social.Friends.ListProfileCard
import com.example.CyTrack.Social.SocialUtils
import com.example.CyTrack.Utilities.ComposeUtils.Companion.getCustomFontFamily
import com.example.CyTrack.Utilities.User
import com.example.compose.CyRedMain

@Composable
fun GroupChatSettingCard(
    user: User,
    isAdmin: Boolean,
    onProfileClick: (User) -> Unit = {},
    onDelete: () -> Unit
) {
    Box {
        ListProfileCard(
            user.firstName,
            user.username,
            SocialUtils.getProfileImageUrl(user.id)
        )

        if (isAdmin) {
            Button(
                onClick = onDelete,
                colors = ButtonDefaults.buttonColors(containerColor = CyRedMain),
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .matchParentSize()
                    .height(25.dp)
                    .width(100.dp)
                    .offset(x = (-50).dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = "Message",
                    fontFamily = getCustomFontFamily(
                        "Inter",
                        FontWeight.SemiBold,
                        FontStyle.Normal
                    ),
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }

    }
}