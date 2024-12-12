package com.example.CyTrack.Leaderboard.TimeEntryBoard

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
import androidx.compose.foundation.shape.CircleShape
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
import com.example.CyTrack.Leaderboard.Websocket.WebSocketManagerLeaderboard
import com.example.CyTrack.R
import com.example.CyTrack.Social.ProfileImage
import com.example.CyTrack.Social.SocialUtils
import com.example.CyTrack.Utilities.ComposeUtils.Companion.getCustomFontFamily
import com.example.CyTrack.Utilities.UrlHolder
import com.example.CyTrack.Utilities.User
import com.example.CyTrack.Utilities.WebSocketListener
import com.example.compose.AppTheme
import org.java_websocket.handshake.ServerHandshake

// Animation Imports End

/**
 * The current user of the application.
 */
private lateinit var user: User // Current User

/**
 * A mutable list to store the leaderboard entries.
 */
private var timeboard: MutableList<TimeBoardEntry> = mutableListOf()

/**
 * The WebSocket URL for the leaderboard.
 */
//private val URL = UrlHolder.URL
private val URL = UrlHolder.wsURL
//private val URL = "${UrlHolder.wsURL}/leaderboard/2"

/**
 * Activity to display the leaderboard and handle WebSocket connections.
 */
class LeaderboardActivity : ComponentActivity(), WebSocketListener {

    /**
     * Called when the activity is starting. Initializes the content view and sets up WebSocket connection.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle). Otherwise it is null.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            user = intent.getSerializableExtra("user") as User
            timeboard = remember { mutableStateListOf() }

            // WEBSOCKET SECTION
            Log.d("WebSocketServiceUtil", "Connecting to ${URL}/leaderboard/${user.id}")
            WebSocketManagerLeaderboard.getInstance()
                .connectWebSocket("${URL}/leaderboard/${user.id}")
            WebSocketManagerLeaderboard.getInstance().setWebSocketListener(this@LeaderboardActivity)
            Log.d("List", "${timeboard}")
            // End WebSocket Section

            AppTheme { // Wraps our app in our custom theme
                Surface(modifier = Modifier.fillMaxSize()) {
                    LeaderboardScreen(timeboard)
                }
            }
        }
    }

    /**
     * Updates the leaderboard with the new entry.
     *
     * @param entry The new leaderboard entry as a JSON string.
     */
    private fun updateLeaderboard(entry: String) {
        try {
            Log.d("Task", "Starting Update")
            Log.d("Import", "${entry}")
            TimeboardUtils.getBoard(entry, timeboard)
            Log.d("List", "${timeboard}")
        } catch (e: Exception) {
            Log.d("ExceptionSendMessage:", e.message.toString())
            Toast.makeText(this, "Failed to Update", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Called when the WebSocket connection is opened.
     *
     * @param handshakedata The handshake data.
     */
    override fun onWebSocketOpen(handshakedata: ServerHandshake) {}

    /**
     * Called when a message is received from the WebSocket.
     *
     * @param leaderboardupdate The leaderboard update message as a JSON string.
     */
    override fun onWebSocketMessage(leaderboardupdate: String) {
        runOnUiThread {
            Log.d("Message Success", "Update Recieved")
            try {
                updateLeaderboard(leaderboardupdate)
            } catch (e: Exception) {
                Log.d("Exception", e.message.toString())
            }
        }
    }

    /**
     * Called when the WebSocket connection is closed.
     *
     * @param code The closure code.
     * @param reason The reason for closure.
     * @param remote Whether the connection was closed by the remote peer.
     */
    override fun onWebSocketClose(code: Int, reason: String?, remote: Boolean) {
        val closedBy = if (remote) "Server" else "Client"
        Toast.makeText(this, "Connection closed by $closedBy", Toast.LENGTH_SHORT).show()
    }

    /**
     * Called when an error occurs on the WebSocket connection.
     *
     * @param ex The exception that occurred.
     */
    override fun onWebSocketError(ex: Exception?) {}
}

/**
 * Composable function to display a basic profile card to be in a list.
 *
 * @param name The name of the user.
 * @param username The username of the user.
 * @param streak The streak of the user.
 * @param img The URL or resource identifier for the user's image.
 * @param modifier The modifier to be applied to the layout.
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
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = { /*TODO*/ })
//            .padding(horizontal = 32.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(10.dp)
        ) {
            ProfileImage(
                imageUrl = SocialUtils.getProfileImageUrl(username.toInt()),
                modifier = Modifier
                    .size(40.dp)
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

            }

            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Total Time: " + streak
                )
            }


        }
    }
}

// <!-- Adding Images END --!>
/**
 * Composable function to display a profile card.
 *
 * @param name The name of the user.
 * @param username The username of the user.
 * @param streak The streak of the user.
 * @param img The URL or resource identifier for the user's image.
 * @param onMessageClick The callback to be invoked when the message button is clicked.
 * @param modifier The modifier to be applied to the layout.
 */
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
/**
 * Composable function to display a list of leaderboard entries.
 *
 * @param userlist The list of TimeBoardEntry objects to display.
 * @param onMessageClick The callback to be invoked when a leaderboard entry is clicked.
 * @param modifier The modifier to be applied to the layout.
 */
@Composable
fun LBHierarchy(
    userlist: MutableList<TimeBoardEntry>,
    onMessageClick: (TimeBoardEntry) -> Unit = {},
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .padding(horizontal = 32.dp)
    ) {
        items(userlist) { user -> // the items() child takes a list as a param
            ProfileCard(user.name, user.id.toString(), user.time.toString(), "temp", {
                onMessageClick(user)
            })  // Our message is then linked into our card and created
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

// <!-- Top Card --!>
/**
 * Composable function to display the top card in the leaderboard screen.
 *
 * @param modifier The modifier to be applied to the layout.
 * @param onClickMyProfile The callback to be invoked when the profile button is clicked.
 */
@Composable
fun LBTopCard(
    modifier: Modifier = Modifier,
    onClickMyProfile: () -> Unit = {}
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
                painter = painterResource(id = R.drawable.leaderboard_header),
                contentDescription = "Leaderboard Header"
            )

            IconButton(
                onClick = onClickMyProfile
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

/**
 * Composable function to display the leaderboard screen.
 *
 * @param UserList The list of TimeBoardEntry objects to display.
 * @param modifier The modifier to be applied to the layout.
 */
@Composable
fun LeaderboardScreen(
    UserList: MutableList<TimeBoardEntry>,
    modifier: Modifier = Modifier
) {
    Column {
        LBTopCard()
        Spacer(modifier = Modifier.height(20.dp))
        LBHierarchy(UserList)
    }
}

/**
 * @suppress
 */
@Preview
@Composable
fun LBLazyListPreview() {
    Surface {
//        MyFriendsCardsLazyList(list)
        //LeaderboardScreen()
    }
}

/**
 * @suppress
 */
@Preview
@Composable
fun PreviewConversation() {
    AppTheme {
        //com.example.CyTrack.Leaderboard.main.LBHierarchy(data)
    }
}

/**
 * @suppress
 */
@Preview
@Composable
fun LBTopCardPreview() {
    LBTopCard()
}