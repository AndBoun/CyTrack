package com.example.CyTrack.Meal
import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONException

import com.example.CyTrack.Utilities.NetworkUtils // Connection Imports
import com.example.CyTrack.Utilities.VolleySingleton
import com.example.CyTrack.Workouts.WorkoutUtils

class MealUtils {

    /**
     * Companion object to hold static methods and properties.
     */
    companion object {
        /**
         * Fetches a list of badges from the specified URL and updates the provided BadgeList.
         *
         * @param context The activity context.
         * @param BadgeList The list to be updated with fetched badges.
         * @param url The URL to fetch badges from.
         * @param arrName The name of the JSON array containing badge data.
         */
        @JvmStatic
        fun getListOfMeals(
            context: Activity,
            MealList: MutableList<MealEntry>,
            url: String,
            arrName: String,
        ) {
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET, url, null,
                { response ->
                    try {
                        val message = response.getString("message")
                        val data = response.getJSONObject("data").getJSONArray(arrName)

                        for (i in 0 until data.length()) {
                            val meal = data.getJSONObject(i)

                            MealList.add(
                                MealEntry(
                                    meal.getString("mealName"),
                                    meal.getInt("calories"),
                                    meal.getInt("protein"),
                                    meal.getInt("carbs"),
                                    meal.getString("time"),
                                    meal.getString("date")
                                )
                            )

                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                { error ->
                    Toast.makeText(context, NetworkUtils.errorResponse(error), Toast.LENGTH_LONG)
                        .show()
                }

            )
            VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest)
        }

        /**
         * Fetches a nutrient summary from the given URL and invokes the callback with the result.
         *
         * @param context The activity context.
         * @param url The URL to fetch the total daily calories burned from.
         * @param onComplete A callback function to be invoked with the nutrients
         */
        @JvmStatic
        fun getDailyNutrients(
            context: Activity,
            url: String,
            time: String,
            date: String,
            onComplete: (Nutrients: NutrientSummary) -> Unit = {}
        ) {
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET, url, null,
                { response ->
                    val summary = response.getJSONObject("data")
                    val totalCalories = summary.getInt("totalCalories")
                    val totalCarbs = summary.getInt("totalCarbs")
                    val totalProtein = summary.getInt("totalProtein")
                    val Nutrients = NutrientSummary(totalCalories, totalCarbs, totalProtein, time, date)
                    onComplete(Nutrients)
                },
                { error ->
                    Log.d("MyWorkouts", "Error: $error")
                    Toast.makeText(context, NetworkUtils.errorResponse(error), Toast.LENGTH_SHORT)
                        .show()
                }
            )
            VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest)
        }
    }
}
