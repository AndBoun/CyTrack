package com.example.CyTrack.Workouts

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.example.CyTrack.Utilities.NetworkUtils
import com.example.CyTrack.Utilities.VolleySingleton

class WorkoutUtils {

    companion object{

        @JvmStatic
        fun getWorkouts(
            context: Activity,
            url: String,
            mutableList: MutableList<WorkoutObject>
        ){
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

        @JvmStatic
        fun getDailyCaloriesBurned(
            context: Activity,
            url: String,
            onComplete: ( calories: Int) -> Unit = {}
        ){
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET, url, null,
                { response ->
                   val calories = response.getJSONObject("data").getInt("totalCalories")
                    Log.d("Calories", calories.toString())
                    onComplete(calories)
                },
                { error ->
                    Log.d("MyWorkouts", "Error: $error")
                    Toast.makeText(context, NetworkUtils.errorResponse(error), Toast.LENGTH_SHORT).show()
                }
            )
            VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest)
        }


        @JvmStatic
        fun getTotalWorkoutTime(
            context: Activity,
            url: String,
            onComplete: ( time: Int) -> Unit = {}
        ){
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET, url, null,
                { response ->
                    val time = response.getJSONObject("data").getInt("totalWorkoutTime")
                    onComplete(time)
                },
                { error ->
                    Toast.makeText(context, NetworkUtils.errorResponse(error), Toast.LENGTH_SHORT).show()
                }
            )
            VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest)
        }


    }
}