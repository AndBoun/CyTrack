package com.example.CyTrack.Social.Friends

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.example.CyTrack.R
import com.example.CyTrack.Utilities.NetworkUtils
import com.example.CyTrack.Utilities.StatusBarUtil
import com.example.CyTrack.Utilities.UrlHolder
import com.example.CyTrack.Utilities.User
import com.example.CyTrack.Utilities.VolleySingleton
import org.json.JSONException
import org.json.JSONObject

/**
 * Activity to add friends.
 */
class AddFriends : ComponentActivity() {

    /**
     * The user whose profile is being displayed.
     */
    private lateinit var user: User

    /**
     * A list of users from search.
     */
    private var searchList: MutableList<Friend> = mutableListOf()

    /**
     * Base URL for friend requests.
     */
    private val URL: String = UrlHolder.URL

    /**
     * Called when the activity is starting.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            user = intent.getSerializableExtra("user") as User
            searchList = remember { mutableStateListOf() }

            Surface(
                color = Color(context.resources.getColor(R.color.CyRed)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.Bottom
                ) {
                    IconButton(
                        onClick = {
                            (context as ComponentActivity).finish()
                        },
                        modifier = Modifier.align(Alignment.Start)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back arrow",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    SearchBar(
                        searchList = mutableListOf(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 56.dp)
                            .align(Alignment.CenterHorizontally),
                        onClickSearch = {
                            addUser(it)
                        }
                    )
                }
            }

            StatusBarUtil.setStatusBarColor(this, R.color.CyRed)
        }
    }

    /**
     * Sends a friend request to the specified user.
     * @param username The username of the user to add as a friend.
     */
    private fun addUser(username: String) {
        val addURL = "${URL}/${user.id}/request"

        val inputs = JSONObject().apply {
            put("friendUsername", username)
        }

        Log.d("AddFriends", inputs.toString())

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,
            addURL,
            inputs,
            { response ->
                // Handle the response
                try {
                    val message = response.getString("message")
                    val data = response.getJSONObject("data")
                    val friendsId = data.getInt("friendRequestID")
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
 * @suppress
 */
@Preview
@Composable
fun AddFriendsTopCard(
    onClickSearch: (String) -> Unit = {}
) {
    val context = LocalContext.current

    Surface(
        color = Color(context.resources.getColor(R.color.CyRed)),
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Bottom
        ) {
            IconButton(
                onClick = {
                    (context as ComponentActivity).finish()
                },
                modifier = Modifier.align(Alignment.Start)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back arrow",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            SearchBar(
                searchList = mutableListOf(),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 56.dp)
                    .align(Alignment.CenterHorizontally),
                onClickSearch = onClickSearch
            )
        }
    }
}

/**
 * Composable function for the search bar.
 * @param searchList List of friends from the search.
 * @param modifier Modifier to be applied to the search bar.
 * @param onClickSearch Callback function to handle search button click.
 */
@Composable
fun SearchBar(
    searchList: MutableList<Friend>,
    modifier: Modifier = Modifier,
    onClickSearch: (String) -> Unit = {}
) {
    var message by remember { mutableStateOf("") } // Message state

    TextField(
        value = message,
        onValueChange = { message = it }, // Update the message value
        trailingIcon = {
            IconButton(
                onClick = {
                    onClickSearch(message)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                )
            }
        },
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedContainerColor = MaterialTheme.colorScheme.surface
        ),
        placeholder = {
            Text("Search for User")
        },
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
    )
}