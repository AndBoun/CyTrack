package com.example.CyTrack.Statistics

import android.app.Activity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.CyTrack.R
import com.example.CyTrack.Utilities.ComposeUtils.Companion.getCustomFontFamily

/**
 * Composable function that displays the top card of the My Workouts screen.
 */
@Composable
fun MyStatsTopCard() {
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
                painter = painterResource(R.drawable.statistics),
                contentDescription = "Messages",
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = (-8).dp)
            )
        }
    }
}


/**
 * Composable function that displays a button to add a workout.
 * @param onClick The action to perform when the button is clicked.
 * @param modifier The modifier to be applied to the button.
 */
@Composable
fun AddMealButton(
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
                contentDescription = "Add Meal",
                tint = Color(LocalContext.current.resources.getColor(R.color.CyRed)),
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(15.dp))

            Text(
                text = "Add Meal",
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
 * Composable function that displays a workout card.
 * @param workout A WorkoutObject representing the workout details to be displayed.
 */
@Composable
fun MealCalsGraphCard(
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
            .fillMaxWidth()
            .height(60.dp)
            .padding(horizontal = 20.dp)
    )
    {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Top,
        ) {
            Text(
                text = "",
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

            Text(
                text = "Meal Calorie Statistics",
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

            Text(
                text = "",
                color = Color.Black,
                fontFamily = getCustomFontFamily(
                    "Inter",
                    FontWeight.Bold,
                    FontStyle.Normal
                ),
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 20.dp)
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}

/**
 * Composable function that displays a workout card.
 * @param workout A WorkoutObject representing the workout details to be displayed.
 */
@Composable
fun MealCalsBurnedGraphCard(
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
            .fillMaxWidth()
            .height(60.dp)
            .padding(horizontal = 20.dp)
    )
    {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Top,
        ) {
            Text(
                text = "",
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

            Text(
                text = "Workout Calories Burned Statistics",
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

            Text(
                text = "",
                color = Color.Black,
                fontFamily = getCustomFontFamily(
                    "Inter",
                    FontWeight.Bold,
                    FontStyle.Normal
                ),
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 20.dp)
            )

        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}
