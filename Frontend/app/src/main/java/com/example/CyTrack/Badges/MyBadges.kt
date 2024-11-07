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
import android.content.Intent
import android.os.Bundle
import android.widget.Space
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
import com.example.CyTrack.Social.MyProfile
import com.example.CyTrack.Utilities.ComposeUtils.Companion.getCustomFontFamily
import com.example.CyTrack.Utilities.UrlHolder
import com.example.CyTrack.Utilities.User
import com.example.compose.AppTheme

// Animation Imports End
/**
 * The user whose profile is being displayed.
 */
private lateinit var user: User

private var AllBadges: MutableList<BadgeObject> = mutableListOf()
private val SampleUser = BadgeData.BadgeSample

private val data = SampleUser
private val URL = "${UrlHolder.URL}/badge/${user.id}/earned"

class BadgesActivity : ComponentActivity(
) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BadgeUtils.getListOfBadges(
                this,
                AllBadges,
                user,
                URL,
                "users"//TODO: VERIFY ARRAY NAME
            )

            AppTheme { // Wraps our app in our custom theme
                Column {
                    BGTopCard(
                        onClickMyProfile = {
                            switchToMyProfile()
                        }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    BGScreen(data)
                }
            }

        }

    }
    private fun switchToMyProfile() {
        val intent = Intent(this, MyProfile::class.java).apply {
            putExtra("user", user)
        }
        startActivity(intent)
    }

}

/**
 * Composable function to display a basic profile card to be in a list.
 *
 * @param name The name of the Badge
 * @param desc The description of the Badge
 * @param img The URL or resource identifier for the Badge
 */
@Composable
fun BGProfileCard(
    name: String,
    desc: String,
    img: String,
    modifier: Modifier = Modifier
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

@Composable
fun ProfileButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.clickable(onClick = onClick)
    ) {
        Image(
            painter = painterResource(R.drawable.general_back_arrow_button),
            contentDescription = "Friends icon",
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.height(3.dp))

    }

}

// <!-- Adding Images END --!>
@Composable
fun BGCard(
    name: String,
    desc: String,
    onMessageClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row {
        Box {
            BGProfileCard(name, desc, "temp", modifier)
            Spacer(modifier = Modifier.height(10.dp))
        }

    }
}
// <!-- LazyColumn Lists --!>
@Composable
fun BGHierarchy(
    BadgeList: List<BadgeObject>,
    onMessageClick: (BadgeObject) -> Unit = {},
    modifier: Modifier = Modifier
) {
    //val userSorted = Badge.sortedByDescending { it.streak }

    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .padding(horizontal = 32.dp)
    ){
        items(BadgeList) { Badge -> // the items() child takes a list as a param
            BGCard(Badge.name, Badge.desc, {
                onMessageClick(Badge)
            })  // Our message is then linked into our card and created
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

// <!-- Top Card --!>
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
            modifier = Modifier.fillMaxWidth()
                .offset(y = (-10).dp)
        ) {

            Row{
                Spacer(modifier = modifier.width(10.dp))
                Column {
                    ProfileButton(
                        onClick = { onClickMyProfile() }
                    )
                    Spacer(modifier = modifier.height(6.dp))
                }
            }

            Image(
                painter = painterResource(id = R.drawable.my_badges),
                contentDescription = "Badge Header"
            )

            Spacer(modifier = Modifier.width(20.dp))


        }
    }
}

@Composable
fun BGScreen(
    BGList: List<BadgeObject>,
    modifier: Modifier = Modifier
) {
    Column {
        BGHierarchy(BGList)
    }
}

@Preview
@Composable
fun BGLazyListPreview() {
    Surface {
        Column{
            BGTopCardPreview()
            Spacer(modifier = Modifier.height(20.dp))
            BGScreen(data)
        }
    }
}

@Preview
@Composable
fun PreviewConversation() {
    AppTheme {
        com.example.CyTrack.Badges.BGHierarchy(data)
    }
}

@Preview
@Composable
fun BGTopCardPreview() {
    BGTopCard()
}
 //*/