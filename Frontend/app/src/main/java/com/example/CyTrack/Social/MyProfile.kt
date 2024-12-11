package com.example.CyTrack.Social

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.volley.toolbox.JsonObjectRequest
import com.example.CyTrack.R
import com.example.CyTrack.Utilities.ComposeUtils.Companion.getCustomFontFamily
import com.example.CyTrack.Utilities.StatusBarUtil
import com.example.CyTrack.Utilities.VolleySingleton
import com.android.volley.Request
import com.example.CyTrack.Social.Friends.Friend
import com.example.CyTrack.Social.Friends.ListProfileCard
import com.example.CyTrack.Social.Friends.MyFriends
import com.example.CyTrack.Social.Messaging.MyMessages
import com.example.CyTrack.Badges.BadgesActivity
import com.example.CyTrack.Utilities.NetworkUtils
import com.example.CyTrack.Utilities.UrlHolder
import com.example.CyTrack.Utilities.User
import org.json.JSONException
import org.json.JSONObject

/**
 * Activity to display the user's profile.
 */
class MyProfile : ComponentActivity() {

    /**
     * The user whose profile is being displayed.
     */
    private lateinit var user: User

    /**
     * A list of friend requests for the user.
     */
    private var friendRequests: MutableList<Friend> = mutableListOf()

    /**
     * The base URL for network requests.
     */
    private val URL: String = UrlHolder.URL

    /**
     * Called when the activity is starting. This is where most initialization should go.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        user = intent.getSerializableExtra("user") as User
        setContent {
            friendRequests = remember { mutableStateListOf() }
            getFriendRequests()

            val tempUrl = "${URL}/user/1/profileImage"

            Column {
                ProfileScreen(user.firstName, user.username, tempUrl,
                    onClickMyFriends = {
                        switchToMyFriends()
                    },
                    onClickMyMessages = {
                        switchToMyMessages()
                    },
                    onClickMyBadges = {
                        switchToMyBadges()
                    }
                )

                Spacer(modifier = Modifier.height(20.dp))

                FriendRequestCardLazyList(friendRequests, onAccept = {
                    acceptFriendRequest(it)
                }, onDecline = {
                    declineFriendRequest(it)
                })
            }
        }

        StatusBarUtil.setStatusBarColor(this, R.color.CyRed)
    }

    /**
     * Switches to the MyFriends activity.
     */
    private fun switchToMyFriends() {
        val intent = Intent(this, MyFriends::class.java).apply {
            putExtra("user", user)
        }
        startActivity(intent)
    }

    /**
     * Switches to the MyMessages activity.
     */
    private fun switchToMyMessages() {
        val intent = Intent(this, MyMessages::class.java).apply {
            putExtra("user", user)
        }
        startActivity(intent)
    }

    /**
     * Switches to the BadgesActivity.
     */
    private fun switchToMyBadges() {
        val intent = Intent(this, BadgesActivity::class.java).apply {
            putExtra("user", user)
        }
        startActivity(intent)
    }

    /**
     * Retrieves the list of friend requests for the user.
     */
    private fun getFriendRequests() {
        val getURL = "${URL}/${user.id}/request/incoming"
        SocialUtils.getListOfFriends(this, friendRequests, getURL, "friendRequests",
            onComplete = {
                friendRequests.sortBy { it.firstName }
            }
        )
    }

    /**
     * Accepts a friend request.
     * @param friend The friend request to accept.
     */
    private fun acceptFriendRequest(friend: Friend) {
        val acceptURL = "${URL}/${user.id}/request"
        val inputs = JSONObject().apply {
            put("friendRequestID", friend.friendID)
        }

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.PUT, acceptURL, inputs,
            { response ->
                try {
                    val message = response.getString("message")
                    friendRequests.remove(friend)
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { error ->
                Toast.makeText(this, NetworkUtils.errorResponse(error), Toast.LENGTH_SHORT).show()
            }
        )
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    /**
     * Declines a friend request.
     * @param friend The friend request to decline.
     */
    private fun declineFriendRequest(friend: Friend) {
        val declineURL = "${URL}/${user.id}/request/${friend.friendID}"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.DELETE, declineURL, null,
            { response ->
                try {
                    val message = response.getString("message")
                    friendRequests.remove(friend)
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { error ->
                Toast.makeText(this, NetworkUtils.errorResponse(error), Toast.LENGTH_SHORT).show()
            }
        )
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

}


/**
 * Composable function to display the main profile card.
 *
 * @param name The name of the user.
 * @param userName The username of the user.
 * @param imageUrl The URL of the user's profile image.
 * @param modifier The modifier to be applied to the layout.
 */
@Composable
fun MainProfileCard(
    name: String,
    userName: String,
    imageUrl: String = "",
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.5f), // Take up half the screen width
            contentAlignment = Alignment.Center // Center the content within the Box
        ) {
            ProfileImage(
                imageUrl,
                modifier = Modifier
                    .size(120.dp)
                    .border(10.dp, Color.Black, CircleShape)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center // Center the content within the Box
        ) {
            Column(
                horizontalAlignment = CenterHorizontally,
            ) {
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

/**
 * Composable function to display the Friends button.
 *
 * @param onClick The callback to be invoked when the button is clicked.
 * @param modifier The modifier to be applied to the layout.
 */
@Composable
fun FriendsButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = CenterHorizontally,
        modifier = modifier.clickable(onClick = onClick)
            .height(50.dp)
    ) {
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

/**
 * Composable function to display the Message button.
 *
 * @param onClick The callback to be invoked when the button is clicked.
 * @param modifier The modifier to be applied to the layout.
 */
@Composable
fun MessageButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = CenterHorizontally,
        modifier = modifier.clickable(onClick = onClick)
            .height(50.dp)
    ) {
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

/**
 * Composable function to display the Badges button.
 *
 * @param onClick The callback to be invoked when the button is clicked.
 * @param modifier The modifier to be applied to the layout.
 */
@Composable
fun BadgesButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = CenterHorizontally,
        modifier = modifier.clickable(onClick = onClick)
            .height(50.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.badge_button),
            contentDescription = "Badge icon",
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.height(3.dp))

        Text(
            text = "Badges",
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            fontFamily = getCustomFontFamily("Inter", FontWeight.SemiBold, FontStyle.Normal)
        )
    }
}

/**
 * Composable function to display the profile screen.
 *
 * @param name The name of the user.
 * @param userName The username of the user.
 * @param imageUrl The URL of the user's profile image.
 * @param modifier The modifier to be applied to the layout.
 * @param onClickMyFriends The callback to be invoked when the Friends button is clicked.
 * @param onClickMyMessages The callback to be invoked when the Message button is clicked.
 * @param onClickMyBadges The callback to be invoked when the Badges button is clicked.
 */
@Composable
private fun ProfileScreen(
    name: String,
    userName: String,
    imageUrl: String = "",
    modifier: Modifier = Modifier,
    onClickMyFriends: () -> Unit = {},
    onClickMyMessages: () -> Unit = {},
    onClickMyBadges: () -> Unit = {}
) {
    val context = LocalContext.current

    Surface(
        color = Color(0xFFC8102E),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp),
            horizontalAlignment = CenterHorizontally
        ) {
            IconButton(
                onClick = {
                    (context as Activity).finish()
                },
                modifier = Modifier.align(Alignment.Start)
            ) {
                Image(
                    painter = painterResource(R.drawable.general_back_arrow_button),
                    contentDescription = "Back arrow",
                    modifier = Modifier.size(24.dp)
                )
            }

            MainProfileCard(name, userName, imageUrl)

            Spacer(modifier = Modifier.height(55.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 60.dp)
                    .padding(bottom = 5.dp)
            ) {
                FriendsButton(onClick = {
                    onClickMyFriends()
                })
                MessageButton(onClick = {
                    onClickMyMessages()
                })
                BadgesButton(onClick = {
                    onClickMyBadges()
                })
            }
        }
    }
}

/**
 * Composable function to display a friend request card.
 *
 * @param name The name of the friend.
 * @param userName The username of the friend.
 * @param imageUrl The URL of the friend's profile image.
 * @param onClickAdd The callback to be invoked when the Accept button is clicked.
 * @param onClickDecline The callback to be invoked when the Decline button is clicked.
 * @param modifier The modifier to be applied to the layout.
 */
@Composable
fun FriendRequestCard(
    name: String,
    userName: String,
    imageUrl: String = "",
    onClickAdd: () -> Unit = {},
    onClickDecline: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box {
        val context = LocalContext.current

        ListProfileCard(name, userName, imageUrl, modifier)

        Row(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(10.dp)
                .padding(end = 30.dp)
        ) {
            Button(
                onClick = onClickAdd,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(
                        context.resources.getColor(
                            R.color.CyRed
                        )
                    )
                ),
                shape = RoundedCornerShape(6.dp),
                border = BorderStroke(1.dp, Color(0xFFF1BE48)),
                modifier = Modifier
                    .height(25.dp)
                    .width(60.dp),
//                    .offset(x = (-50).dp),
                contentPadding = PaddingValues(0.dp) //remove padding so text fits

            ) {
                Text(
                    text = "Accept",
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

            Spacer(modifier = Modifier.width(10.dp))

            Button(
                onClick = onClickDecline,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                shape = RoundedCornerShape(6.dp),
                border = BorderStroke(1.dp, Color(0xFFF1BE48)),
                modifier = Modifier
                    .height(25.dp)
                    .width(60.dp),
//                    .offset(x = (-50).dp),
                contentPadding = PaddingValues(0.dp) //remove padding so text fits

            ) {
                Text(
                    text = "Decline",
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
}

/**
 * Composable function to display a list of friend request cards.
 *
 * @param friendRequests The list of friend requests.
 * @param modifier The modifier to be applied to the layout.
 * @param onAccept The callback to be invoked when a friend request is accepted.
 * @param onDecline The callback to be invoked when a friend request is declined.
 */
@Composable
private fun FriendRequestCardLazyList(
    friendRequests: List<Friend>,
    modifier: Modifier = Modifier,
    onAccept: (Friend) -> Unit,
    onDecline: (Friend) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
    ) {
        for (friend in friendRequests) {
            FriendRequestCard(friend.firstName, friend.username, "", {
                onAccept(friend)
            }, {
                onDecline(friend)
            })
            Spacer(modifier = modifier.height(10.dp))
        }
    }
}

/**
 * @suppress
 */
@Preview
@Composable
private fun FriendRequestCardPreview() {
    Surface {
        FriendRequestCard(
            name = "Cati",
            userName = "cattack",
        )
    }
}

/**
 * @suppress
 */
@Preview
@Composable
private fun ProfileCardPreview() {
    Surface(
//        modifier = Modifier.fillMaxWidth(),
        color = Color(0xFFC8102E)

    ) {
        MainProfileCard(
            name = "Cati",
            userName = "cattack",
//            imageUrl = "https://thumbs.dreamstime.com/b/cute-cat-portrait-square-photo-beautiful-white-closeup-105311158.jpg"
        )
    }
}

/**
 * @suppress
 */
@Preview
@Composable
private fun FriendsButtonPreview() {
    Surface(
        color = Color(0xFFC8102E)
    ) {
        FriendsButton(onClick = {})
    }
}

/**
 * @suppress
 */
@Preview
@Composable
private fun MessageButtonPreview() {
    Surface(
        color = Color(0xFFC8102E)
    ) {
        MessageButton(onClick = {})
    }
}

/**
 * @suppress
 */
@Preview
@Composable
private fun BadgeButtonPreview() {
    Surface(
        color = Color(0xFFC8102E)
    ) {
        BadgesButton(onClick = {})
    }
}

/**
 * @suppress
 */
@Preview
@Composable
private fun ProfileScreenPreview() {
    val list = remember { mutableStateListOf<User>() }
    list.add(User(1, "Doe", "John", "Doe", 20, "M", 2))
    list.add(User(2, "Doe", "Jane", "Doe", 20, "F", 2))
    list.add(User(3, "Doe", "John", "Doe", 20, "M", 2))
    list.add(User(4, "Doe", "Jane", "Doe", 20, "F", 2))
    list.add(User(5, "Doe", "John", "Doe", 20, "M", 2))
    Surface {
        Column {
            ProfileScreen(
                name = "Cati",
                userName = "cattack",
            )

            Spacer(modifier = Modifier.height(20.dp))

//            FriendRequestCardLazyList(list, onAccept = {}, onDecline = {})
        }
    }
}