package com.example.CyTrack.Social.Friends

import android.app.Activity
import android.os.Bundle
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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.CyTrack.Social.SocialUtils
import com.example.CyTrack.Social.SocialUtils.Companion.messageUserScreen
import com.example.CyTrack.Social.SocialUtils.Companion.switchToAddFriends
import com.example.CyTrack.Social.SocialUtils.Companion.switchToFriendProfile
import com.example.CyTrack.Utilities.ComposeUtils.Companion.getCustomFontFamily
import com.example.CyTrack.Utilities.User
import com.example.CyTrack.Utilities.StatusBarUtil
import com.example.CyTrack.Utilities.UrlHolder

class MyFriends : ComponentActivity() {

    /**
     * The user whose profile is being displayed.
     */
    private lateinit var user: User

    /**
     * A list of friend requests for the user.
     */
    private var myFriends: MutableList<Friend> = mutableListOf()

    private val URL = UrlHolder.URL

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            user = intent.getSerializableExtra("user") as User


            myFriends = remember { mutableStateListOf() }

            getFriends()

            Column {
                MyFriendsTopCard(onAddFriendsButton = {
                    switchToAddFriends(this@MyFriends, user)
                })
                Spacer(modifier = Modifier.height(20.dp))
                MyFriendsCardsLazyList(myFriends,
                    onMessageClick = {
                        messageUserScreen(user, it, this@MyFriends)
                    },
                    onProfileClick = {
                        switchToFriendProfile(this@MyFriends, user, it)
                    },
                    onDeleteClick = {
                        deleteFriend(it)
                    }
                )
            }
        }

        StatusBarUtil.setStatusBarColor(this, R.color.CyRed)
    }


    private fun getFriends() {
        val getURL = "${URL}/${user.id}/friends"

        SocialUtils.getListOfFriends(this, myFriends, getURL, "friends", onComplete = {
            myFriends.sortBy { it.firstName }
        })
    }

    private fun deleteFriend(friend: Friend) {
        val delURL = "${URL}/${user.id}/friends/${friend.friendID}"

        SocialUtils.deleteFriend(this, delURL, onComplete = {
            myFriends.remove(friend)
        })
    }

}


@Composable
fun ListProfileCard(
    name: String,
    username: String,
    img: String,
    modifier: Modifier = Modifier,
    onProfileClick: () -> Unit = {},
    dropDownButton: @Composable () -> Unit = {}
) {
    // Composable function to display a card with user information
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, Color.Black),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(10.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.general_generic_avatar),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(40.dp)
                    .clickable(onClick = onProfileClick)
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
                dropDownButton()
            }

        }
    }
}

@Composable
fun MoreOptionsButtonMyFriends(onDeleteFriend: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        IconButton(
            onClick = { expanded = true },
            modifier = Modifier.size(24.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.general_more_options_horizontal),
                contentDescription = "More Options",
                colorFilter = ColorFilter.tint(Color.Black)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Delete Friend") },
                onClick = {
                    expanded = false
                    onDeleteFriend()
                }
            )
        }
    }
}

@Composable
fun FriendsListProfileCard(
    name: String,
    username: String,
    img: String,
    onMessageClick: () -> Unit,
    onProfileClick: () -> Unit = {},
    dropDownButton: @Composable () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box {
        ListProfileCard(
            name, username, img, modifier,
            onProfileClick = onProfileClick,
            dropDownButton = dropDownButton,
        )


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
                fontFamily = getCustomFontFamily(
                    "Inter",
                    FontWeight.SemiBold,
                    FontStyle.Normal
                ),
                color = Color.Black,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}


@Composable
fun MyFriendsCardsLazyList(
    friendsList: List<Friend>,
    onMessageClick: (Friend) -> Unit = {},
    onProfileClick: (Friend) -> Unit = {},
    onDeleteClick: (Friend) -> Unit = {},
    modifier: Modifier = Modifier
) {
    // LazyColumn to display a list of friends
    Column(
        modifier = modifier
            .fillMaxHeight()
            .padding(horizontal = 32.dp)
    ) {
        for (friend in friendsList) {
            FriendsListProfileCard(friend.firstName, friend.username, "temp", {
                onMessageClick(friend)
            }, {
                onProfileClick(friend)
            }, {
                MoreOptionsButtonMyFriends {
                    onDeleteClick(friend)
                }
            }
            )
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
            modifier = Modifier
                .fillMaxWidth()
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




@Preview
@Composable
fun FriendsListProfileCardPreview() {
    FriendsListProfileCard("John Doe", "johndoe", "temp", {},
        dropDownButton = {
            MoreOptionsButtonMyFriends {
                // Delete friend
            }
        }
    )
}

@Preview
@Composable
fun MyFriendsTopCardPreview() {
    MyFriendsTopCard()
}



