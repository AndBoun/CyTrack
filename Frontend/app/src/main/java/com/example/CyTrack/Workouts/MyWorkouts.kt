package com.example.CyTrack.Workouts

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.CyTrack.R
import com.example.CyTrack.Utilities.ComposeUtils.Companion.getCustomFontFamily
import com.example.CyTrack.Utilities.StatusBarUtil
import com.example.CyTrack.Utilities.UrlHolder
import com.example.CyTrack.Utilities.User


/**
 * Activity class for displaying the user's workouts.
 */
class MyWorkouts : ComponentActivity() {

    /**
     * The user object representing the current user.
     */
    private lateinit var user: User

    /**
     * A mutable list of workout objects.
     */
    private var workoutList = mutableStateListOf<WorkoutObject>()

    /**
     * The base URL for workout-related API calls.
     */
    private val URL = "${UrlHolder.URL}/workout"

    /**
     * The total calories burned, stored as a mutable state.
     */
    private var caloriesBurned = mutableIntStateOf(0)

    /**
     * The total workout time, stored as a mutable state.
     */
    private var workoutTime = mutableIntStateOf(0)

    /**
     * Called when the activity is starting. This is where most initialization should go.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        user = intent.getSerializableExtra("user") as User
        WorkoutUtils.getWorkouts(
            this,
            "${URL}/${user.id}/workoutByDate/${getDateAsString()}",
            workoutList
        )

        setContent {

            Column(
                verticalArrangement = Arrangement.Top,
                modifier = Modifier.fillMaxSize()
            ) {
                MyWorkoutsTopCard()
                getDailyCaloriesBurned()
                getDailyWorkoutTime()

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    DailyStatisticBox(
                        displayText = "Calories Burned",
                        displayValue = caloriesBurned.value,
                    )
                    DailyStatisticBox(
                        displayText = "Workout Time",
                        displayValue = workoutTime.value,
                    )
                }

                Spacer(modifier = Modifier.height(50.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    StartWorkoutButton(
                        onClick = {
                            switchToStartWorkouts()
                        }
                    )
                    AddWorkOutButton(
                        onClick = {
                            switchToAddWorkouts()
                        }
                    )
                }

                Spacer(modifier = Modifier.height(50.dp))

                WorkoutsLazyList(workoutList)

            }

            StatusBarUtil.setStatusBarColor(this, R.color.CyRed)

        }
    }

    /**
     * Returns the current date as a string in the format MM-dd-yyyy.
     * @return The current date as a string.
     */
    private fun getDateAsString(): String {
        val dateFormat = java.text.SimpleDateFormat("MM-dd-yyyy", java.util.Locale.getDefault())
        return dateFormat.format(java.util.Date())
    }

    /**
     * Fetches the total calories burned for the current date and updates the state.
     */
    private fun getDailyCaloriesBurned() {
        val getURL = "${URL}/${user.id}/totalCalories/${getDateAsString()}"
//        var calories = 0

        WorkoutUtils.getDailyCaloriesBurned(this, getURL) {
            caloriesBurned.intValue = it
            Log.d("Workout calories", caloriesBurned.toString())
        }

    }

    /**
     * Fetches the total workout time for the current date and updates the state.
     */
    private fun getDailyWorkoutTime() {
        val getURL = "${URL}/${user.id}/totalWorkoutTime/${getDateAsString()}"

        WorkoutUtils.getTotalWorkoutTime(this, getURL) {
            workoutTime.intValue = it
        }

    }

    /**
     * Switches to the Add Workouts activity.
     */
    private fun switchToAddWorkouts() {
        val intent = Intent(this, WorkoutActivity::class.java).apply {
            putExtra("user", user)
        }
        startActivity(intent)
    }

    /**
     * Switches to the Start Workouts activity.
     */
    private fun switchToStartWorkouts() {
        val intent = Intent(this, StartWorkout::class.java).apply {
            putExtra("user", user)
        }
        startActivity(intent)
    }

}

/**
 * Composable function that displays the top card of the My Workouts screen.
 */
@Composable
fun MyWorkoutsTopCard() {
    val context = LocalContext.current

    Surface(
        color = Color(context.resources.getColor(R.color.CyRed)),
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
    ) {
        Box {
            IconButton(
                onClick = {
                    (context as Activity).finish()
                },
                modifier = Modifier.align(Alignment.BottomStart)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.general_back_arrow_button),
                    contentDescription = "Back arrow",
                    modifier = Modifier.size(24.dp)
                )
            }

            Image(
                painter = painterResource(R.drawable.workouts_title),
                contentDescription = "Messages",
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = (-8).dp)
            )
        }
    }
}

/**
 * @suppress
 */
@Preview
@Composable
fun PreviewMyWorkoutsTopCard() {
    MyWorkoutsTopCard()
}

/**
 * Composable function that displays a button to add a workout.
 * @param onClick The action to perform when the button is clicked.
 * @param modifier The modifier to be applied to the button.
 */
@Composable
fun AddWorkOutButton(
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(15.dp),
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        border = BorderStroke(2.dp, Color(0xFFF1BE48)),
        modifier = modifier
            .width(160.dp)
            .height(60.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Workout",
                tint = Color(LocalContext.current.resources.getColor(R.color.CyRed)),
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(15.dp))

            Text(
                text = "Add Workout",
                color = Color.Black,
                fontFamily = getCustomFontFamily(
                    "Inter",
                    FontWeight.Bold,
                    FontStyle.Normal
                ),
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )
        }
    }
}

/**
 * @suppress
 */
@Preview
@Composable
fun PreviewAddWorkOutButton() {
    Surface {
        AddWorkOutButton()
    }
}

/**
 * Composable function that displays a button to start a workout.
 * @param onClick The action to perform when the button is clicked.
 * @param modifier The modifier to be applied to the button.
 */
@Composable
fun StartWorkoutButton(
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(15.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(
                LocalContext.current.resources.getColor(
                    R.color.CyRed
                )
            )
        ),
        modifier = modifier
            .width(160.dp)
            .height(60.dp)
    ) {
        Text(
            text = "Start Workout",
            color = Color.White,
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
 * Composable function that displays a box with daily statistics.
 * @param displayText The text to display.
 * @param displayValue The value to display.
 * @param modifier The modifier to be applied to the box.
 */
@Composable
fun DailyStatisticBox(
    displayText: String,
    displayValue: Int,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Surface(
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(
            width = 2.dp,
            brush = Brush.linearGradient(
                colors = listOf(
                    Color(context.resources.getColor(R.color.CyRed)),
                    Color(context.resources.getColor(R.color.CyYellow))
                )
            )
        ),
        modifier = modifier.size(160.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .padding(10.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = displayValue.toString(),
                color = Color(context.resources.getColor(R.color.CyRed)),
                fontFamily = getCustomFontFamily(
                    "Inter",
                    FontWeight.Bold,
                    FontStyle.Normal
                ),
                fontWeight = FontWeight.Bold,
                fontSize = 48.sp,
                modifier = Modifier.padding(top = 20.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = displayText,
                color = Color.Black,
                fontFamily = getCustomFontFamily(
                    "Inter",
                    FontWeight.Bold,
                    FontStyle.Normal
                ),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = modifier.padding(bottom = 4.dp)
            )
        }
    }
}

/**
 * Composable function that displays a workout card.
 * @param workout A WorkoutObject representing the workout details to be displayed.
 */
@Composable
fun WorkoutCard(workout: WorkoutObject) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .height(52.dp)
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(

            ) {
                Text(
                    text = workout.exerciseType,
                    color = Color.Black,
                    fontFamily = getCustomFontFamily(
                        "Inter",
                        FontWeight.Bold,
                        FontStyle.Normal
                    ),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(start = 20.dp)
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = workout.date,
                    color = Color.Black,
                    fontFamily = getCustomFontFamily(
                        "Inter",
                        FontWeight.Normal,
                        FontStyle.Normal
                    ),
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 20.dp)
                )
            }

            Text(
                text = workout.duration,
                color = Color.Black,
                fontFamily = getCustomFontFamily(
                    "Inter",
                    FontWeight.Bold,
                    FontStyle.Normal
                ),
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                modifier = Modifier.padding(end = 20.dp)
            )

            Text(
                text = workout.caloriesBurned,
                color = Color.Black,
                fontFamily = getCustomFontFamily(
                    "Inter",
                    FontWeight.Bold,
                    FontStyle.Normal
                ),
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                modifier = Modifier.padding(end = 20.dp)
            )

        }
    }
}

/**
 * Composable function that displays a list of workouts using a LazyColumn.
 * @param workoutList A list of WorkoutObject representing the workouts to be displayed.
 */
@Composable
fun WorkoutsLazyList(workoutList: List<WorkoutObject>) {
    LazyColumn {
        items(workoutList.size) {
            WorkoutCard(workoutList[it])
            HorizontalDivider()
        }
    }
}

/**
 * @suppress
 */
@Preview
@Composable
fun PreviewWorkoutCard() {
    val workout = WorkoutObject("Running", "30", "200", "12-12-2021", 1)
    WorkoutCard(workout)
}

/**
 * @suppress
 */
@Preview
@Composable
fun PreviewMyWorkoutsPage() {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxSize()

        ) {

            MyWorkoutsTopCard()

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                DailyStatisticBox(
                    displayText = "Calories Burned",
                    displayValue = 100,
                )
                DailyStatisticBox(
                    displayText = "Workout Time",
                    displayValue = 120,
                )
            }


            Spacer(modifier = Modifier.height(50.dp))


            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                StartWorkoutButton()
                AddWorkOutButton()
            }

            Spacer(modifier = Modifier.height(40.dp))

            WorkoutsLazyList(
                workoutList = listOf(
                    WorkoutObject("Running", "30", "200", "12-12-2021", 1),
                    WorkoutObject("Cycling", "45", "300", "12-12-2021", 2),
                    WorkoutObject("Swimming", "60", "400", "12-12-2021", 3),
                    WorkoutObject("Running", "30", "200", "12-12-2021", 4),
                    WorkoutObject("Cycling", "45", "300", "12-12-2021", 5),
                    WorkoutObject("Swimming", "60", "400", "12-12-2021", 6),
                    WorkoutObject("Running", "30", "200", "12-12-2021", 7),
                    WorkoutObject("Cycling", "45", "300", "12-12-2021", 8),
                    WorkoutObject("Swimming", "60", "400", "12-12-2021", 9),
                    WorkoutObject("Running", "30", "200", "12-12-2021", 10),
                    WorkoutObject("Cycling", "45", "300", "12-12-2021", 11),
                    WorkoutObject("Swimming", "60", "400", "12-12-2021", 12),
                    WorkoutObject("Running", "30", "200", "12-12-2021", 13),
                    WorkoutObject("Cycling", "45", "300", "12-12-2021", 14),
                    WorkoutObject("Swimming", "60", "400", "12-12-2021", 15),
                    WorkoutObject("Running", "30", "200", "12-12-2021", 16),
                    WorkoutObject("Cycling", "45", "300", "12-12-2021", 17),
                    WorkoutObject("Swimming", "60", "400", "12-12-2021", 18),
                    WorkoutObject("Running", "30", "200", "12-12-2021", 19),
                    WorkoutObject("Cycling", "45", "300", "12-12-2021", 20)
                )
            )

        }
    }
}