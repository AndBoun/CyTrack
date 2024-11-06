package com.example.CyTrack.sample

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

// Animation Imports End

data class Message(val author: String, val body: String)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme { // Wraps our app in our custom theme
                Surface(modifier = Modifier.fillMaxSize()) {
                    Conversation(SampleData.conversationSample)
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
            MessageCard(Message("Android", "Jetpack Compose"))
        }
    }
}


// <!-- Adding Images --!>
@Composable
fun MessageCard(msg: Message) {
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
                Text(
                    text = msg.author,
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
                        text = msg.body,
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

// <!-- Adding Images END --!>

// <!-- LazyColumn Lists --!>
@Composable
fun Conversation(messages: List<Message>) {
    LazyColumn {
        items(messages) { message -> // the items() child takes a list as a param
            MessageCard(message) // Our message is then linked into our card and creaated
        }
    }
}

@Preview
@Composable
fun PreviewConversation() {
    AppTheme {
        Conversation(SampleData.conversationSample)
    }
}