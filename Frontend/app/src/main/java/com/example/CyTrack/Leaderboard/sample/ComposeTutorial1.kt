package com.example.CyTrack.Leaderboard.sample

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
import androidx.compose.foundation.layout.Box // A function arranging elements on a stack

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

// Custom Theme Testing
import com.example.compose.AppTheme
data class Message(val author: String, val body: String)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MessageCard(Message("Android", "Jetpack Compose"))
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewMessageCard() {
    AppTheme { // Allows our composable to inherit styles as defined in our app's theme
        Surface {
            MessageCard(
                msg = Message("Lexi", "Take a look at Jetpack Compose, it's great!")
            )
        }
    }
}


// <!-- Adding Images --!>
@Composable
fun MessageCard(msg: Message) {
    // Add padding around our message
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

        Column { // A function arranging elements vertically
            Text {

            }
            // Add a vertical space between the author and message texts
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = msg.body)
        }
    }
}
// <!-- Adding Images END --!>