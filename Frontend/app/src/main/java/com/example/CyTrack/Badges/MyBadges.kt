package com.example.CyTrack.Badges

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
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import com.example.CyTrack.R
import com.example.CyTrack.Social.ProfileImage
import com.example.CyTrack.Social.SocialUtils
import com.example.CyTrack.Utilities.ComposeUtils.Companion.getCustomFontFamily
import com.example.CyTrack.Utilities.UrlHolder
import com.example.CyTrack.Utilities.User
import com.example.compose.AppTheme

// Animation Imports End

/**
 * The user whose profile is being displayed.
 */
private lateinit var user: User

/**
 * List of all badges.
 */
private var AllBadges: MutableList<BadgeObject> = mutableListOf()

/**
 * Sample user data for testing.
 */
private val SampleUser = BadgeData.BadgeSample

/**
 * Data for the sample user.
 */
private val data = SampleUser

/**
 * Base URL for fetching badge data.
 */
private val URL = UrlHolder.URL
//private val URL = "${UrlHolder.URL}/badge/2/earned"

/**
 * Activity to display badges.
 */
class BadgesActivity : ComponentActivity() {
    /**
     * Called when the activity is starting. This is where most initialization should go.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down
     *                           then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            user = intent.getSerializableExtra("user") as User

            AllBadges = remember { mutableStateListOf() }
            BadgeUtils.getListOfBadges(
                this,
                AllBadges,
                "${URL}/badge/${user.id.toString()}/earned",
                "badges" // TODO: VERIFY ARRAY NAME
            )

            AppTheme { // Wraps our app in our custom theme
                Column {
                    BGTopCard()
                    Spacer(modifier = Modifier.height(10.dp))
                    BGScreen(AllBadges, user)
                }
            }
        }
    }
}

/**
 * Composable function to display a basic profile card to be in a list.
 *
 * @param name The name of the Badge
 * @param desc The description of the Badge
 * @param img The URL or resource identifier for the Badge
 * @param modifier Modifier for the composable
 */
@Composable
fun BGProfileCard(
    name: String,
    desc: String,
    img: String,
    modifier: Modifier = Modifier,
    user: Int,
) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, Color.Black),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(10.dp)
        ) {
            ProfileImage(
                imageUrl = SocialUtils.getProfileImageUrl(user),
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
            }

            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = desc
                )
            }
        }
    }
}

/**
 * Composable function to display a card with a badge profile.
 *
 * @param name The name of the Badge
 * @param desc The description of the Badge
 * @param onMessageClick Callback when the card is clicked
 * @param modifier Modifier for the composable
 */
@Composable
fun BGCard(
    name: String,
    desc: String,
    user: User = User(0, "Proto", "Proto", "Proto", 32, "M", 32),
    onMessageClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row {
        Box {
            BGProfileCard(name,desc, "temp", modifier, user.id)
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

/**
 * Composable function to display a list of badges in a hierarchical structure.
 *
 * @param BadgeList List of BadgeObject to display
 * @param onMessageClick Callback when a badge is clicked
 * @param modifier Modifier for the composable
 */
@Composable
fun BGHierarchy(
    BadgeList: List<BadgeObject>,
    user: User = User(0, "Proto", "Proto", "Proto", 32, "M", 32),
    onMessageClick: (BadgeObject) -> Unit = {},
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .padding(horizontal = 32.dp)
    ) {
        items(BadgeList) { Badge -> // the items() child takes a list as a param
            BGCard(Badge.name, Badge.desc, user, {
                onMessageClick(Badge)
            })  // Our message is then linked into our card and created
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

/**
 * Composable function to display the top card with a back button and header image.
 *
 * @param modifier Modifier for the composable
 * @param onClickMyProfile Callback when the profile is clicked
 */
@Composable
fun BGTopCard(
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
                painter = painterResource(id = R.drawable.my_badges),
                contentDescription = "Badge Header"
            )

            Spacer(modifier = Modifier.width(20.dp))
        }
    }
}

/**
 * Composable function to display the main screen with a list of badges.
 *
 * @param BGList List of BadgeObject to display
 * @param modifier Modifier for the composable
 */
@Composable
fun BGScreen(
    BGList: List<BadgeObject>,
    user: User = User(0, "Proto", "Proto", "Proto", 32, "M", 32),
    modifier: Modifier = Modifier
) {
    Column {
        BGHierarchy(BGList, user)
    }
}

/**
 * @suppress
 */
@Preview
@Composable
fun BGLazyListPreview() {
    Surface {
        Column {
            BGTopCardPreview()
            Spacer(modifier = Modifier.height(20.dp))
            BGScreen(data)
        }
    }
}

/**
 * @suppress
 */
@Preview
@Composable
fun PreviewConversation() {
    AppTheme {
        com.example.CyTrack.Badges.BGHierarchy(data)
    }
}

/**
 * @suppress
 */
@Preview
@Composable
fun BGTopCardPreview() {
    BGTopCard()
}