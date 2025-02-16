package com.example.CyTrack.Workouts

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.SegmentedButtonDefaults.borderStroke
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.example.CyTrack.R
import com.example.CyTrack.Utilities.ComposeUtils.Companion.getCustomFontFamily
import com.example.CyTrack.Utilities.NetworkUtils
import com.example.CyTrack.Utilities.UrlHolder
import com.example.CyTrack.Utilities.User
import com.example.CyTrack.Utilities.VolleySingleton
import com.example.compose.AppTheme
import org.json.JSONObject

/**
 * Activity to start and end a workout.
 */
class StartWorkout : ComponentActivity() {

    /**
     * The user object representing the current user.
     */
    private lateinit var user: User

    /**
     * The base URL for workout-related API endpoints.
     */
    private val URL = "${UrlHolder.URL}/workout"

    /**
     * The ID of the current workout.
     */
    private var workoutID: Int = 0

    /**
     * Called when the activity is starting.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            user = intent.getSerializableExtra("user") as User

            AppTheme {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    CreateWorkoutForm(
                        onCreateWorkout = { workout ->
                            startWorkout(workout)
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    EndWorkoutButton(
                        onClick = {
                            // Handle ending workout
                            endWorkout()
                        }
                    )
                }
            }
        }
    }

    /**
     * Starts a workout by sending a POST request to the server.
     *
     * @param workout The workout object containing details of the workout to be started.
     */
    private fun startWorkout(workout: WorkoutObject) {
        val postURL = "${URL}/${user.id}/createAndStart"

        // Handle starting workout
        val inputs = JSONObject().apply {
            put("exerciseType", workout.exerciseType)
            put("date", workout.date)
            put("calories", workout.caloriesBurned)
        }

        val jsonObject = JsonObjectRequest(
            Request.Method.POST,
            postURL,
            inputs,
            { response ->
                // Handle response
                workout.workoutID = response.getJSONObject("data").getInt("workoutID")
                workoutID = workout.workoutID
                Toast.makeText(this, "Workout started", Toast.LENGTH_LONG)
                    .show()
            },
            { error ->
                // Handle error
                Toast.makeText(this, NetworkUtils.errorResponse(error), Toast.LENGTH_LONG)
                    .show()
            })
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObject)
    }

    /**
     * Ends the current workout by sending a POST request to the server.
     */
    private fun endWorkout() {
        val postURL = "${URL}/${user.id}/workout/${workoutID}/end"

        val jsonObject = JsonObjectRequest(
            Request.Method.POST,
            postURL,
            null,
            { response ->
                // Handle response
                Toast.makeText(this, response.getString("message"), Toast.LENGTH_LONG)
                    .show()
            },
            { error ->
                // Handle error
                Toast.makeText(this, NetworkUtils.errorResponse(error), Toast.LENGTH_LONG)
                    .show()
            })
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObject)
    }

}

/**
 * A composable function that creates a button to end the workout.
 *
 * @param onClick The callback to be invoked when the button is clicked.
 * @param modifier The modifier to be applied to the button.
 */
@Composable
fun EndWorkoutButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(15.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        ),
        border = borderStroke(
            width = 2.dp,
            color = Color(LocalContext.current.resources.getColor(R.color.CyYellow))
        ),
        modifier = modifier
            .width(160.dp)
            .height(60.dp)
    ) {
        Text(
            text = "End Workout",
            color = Color.Black,
            fontFamily = getCustomFontFamily(
                "Inter",
                FontWeight.Bold,
                FontStyle.Normal
            ),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}

/**
 * A composable function that creates a form for starting a workout.
 *
 * @param onCreateWorkout The callback to be invoked when the workout is created.
 * @param modifier The modifier to be applied to the form.
 */
@Composable
fun CreateWorkoutForm(
    onCreateWorkout: (WorkoutObject) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var exerciseType by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }
    var caloriesBurned by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        TextField(
            value = exerciseType,
            onValueChange = { exerciseType = it },
            label = { Text("Exercise Type") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = duration,
            onValueChange = { duration = it },
            label = { Text("Duration (minutes)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = caloriesBurned,
            onValueChange = { caloriesBurned = it },
            label = { Text("Calories Burned") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = date,
            onValueChange = { date = it },
            label = { Text("Date") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                val workout = WorkoutObject(
                    exerciseType,
                    duration,
                    caloriesBurned,
                    date,
                    0 // Placeholder for workout ID
                )
                onCreateWorkout(workout)
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Start Workout")
        }
    }
}

/**
 * @suppress
 */
@Preview
@Composable
fun CreateWorkoutFormPreview() {
    Surface {
        CreateWorkoutForm(
            onCreateWorkout = {}
        )
    }
}

/**
 * @suppress
 */
@Preview
@Composable
fun EndWorkoutButtonPreview() {
    Surface {
        EndWorkoutButton(
            onClick = {}
        )
    }
}