package com.example.CyTrack.Social

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.CyTrack.R
import com.example.CyTrack.Utilities.User
import com.example.CyTrack.Utilities.StatusBarUtil

class MyProfile : ComponentActivity(){

    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val user = intent.getSerializableExtra("user") as User?
            if (user != null) {
                ProfileScreen(user.firstName, user.username, "temp")
            }
        }

        StatusBarUtil.setStatusBarColor(this, R.color.CyRed)
    }
}

fun getCustomFontFamily(fontName: String, fontWeight: FontWeight, fontStyle: FontStyle): FontFamily {
    val provider = GoogleFont.Provider(
        providerAuthority = "com.google.android.gms.fonts",
        providerPackage = "com.google.android.gms",
        certificates = R.array.com_google_android_gms_fonts_certs
    )
    val googleFont = GoogleFont(fontName)

    return FontFamily(
        Font(
            googleFont = googleFont,
            fontProvider = provider,
            weight = fontWeight,
            style = fontStyle
        )
    )
}


@Composable
fun MainProfileCard(
    name: String,
    userName: String,
    imageUrl: String,
    modifier: Modifier = Modifier
){
    Row(
        verticalAlignment = CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth(0.5f), // Take up half the screen width
            contentAlignment = Alignment.Center // Center the content within the Box
        ) {
            Image(
//            painter = rememberAsyncImagePainter(imageUrl),
                painter = painterResource(R.drawable.general_generic_avatar),
                contentDescription = "Profile picture",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center// Center the content within the Box
        ) {
            Column(
                horizontalAlignment = CenterHorizontally,
            ){
                Text(
                    text = name,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                    fontFamily = getCustomFontFamily("Inter", FontWeight.Bold, FontStyle.Normal)
                )

                Spacer(modifier = Modifier.height(7.dp))

                Text(
                    text = "#$userName",
                    color = Color(0xFFF1BE48),
                    fontWeight = FontWeight.Medium,
                    fontStyle = FontStyle.Italic,
                    fontFamily = getCustomFontFamily("Inter", FontWeight.Medium, FontStyle.Normal)
                )
            }
        }

    }
}

@Composable
fun FriendsButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
){
    Column(
        horizontalAlignment = CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ){
        Image(
            painter = painterResource(R.drawable.general_friends_icon),
            contentDescription = "Friends icon",
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.height(3.dp))

        Text(
            text = "Friends",
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            fontFamily = getCustomFontFamily("Inter", FontWeight.SemiBold, FontStyle.Normal)
        )
    }
}

@Composable
fun MessageButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
){
    Column(
        horizontalAlignment = CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ){
        Image(
            painter = painterResource(R.drawable.general_message_icon),
            contentDescription = "Message icon",
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.height(3.dp))

        Text(
            text = "Message",
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            fontFamily = getCustomFontFamily("Inter", FontWeight.SemiBold, FontStyle.Normal)
        )
    }
}

@Composable
fun ProfileScreen(
    name: String,
    userName: String,
    imageUrl: String,
    modifier: Modifier = Modifier
){
    val context = LocalContext.current

    Surface(
        color = Color(0xFFC8102E)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 80.dp),
            horizontalAlignment = CenterHorizontally
        ){
            MainProfileCard(name, userName, imageUrl)

            Spacer(modifier = Modifier.height(55.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 80.dp)
                    .padding(bottom = 5.dp)
            ){
                FriendsButton(onClick = {
                    val intent = Intent(context, MyFriends::class.java)
                    context.startActivity(intent)
                })
                MessageButton(onClick = {
                    val intent = Intent(context, MyMessages::class.java)
                    context.startActivity(intent)
                })
            }
        }
    }
}

@Preview
@Composable
fun ProfileCardPreview(){
    Surface(
//        modifier = Modifier.fillMaxWidth(),
        color = Color(0xFFC8102E)

    ) {
         MainProfileCard(
            name = "Cati",
            userName = "cattack",
            imageUrl = "https://thumbs.dreamstime.com/b/cute-cat-portrait-square-photo-beautiful-white-closeup-105311158.jpg"
        )
    }
}

@Preview
@Composable
fun FriendsButtonPreview(){
    Surface(
        color = Color(0xFFC8102E)
    ) {
       FriendsButton(onClick = {})
    }
}

@Preview
@Composable
fun MessageButtonPreview(){
    Surface(
        color = Color(0xFFC8102E)
    ) {
        MessageButton(onClick = {})
    }
}

@Preview
@Composable
fun ProfileScreenPreview(){
    ProfileScreen(
        name = "Cati",
        userName = "cattack",
        imageUrl = "https://thumbs.dreamstime.com/b/cute-cat-portrait-" +
                "square-photo-beautiful-white-closeup-105311158.jpg"
    )
}
