package com.example.CyTrack.Social

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.IBinder.DeathRecipient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.CyTrack.R
import com.example.CyTrack.Social.SocialUtils.Companion.messageUserScreen
import com.example.CyTrack.Utilities.ComposeUtils.Companion.getCustomFontFamily
import com.example.CyTrack.Utilities.User
import com.example.CyTrack.Utilities.StatusBarUtil

class MyFriends : ComponentActivity() {

    /**
     * The user whose profile is being displayed.
     */
    private lateinit var user: User

    /**
     * A list of friend requests for the user.
     */
    private var myFriends: MutableList<User> = mutableListOf()

    private val URL = "temp"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val user = intent.getSerializableExtra("user") as User
            if (user != null) {

            }

            SocialUtils.getListOfUsers(
                this,
                myFriends,
                "temp",
                "friends"
            )

            Column {
                MyFriendsTopCard(onAddFriendsButton = {
                    switchToAddFriends()
                })
                Spacer(modifier = Modifier.height(20.dp))
                MyFriendsCardsLazyList(myFriends, onMessageClick = {
                    messageUserScreen(user, it, Activity())
                })
            }
        }

        StatusBarUtil.setStatusBarColor(this, R.color.CyRed)
    }

    private fun switchToAddFriends() {
//        val intent = Intent(this, AddFriends::class.java).apply {
//            putExtra("user", user)
//        }
//        startActivity(intent)
    }



}

/**
 * Composable function to display a basic profile card to be in a list.
 *
 * @param name The name of the user.
 * @param username The username of the user.
 * @param img The URL or resource identifier for the user\`s image.
 */
@Composable
fun ListProfileCard(
    name: String,
    username: String,
    img: String,
    modifier: Modifier = Modifier
) {
    // Composable function to display a card with user information
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, Color.Black),
        modifier = modifier.fillMaxWidth()
            .clickable(onClick = { /*TODO*/ })
//            .padding(horizontal = 32.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(10.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.general_generic_avatar),
                contentDescription = "Profile Picture",
                modifier = Modifier.size(40.dp),
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = name,
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = getCustomFontFamily("Inter", FontWeight.Bold, FontStyle.Normal)
                )
                Spacer(modifier = Modifier.padding(2.dp))
                Text(
                    text = "#$username",
                    fontSize = 11.sp,
                    fontStyle = FontStyle.Italic,
                    fontFamily = getCustomFontFamily("Inter", FontWeight.Normal, FontStyle.Italic)
                )
            }

            // Right align more options button
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.general_more_options_horizontal),
                    contentDescription = "More Options",
                    modifier = Modifier.size(24.dp),
                    colorFilter = ColorFilter.tint(Color.Black),
                    // TODO ADD ONCLICK
                )
            }

        }
    }
}

@Composable
fun FriendsListProfileCard(
    name: String,
    username: String,
    img: String,
    onMessageClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box {
        ListProfileCard(name, username, img, modifier)


        Button(
            onClick = onMessageClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            shape = RoundedCornerShape(6.dp),
            border = BorderStroke(1.dp, Color(0xFFF1BE48)),
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .matchParentSize()
                .height(25.dp)
                .width(100.dp)
                .offset(x = (-50).dp),
            contentPadding = PaddingValues(0.dp) //remove padding so text fits

        ) {
            Text(
                text = "Message",
                fontFamily = getCustomFontFamily("Inter", FontWeight.SemiBold, FontStyle.Normal),
                color = Color.Black,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

@Composable
fun MyFriendsCardsLazyList(
    friendsList: List<User>,
    onMessageClick: (User) -> Unit = {},
    modifier: Modifier = Modifier
) {
    // LazyColumn to display a list of friends
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(horizontal = 32.dp)
    ) {
        for (friend in friendsList) {
            FriendsListProfileCard(friend.firstName, friend.username, "temp", {
                onMessageClick(friend)
            })
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}



@Composable
fun MyFriendsTopCard(
    modifier: Modifier = Modifier,
    onAddFriendsButton: () -> Unit = {}
) {
    val context = LocalContext.current

    Surface(
        color = Color(context.resources.getColor(R.color.CyRed)),
        border = BorderStroke(1.dp, Color.Black),
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
    ) {
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
                .offset(y = (-10).dp)
        ) {

            IconButton(
                onClick = {
                    (context as Activity).finish()
                },
            ) {
                Image(
                    painter = painterResource(id = R.drawable.general_back_arrow_button),
                    contentDescription = "Back arrow",
                    modifier = Modifier.size(24.dp)
                )
            }


            Image(
                painter = painterResource(id = R.drawable.social_friends_header),
                contentDescription = "Friends text",
            )

            IconButton(
                onClick = onAddFriendsButton,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.social_add_person),
                    contentDescription = "Friends icon",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun MyFriendsScreen(
    friendsList: List<User>,
    modifier: Modifier = Modifier
) {
    Column {
        MyFriendsTopCard()
        Spacer(modifier = Modifier.height(20.dp))
        MyFriendsCardsLazyList(friendsList)
    }
}


//@Preview
@Composable
fun ListProfileCardPreview() {
    ListProfileCard("John Doe", "johndoe", "temp")
}

//@Preview
@Composable
fun FriendsListProfileCardPreview() {
    FriendsListProfileCard("John Doe", "johndoe", "temp", {})
}

@Preview
@Composable
fun MyFriendsCardsLazyListPreview() {
    val list = ArrayList<User>()
    list.add(User(1, "Doe", "John", "Doe", 20, "M", 2))
    list.add(User(2, "Doe", "Jane", "Doe", 20, "F", 2))
    list.add(User(3, "Doe", "John", "Doe", 20, "M", 2))
    list.add(User(4, "Doe", "Jane", "Doe", 20, "F", 2))
    list.add(User(5, "Doe", "John", "Doe", 20, "M", 2))
    Surface {
//        MyFriendsCardsLazyList(list)
        MyFriendsScreen(list)
    }
}

@Preview
@Composable
fun MyFriendsTopCardPreview() {
    MyFriendsTopCard()
}



