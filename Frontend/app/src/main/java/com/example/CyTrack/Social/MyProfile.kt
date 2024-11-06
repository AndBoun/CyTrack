package com.example.CyTrack.Social

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.example.CyTrack.Utilities.User
import com.example.CyTrack.Utilities.StatusBarUtil
import com.example.CyTrack.Utilities.VolleySingleton
import com.android.volley.Request
import com.example.CyTrack.Utilities.NetworkUtils
import com.example.CyTrack.Utilities.UrlHolder
import org.json.JSONException
import org.json.JSONObject

class MyProfile : ComponentActivity() {

    /**
     * The user whose profile is being displayed.
     */
    private lateinit var user: User

    /**
     * A list of friend requests for the user.
     */
    private var friendRequests: MutableList<IncomingRequest> = mutableListOf()

    private val URL: String = "${UrlHolder.URL}"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            user = intent.getSerializableExtra("user") as User
            friendRequests = remember { mutableStateListOf() }
            getFriendRequests()



            Column {
                if (user != null) {
                    ProfileScreen(user.firstName, user.username, "temp",
                        onClickMyFriends = {
                            switchToMyFriends()
                        },
                        onClickMyMessages = {
                            switchToMyMessages()
                        }
                    )
                }

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

    data class IncomingRequest(
        val firstName: String,
        val userName: String,
        val id: Int
    )

    private fun switchToMyFriends() {
        val intent = Intent(this, MyFriends::class.java).apply {
            putExtra("user", user)
        }
        startActivity(intent)
    }

    private fun switchToMyMessages() {
        val intent = Intent(this, MyMessages::class.java).apply {
            putExtra("user", user)
        }
        startActivity(intent)
    }

    private fun getFriendRequests() {
        val getURL = "${URL}/${user.id}/request/incoming"
//        SocialUtils.getListOfUsers(this, friendRequests, getURL, "friendRequests")

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, getURL, null,
            { response ->
                try {
                    val message = response.getString("message")
                    val data = response.getJSONObject("data").getJSONArray("friendRequests")

                    for (i in 0 until data.length()) {
                        val tempUser = data.getJSONObject(i)
                        friendRequests.add(
                            IncomingRequest(
                                tempUser.getString("firstname"),
                                tempUser.getString("username"),
                                tempUser.getInt("id")
                            )
                        )
                    }
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


    private fun acceptFriendRequest(friend: IncomingRequest) {
        val acceptURL = "${URL}/${user.id}/request"
        val inputs = JSONObject().apply {
            put("friendRequestID", friend.id)
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

    private fun declineFriendRequest(friend: IncomingRequest) {
        val declineURL = "${URL}/${user.id}/request/${friend.id}"

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


@Composable
fun MainProfileCard(
    name: String,
    userName: String,
    imageUrl: String,
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

@Composable
fun FriendsButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = CenterHorizontally,
        modifier = modifier.clickable(onClick = onClick)
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

@Composable
fun MessageButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = CenterHorizontally,
        modifier = modifier.clickable(onClick = onClick)
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

@Composable
fun ProfileScreen(
    name: String,
    userName: String,
    imageUrl: String,
    modifier: Modifier = Modifier,
    onClickMyFriends: () -> Unit = {},
    onClickMyMessages: () -> Unit = {}
) {
    val context = LocalContext.current

    Surface(
        color = Color(0xFFC8102E),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 80.dp),
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
                    .padding(horizontal = 80.dp)
                    .padding(bottom = 5.dp)
            ) {
                FriendsButton(onClick = {
                    onClickMyFriends()
                })
                MessageButton(onClick = {
                    onClickMyMessages()
                })
            }
        }
    }
}

@Composable
fun FriendRequestCard(
    name: String,
    userName: String,
    imageUrl: String,
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

@Composable
fun FriendRequestCardLazyList(
    friendRequests: List<MyProfile.IncomingRequest>,
    modifier: Modifier = Modifier,
    onAccept: (MyProfile.IncomingRequest) -> Unit,
    onDecline: (MyProfile.IncomingRequest) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
    ) {
        for (friend in friendRequests) {
            FriendRequestCard(friend.firstName, friend.userName, "temp", {
                onAccept(friend)
            }, {
                onDecline(friend)
            })
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Preview
@Composable
fun FriendRequestCardPreview() {
    Surface {
        FriendRequestCard(
            name = "Cati",
            userName = "cattack",
            imageUrl = "https://thumbs.dreamstime.com/b/cute-cat-portrait-square-photo-beautiful-white-closeup-105311158.jpg"
        )
    }
}

@Preview
@Composable
fun ProfileCardPreview() {
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
fun FriendsButtonPreview() {
    Surface(
        color = Color(0xFFC8102E)
    ) {
        FriendsButton(onClick = {})
    }
}

@Preview
@Composable
fun MessageButtonPreview() {
    Surface(
        color = Color(0xFFC8102E)
    ) {
        MessageButton(onClick = {})
    }
}

@Preview
@Composable
fun ProfileScreenPreview() {
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
                imageUrl = "https://thumbs.dreamstime.com/b/cute-cat-portrait-" +
                        "square-photo-beautiful-white-closeup-105311158.jpg"
            )

            Spacer(modifier = Modifier.height(20.dp))

//            FriendRequestCardLazyList(list, onAccept = {}, onDecline = {})
        }
    }
}
