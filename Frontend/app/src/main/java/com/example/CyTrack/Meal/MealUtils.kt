package com.example.CyTrack.Meal
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

        /**
         * Shows a dialog to add a new workout.
         */
        @JvmStatic

        fun showAddMeal(
            user: User,
            url: String,
            time: String,
            date: String,
            context: Activity
        ) {
            // Inflate the dialog layout
            val inflater = LayoutInflater.from(context)
            val dialogView: View = inflater.inflate(R.layout.workouts_add_workout_dialog, null)

            // Create the AlertDialog
            val builder = AlertDialog.Builder(context)
            builder.setView(dialogView)
                .setTitle("Add Meal")
                .setPositiveButton("Add") { dialog: DialogInterface, which: Int ->
                    // Handle the add button click
                    val inputExerciseType = dialogView.findViewById<EditText>(R.id.inputExerciseType)
                    val inputWorkoutDuration = dialogView.findViewById<EditText>(R.id.inputWorkoutDuration2)
                    val inputCalories = dialogView.findViewById<EditText>(R.id.inputCalories)

                    if (inputExerciseType.text.toString().isEmpty() ||
                        inputWorkoutDuration.text.toString().isEmpty() ||
                        inputCalories.text.toString().isEmpty()
                    ) {
                        Toast.makeText(
                            context,
                            "Please fill out all fields",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@setPositiveButton
                    }


                    val carbs = inputExerciseType.text.toString()
                    val protein = inputWorkoutDuration.text.toString()
                    val calories = inputCalories.text.toString()

                    val meal = MealEntry("Placeholder", calories.toInt(), carbs.toInt(), protein.toInt(), time, date)
                    postMeal(user, url, meal, date, context)
                    dialog.dismiss()
                }
                .setNegativeButton(
                    "Cancel"
                ) { dialog: DialogInterface, which: Int -> dialog.dismiss() }
                .create()
                .show()
        }
        /**
         * Posts a new workout to the server.
         *
         * @param exerciseType The type of exercise.
         * @param duration     The duration of the workout.
         * @param calories     The calories burned during the workout.
         * @param date         The date of the workout.
         */
        fun postMeal(
            user: User,
            URL: String,
            meal: MealEntry,
            date: String,
            context: Activity,
        ) {
            val inputs = JSONObject().apply {
                put("mealName", meal.name)
                put("calories", meal.calories)
                put("protein", meal.protein)
                put("carbs", meal.carbs)
                put("time", meal.time)
                put("date", meal.date)
            }

            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.POST,
                URL,
                inputs,
                { response: JSONObject ->
                    try {
                        val data = response.getJSONObject("data")
                        val workoutID = data.getInt("workoutID")
                        Toast.makeText(context, "Workout added", Toast.LENGTH_SHORT).show()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                { error: VolleyError? ->
                    Toast.makeText(
                        context,
                        NetworkUtils.errorResponse(error),
                        Toast.LENGTH_SHORT
                    ).show()
                })
            VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest)
        }
    }
}
