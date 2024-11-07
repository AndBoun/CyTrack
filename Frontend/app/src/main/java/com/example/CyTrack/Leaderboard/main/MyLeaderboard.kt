package com.example.CyTrack.Leaderboard.main

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable // Import For Compose
import androidx.compose.ui.tooling.preview.Preview // Import for Previewing Compose
import com.example.CyTrack.R // R equals Resource Files

// <!-- Creating a Layout --!>
// UI elements are hierarchical. Elements contain other elements.
// Uses Column
// A Hierarchy is built as comp functions call other comp functions.

import androidx.compose.foundation.layout.Column // A function arranging elements vertically
import androidx.compose.foundation.layout.Row // A function arranging elements horizontally

// <!-- Adding Images --!>
import androidx.compose.foundation.Image // Lays out a Format to display an image with modifiers
import androidx.compose.foundation.border
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

// Custom Theme Testing
import com.example.compose.AppTheme // Function for determining Theme
import androidx.compose.material3.MaterialTheme
import android.content.res.Configuration
import android.provider.ContactsContract.Profile
// Custom Themes

// Creating Lists
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
// Creating Lists End

// Animation Imports
import androidx.compose.foundation.clickable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.sp
import com.example.CyTrack.Social.SocialUtils.Companion.messageUserScreen
import com.example.CyTrack.Utilities.ComposeUtils.Companion.getCustomFontFamily
import com.example.CyTrack.Utilities.User
// Animation Imports End

/**
 * A list of friend requests for the user.
 */
private var AllUsers: MutableList<User> = mutableListOf()

private val URL = "temp"

class LeaderboardActivity : ComponentActivity(
) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LeaderboardUtils.getListOfUsers(
                this,
                AllUsers,
                URL,
                "users"  //TODO: VERIFY ARRAY NAME
            )
            AppTheme { // Wraps our app in our custom theme
                Surface(modifier = Modifier.fillMaxSize()) {
                    com.example.CyTrack.Leaderboard.main.LeaderboardScreen(AllUsers)
                }
            }
        }
    }
}

@Preview(name = "Light Mode")
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)

@Preview
@Composable
fun PreviewMessageCard() { // Wraps our app in our custom theme
    AppTheme { // Allows our composable to inherit styles as defined in our app's theme
        Surface {
            LBCard(User(1, "Ventrixq", "Ethan", "Cabelin", 32, "M", 23))
        }

    }
}


// <!-- Adding Images --!>
@Composable
fun LBCard(user: User) {
    // Add padding around our message
    AppTheme{
        Row(modifier = Modifier.padding(all = 8.dp)) {
            Image(
                painter = painterResource(R.drawable.general_generic_avatar),
                contentDescription = "Contact profile picture",
                modifier = Modifier
                    // Set image size to 40 dp
                    .size(40.dp)
                    // Clip image to be shaped as a circle
                    .clip(CircleShape)
                    // An easy border implementation
                    .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
            )

            // Add a horizontal space between the image and the column
            Spacer(modifier = Modifier.width(8.dp))

            // We keep track if the message is expanded or not in this
            // variable
            var isExpanded by remember { mutableStateOf(false) } // This keeps track of local memory
            // Tracks changes to value passed to "mutableStateOf"
            val surfaceColor by animateColorAsState(
                if (isExpanded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
            )

            // We toggle the isExpanded variable when we click on this Column
            Column(modifier = Modifier.clickable { isExpanded = !isExpanded }) {
                Text( // Main TextBody
                    text = user.username,
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.titleSmall
                )

                Spacer(modifier = Modifier.height(4.dp))

                Surface(
                    shape = MaterialTheme.shapes.medium,
                    shadowElevation = 1.dp,
                    // surfaceColor color will be changing gradually from primary to surface
                    color = surfaceColor,
                    // animateContentSize will change the Surface size gradually
                    modifier = Modifier.animateContentSize().padding(1.dp)
                ) {
                    Text(
                        text = user.streak.toString(),
                        modifier = Modifier.padding(all = 4.dp),
                        // If the message is expanded, we display all its content
                        // otherwise we only display the first line
                        maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                        style = MaterialTheme.typography.bodyMedium
                    )
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
                    text = "#$username",
                    fontSize = 11.sp,
                    fontStyle = FontStyle.Italic,
                    fontFamily = getCustomFontFamily("Inter", FontWeight.Normal, FontStyle.Italic)
                )
            }

            Row(
                horizontalArrangement = Arrangement.Center,
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
    user: List<User>,
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
                    contentDescription = "Friends icon",
                    modifier = Modifier.size(24.dp)
                )
            }

        }
    }
}

@Composable
fun LeaderboardScreen(
    UserList: List<User>,
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
        LeaderboardScreen(LeaderboardData.UserSample)
    }
}

@Preview
@Composable
fun PreviewConversation() {
    AppTheme {
        com.example.CyTrack.Leaderboard.main.LBHierarchy(LeaderboardData.UserSample)
    }
}

@Preview
@Composable
fun LBTopCardPreview() {
    LBTopCard()
}