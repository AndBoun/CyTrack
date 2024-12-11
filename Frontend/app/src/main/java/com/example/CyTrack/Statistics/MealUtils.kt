package com.example.CyTrack.Statistics
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.example.CyTrack.R
import com.example.CyTrack.Utilities.NetworkUtils
import com.example.CyTrack.Utilities.User
import com.example.CyTrack.Utilities.VolleySingleton
import com.example.CyTrack.Workouts.WorkoutObject
import org.json.JSONException
import org.json.JSONObject
import java.util.Objects

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
            Log.d("Get Meal List URL Checker", "${url}")
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET, url, null,
                { response ->
                    try {
                        MealList.clear()
                        val message = response.getString("message")
                        val data = response.getJSONObject("data").getJSONArray(arrName)
                        Log.d("Get Meal List URL Checker", "${url}")
                        Log.d("Get Meal Data Checker", data.toString())
                        for (i in 0 until data.length()) {
                            val meal = data.getJSONObject(i)
                            MealList.add(
                                MealEntry(
                                    meal.getLong("mealID").toInt(),
                                    meal.getString("mealName"),
                                    meal.getInt("calories"),
                                    meal.getInt("carbs"),
                                    meal.getInt("protein"),
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
