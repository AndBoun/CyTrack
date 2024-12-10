package com.example.CyTrack.Meal

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
import com.example.CyTrack.Workouts.AddWorkOutButton
import com.example.CyTrack.Workouts.DailyStatisticBox
import com.example.CyTrack.Workouts.MyWorkoutsTopCard
import com.example.CyTrack.Workouts.StartWorkoutButton
import com.example.CyTrack.Workouts.WorkoutObject
import com.example.CyTrack.Workouts.WorkoutUtils
import com.example.CyTrack.Workouts.WorkoutsLazyList

class MyMeals : ComponentActivity(){
    /**
     * The user object representing the current user.
     */
    private lateinit var user: User

    /**
     * A mutable list of workout objects.
     */
    private var mealList = mutableStateListOf<MealEntry>()

    /**
     * Base URL for meal calls
     */
    private val URL = "${UrlHolder.URL}"
    // ("/{userID}/meal") getAllMealsByUserID
    // ("/{mealId}/meal/{mealID}") Get Meal By Id
    // ("/{userID}/mealsByDate/{date}") getMealsByUserIDAndDate
    // (/{userID}/nutrients/{date}") getTotalNutrientsForDate
    // ("/{userID}/meal") createMeal
    // ("/{userID}/meal/{mealID}") deleteMeal
    // ("/{userID}/meal/{mealID}") updateMeal

    /**
     * Current day calorie intake
     */
    private var dailyCalories = mutableIntStateOf(0)

    /**
     * Current week calorie intake
     */
    private var weekCalories = mutableIntStateOf(0)

    /**
     * Nutrient Summary Intake
     */
    private var nutrientsum = NutrientSummary(0, 0, 0, getTimeAsString(), getDateAsString(), )

    /**
     * Nutrient sum - Calories
     */
    private var calories = nutrientsum.calories

    /**
     * Nutrient sum - Carbs
     */
    private var carbs = nutrientsum.carbs

    /**
     * Nutrient sum - Protein
     */
    private var protein = nutrientsum.protein

    /**
     * Nutrient Summary Time
     */
    private var ns_time = nutrientsum.time

    /**
     * Nutrient Summary Date
     */
    private var ns_date = nutrientsum.date

    /**
     * Called when the activity is starting. This is where most initialization should go.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        user = intent.getSerializableExtra("user") as User
        MealUtils.getListOfMeals(
            this,
            mealList,
            "${URL}/${user.id}/meal",
            "meals"
        )

        setContent {
            Column(
                verticalArrangement = Arrangement.Top,
                modifier = Modifier.fillMaxSize()
            ) {
                MyWorkoutsTopCard()
                getDateNutrients()

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    DailyStatisticBox(
                        displayText = ns_date + " Caloric Intake",
                        displayValue = calories,
                    )
                    DailyStatisticBox(
                        displayText = ns_date + "Protein Intake",
                        displayValue = protein,
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
     * Returns the current time as a string
     * @return The current time as a string.
     */
    private fun getTimeAsString(): String {
        val timeFormat = java.time.LocalTime.now()
        return timeFormat.toString()
    }

    /**
     * Fetches the total nutrients to date
     */
    private fun getDateNutrients() {
        val getURL = "${URL}/${user.id}/nutrients/${getDateAsString()}"
//        var calories = 0

        MealUtils.getDailyNutrients(this, getURL, getDateAsString(), getTimeAsString()) {
            nutrientsum = it
            Log.d("NutrientSummary", nutrientsum.toString())
        }
    }

}