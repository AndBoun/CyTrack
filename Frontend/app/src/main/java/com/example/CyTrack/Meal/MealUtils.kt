package com.example.CyTrack.Meal
import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONException

import com.example.CyTrack.Utilities.NetworkUtils // Connection Imports
import com.example.CyTrack.Utilities.VolleySingleton

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
    }
}
