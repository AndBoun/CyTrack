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
import androidx.compose.runtime.remember
import com.example.compose.AppTheme
import androidx.compose.foundation.lazy.LazyColumn


class MyMeals : ComponentActivity(){
    /**
     * The user object representing the current user.
     */
    private lateinit var user: User

    /**
     * The user object representing a sample user.
     */
    //private var user = User(23, "Marco", "Darco", "Sparco", 23, "M", 23)

    /**
     * A mutable list of workout objects.
     */
    private var mealList = mutableStateListOf<MealEntry>()

    private var nutrients = mutableStateListOf<Int>()
    /**
     * Base URL for meal calls
     */
    //private val URL = UrlHolder.URL
    // ("meal/{userID}/meal") getAllMealsByUserID
    // ("meal/{mealId}/meal/{mealID}") Get Meal By Id
    // ("meal/{userID}/mealsByDate/{date}") getMealsByUserIDAndDate
    // (meal/{userID}/nutrients/{date}") getTotalNutrientsForDate
    // ("meal/{userID}/meal") createMeal
    // ("meal/{userID}/meal/{mealID}") deleteMeal
    // ("meal/{userID}/meal/{mealID}") updateMeal
    private val URLBase = "${UrlHolder.URL}/meal"

    /**
     * Current day calorie intake
     */
    private var dailyCalories = mutableIntStateOf(0)

    /**
     * Current week calorie intake
     */
    private var dailyProtein = mutableIntStateOf(0)



    /**
     * Called when the activity is starting. This is where most initialization should go.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val context = this
        setContent {

            user = intent.getSerializableExtra("user") as User
            val URL = "${URLBase}/${user.id.toString()}"

            mealList = remember { mutableStateListOf() }
            nutrients = remember { mutableStateListOf() }

            MealUtils.getListOfMeals(
                context,
                mealList,
                "${URL}/meal",
                "meals"
            )

            Log.d("Initial Meal List", mealList.toString())

            MealUtils.getDailyNutrients(
                context,
                URL,
                getTimeAsString(),
                getDateAsString(),
            )
            Log.d("Preview Nutrients", nutrients.toString())
            Log.d("Preview", "MealPage Created")
            AppTheme {
                Column(
                    verticalArrangement = Arrangement.Top,
                    modifier = Modifier.fillMaxSize()
                ) {
                    MyMealsTopCard()

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    ) {
                        DailyMealStatisticBox(
                            displayText = getDateAsString() + " Caloric Intake",
                            displayValue = dailyCalories.intValue
                        )
                        DailyMealStatisticBox(
                            displayText = getDateAsString() + " Protein Intake",
                            displayValue = dailyProtein.intValue
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
                                Log.d("Main Meal URL Checker", "${URL}")
                                MealUtils.showAddMeal(user, "${URL}", mealList, getTimeAsString(), getDateAsString(), context)
                                MealUtils.getListOfMeals(context, mealList, "${URL}/meal", "meals")
                                MealUtils.getDailyNutrients(context, URL, getTimeAsString(), getDateAsString()) {
                                    Log.d("Nutrients Update", "${it.calories} ${it.protein}")
                                    dailyCalories.intValue = it.calories
                                    dailyProtein.intValue = it.protein
                                }
                                Log.d("Nutrients Update", "${dailyCalories.intValue} ${dailyProtein.intValue}")
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(50.dp))

                    LazyColumn {
                        items(mealList.size) {
                            MealCard(
                                mealList[it],
                                onClick = {
                                    MealUtils.showModifyMeal(user, mealList, "${URL}", getTimeAsString(), getDateAsString(), mealList[it].id, context)
                                    MealUtils.getListOfMeals(context, mealList, "${URL}/meal", "meals")
                                    MealUtils.getDailyNutrients(context, URL, getTimeAsString(), getDateAsString()) {
                                        dailyCalories.intValue = it.calories
                                        dailyProtein.intValue = it.protein
                                    }
                                }
                            )
                        }
                    }

                }
                Log.d("URL Check", "${URL}")
                StatusBarUtil.setStatusBarColor(this, R.color.CyRed)
            }

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
                        displayText = " Caloric Intake",
                        displayValue = 123,
                    )
                    DailyStatisticBox(
                        displayText = "Protein Intake",
                        displayValue = 523,
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
