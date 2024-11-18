package com.example.CyTrack.Workouts

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.example.CyTrack.Utilities.NetworkUtils
import com.example.CyTrack.Utilities.VolleySingleton

/**
 * Utility class for handling workout-related network operations.
 */
class WorkoutUtils {

    /**
     * Companion object to hold static methods for WorkoutUtils.
     */
    companion object {

        /**
         * Fetches the list of workouts from the given URL and adds them to the provided mutable list.
         *
         * @param context The activity context.
         * @param url The URL to fetch the workouts from.
         * @param mutableList The mutable list to add the fetched workouts to.
         */
        @JvmStatic
        fun getWorkouts(
            context: Activity,
            url: String,
            mutableList: MutableList<WorkoutObject>
        ) {
            Log.d("getWorkouts at WorkoutUtils", "getWorkouts called")

            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                { response ->
                    try {
                        val jsonArray = response.getJSONObject("data").getJSONArray("workouts")
                        Log.d("jsonarrlength_workoutUtils", jsonArray.length().toString())
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject = jsonArray.getJSONObject(i)
                            val workout = WorkoutObject(
                                jsonObject.getString("exerciseType"),
                                jsonObject.getString("duration"),
                                jsonObject.getString("calories"),
                                jsonObject.getString("date"),
                                jsonObject.getInt("workoutID")
                            )
                            mutableList.add(workout)
                            Log.d("Workouts", workout.toString())
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                },
                { error ->
                    error.printStackTrace()
                }
            )
            VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest)
        }

        /**
         * Fetches the total daily calories burned from the given URL and invokes the callback with the result.
         *
         * @param context The activity context.
         * @param url The URL to fetch the total daily calories burned from.
         * @param onComplete A callback function to be invoked with the total calories burned.
         */
        @JvmStatic
        fun getDailyCaloriesBurned(
            context: Activity,
            url: String,
            onComplete: (calories: Int) -> Unit = {}
        ) {
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET, url, null,
                { response ->
                    val calories = response.getJSONObject("data").getInt("totalCalories")
                    Log.d("Calories", calories.toString())
                    onComplete(calories)
                },
                { error ->
                    Log.d("MyWorkouts", "Error: $error")
                    Toast.makeText(context, NetworkUtils.errorResponse(error), Toast.LENGTH_SHORT)
                        .show()
                }
            )
            VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest)
        }


        /**
         * Fetches the total workout time from the given URL and invokes the callback with the result.
         *
         * @param context The activity context.
         * @param url The URL to fetch the total workout time from.
         * @param onComplete A callback function to be invoked with the total workout time.
         */
        @JvmStatic
        fun getTotalWorkoutTime(
            context: Activity,
            url: String,
            onComplete: (time: Int) -> Unit = {}
        ) {
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET, url, null,
                { response ->
                    val time = response.getJSONObject("data").getInt("totalWorkoutTime")
                    onComplete(time)
                },
                { error ->
                    Toast.makeText(context, NetworkUtils.errorResponse(error), Toast.LENGTH_SHORT)
                        .show()
                }
            )
            VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest)
        }


    }
}