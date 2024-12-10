package com.example.CyTrack.Meal

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.CyTrack.R
import com.example.CyTrack.Utilities.StatusBarUtil
import com.example.CyTrack.Utilities.UrlHolder
import com.example.CyTrack.Utilities.User
import com.example.CyTrack.Workouts.DailyStatisticBox


class MyMeals : ComponentActivity(){
    //    @param ID        the unique identifier for the user
    //    * @param username  the username of the user
    //    * @param firstName the first name of the user
    //    * @param lastName  the last name of the user
    //    * @param age       the age of the user
    //    * @param gender    the gender of the user
    //    * @param streak    the current streak of the user
//    /**
//     * The user object representing the current user.
//     */
//    private lateinit var user: User

    /**
     * The user object representing the current user.
     */
    private var user = User(23, "Marco", "Darco", "Sparco", 23, "M", 23)

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
        val context = this
        //user = intent.getSerializableExtra("user") as User
        MealUtils.getListOfMeals(
            context,
            mealList,
            "${URL}/${user.id}/meal",
            "meals"
        )
        Log.d("Preview", "MealPage Created")

        setContent {
            Column(
                verticalArrangement = Arrangement.Top,
                modifier = Modifier.fillMaxSize()
            ) {
                MyMealsTopCard()
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
                    AddMealButton(
                        onClick = {
                            MealUtils.showAddMeal(user, "${URL}/${user.id}/meal", getTimeAsString(), getDateAsString(), context)
                        }
                    )
                }

                Spacer(modifier = Modifier.height(50.dp))
                MealsLazyList(MealData.MealSample, user, "${URL}/${user.id}/meal", getTimeAsString(), getDateAsString(), context)
            }

            StatusBarUtil.setStatusBarColor(this, R.color.CyRed)

        }
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
    fun getTimeAsString(): String {
        val timeFormat = java.time.LocalTime.now()
        return timeFormat.toString()
    }

    /**
     * @suppress
     */
    @Preview
    @Composable
    fun PreviewMyMealsTopCard() {
        MyMealsTopCard()
    }

    /**
     * @suppress
     */
    @Preview
    @Composable
    fun PreviewAddMealButton() {
        Surface {
            AddMealButton()
        }
    }

    /**
     * @suppress
     */
    @Preview
    @Composable
    fun PreviewMealCard() {
        com.example.CyTrack.Meal.MealCard(MealData.MealSample.get(0))
    }

    /**
     * @suppress
     */
    @Preview
    @Composable
    fun PreviewMyMealsPage() {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                verticalArrangement = Arrangement.Top,
                modifier = Modifier.fillMaxSize()
            ) {
                MyMealsTopCard()
                //getDateNutrients()

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
                    AddMealButton(
                        onClick = {
                            //MealUtils.showAddMeal(user, "${URL}/${user.id}/meal", getTimeAsString(), getDateAsString(), this)
                        }
                    )
                }

                Spacer(modifier = Modifier.height(50.dp))
                //MealsLazyList(MealData.MealSample)
                Log.d("Preview", "preview started")

            }
        }
    }
}
