package com.example.CyTrack.Leaderboard.main

// <!-- Creating a Layout --!>
// UI elements are hierarchical. Elements contain other elements.
// Uses Column
// A Hierarchy is built as comp functions call other comp functions.

// <!-- Adding Images --!>

// Custom Theme Testing
// Custom Themes

// Creating Lists
// Creating Lists End

// Animation Imports
import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.CyTrack.R
import com.example.CyTrack.Utilities.ComposeUtils.Companion.getCustomFontFamily
import com.example.CyTrack.Utilities.UrlHolder
import com.example.CyTrack.Utilities.User
import com.example.compose.AppTheme

// Animation Imports End

private var AllUsers: MutableList<User> = mutableListOf()
private val SampleUser = LeaderboardData.UserSample

private val data = AllUsers
private val URL = UrlHolder.URL

class LeaderboardActivity : ComponentActivity(
) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AllUsers = remember { mutableStateListOf() }

            LeaderboardUtils.getListOfUsers(this, AllUsers, "${URL}/user", "users")//TODO: VERIFY ARRAY NAME
            Log.d("Tag", "hi")
            Log.d("Tag", "${AllUsers}")

            AppTheme { // Wraps our app in our custom theme
                Surface(modifier = Modifier.fillMaxSize()) {
                    com.example.CyTrack.Leaderboard.main.LeaderboardScreen(data) // LeaderBoardData.UserSample
                }
            }
        }
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
fun LBProfileCard(
    name: String,
    username: String,
    streak: String,
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
                    text = username,
                    fontSize = 11.sp,
                    fontStyle = FontStyle.Italic,
                    fontFamily = getCustomFontFamily("Inter", FontWeight.Normal, FontStyle.Italic)
                )
            }

            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Streak: " + streak
                )
            }


        }
    }
}

// <!-- Adding Images END --!>
@Composable
fun ProfileCard(
    name: String,
    username: String,
    streak: String,
    img: String,
    onMessageClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row {
        Box {
            LBProfileCard(name, username, streak, img, modifier)
            Spacer(modifier = Modifier.height(10.dp))

        }

    }
}
// <!-- LazyColumn Lists --!>
@Composable
fun LBHierarchy(
    user: MutableList<User>,
    onMessageClick: (User) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val userSorted = user.sortedByDescending { it.streak }

    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .padding(horizontal = 32.dp)
    ){
        items(userSorted) { user -> // the items() child takes a list as a param
            ProfileCard(user.firstName, user.username, user.streak.toString(), "temp",{
                onMessageClick(user)
            })  // Our message is then linked into our card and created
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

// <!-- Top Card --!>
@Composable
fun LBTopCard(
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
                painter = painterResource(id = R.drawable.leaderboard_header),
                contentDescription = "Leaderboard Header"
            )

            IconButton(
                onClick = onAddFriendsButton,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.general_generic_avatar),
                    contentDescription = "Profile Button",
                    modifier = Modifier.size(24.dp)
                )
            }

        }
    }
}

@Composable
fun LeaderboardScreen(
    UserList: MutableList<User>,
    modifier: Modifier = Modifier
) {
    Column {
        LBTopCard()
        Spacer(modifier = Modifier.height(20.dp))
        LBHierarchy(UserList)
    }
}

@Preview
@Composable
fun LBLazyListPreview() {
    Surface {
//        MyFriendsCardsLazyList(list)
        LeaderboardScreen(data)
    }
}

@Preview
@Composable
fun PreviewConversation() {
    AppTheme {
        com.example.CyTrack.Leaderboard.main.LBHierarchy(data)
    }
}

@Preview
@Composable
fun LBTopCardPreview() {
    LBTopCard()
}