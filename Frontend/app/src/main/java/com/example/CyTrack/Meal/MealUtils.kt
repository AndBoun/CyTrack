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
            val dialogView: View = inflater.inflate(R.layout.meal_add_popup, null)

            // Create the AlertDialog
            val builder = AlertDialog.Builder(context)
            builder.setView(dialogView)
                .setTitle("Add Meal")
                .setPositiveButton("Add") { dialog: DialogInterface, which: Int ->
                    // Handle the add button click
                    val inputMealName = dialogView.findViewById<EditText>(R.id.inputMealName)
                    val inputMealCalories = dialogView.findViewById<EditText>(R.id.inputMealCalories)
                    val inputMealProtein = dialogView.findViewById<EditText>(R.id.inputMealProtein)
                    val inputMealCarbs = dialogView.findViewById<EditText>(R.id.inputMealCarbs)

                    if (inputMealName.text.toString().isEmpty() ||
                        inputMealCalories.text.toString().isEmpty() ||
                        inputMealProtein.text.toString().isEmpty() ||
                        inputMealCarbs.text.toString().isEmpty()
                    ) {
                        Toast.makeText(
                            context,
                            "Please fill out all fields",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@setPositiveButton
                    }


                    val carbs = inputMealCarbs.text.toString()
                    val protein = inputMealProtein.text.toString()
                    val calories = inputMealCalories.text.toString()

                    val meal = MealEntry("Placeholder", calories.toInt(), carbs.toInt(), protein.toInt(), time, date)
                    postMeal(url, meal, context)
                    dialog.dismiss()
                }
                .setNegativeButton(
                    "Cancel"
                ) { dialog: DialogInterface, which: Int -> dialog.dismiss() }
                .create()
                .show()
        }

        fun showModifyMeal(
            user: User,
            url: String,
            time: String,
            date: String,
            context: Activity
        ) {
            // Inflate the dialog layout
            val inflater = LayoutInflater.from(context)
            val dialogView: View = inflater.inflate(R.layout.meal_modify_popup, null)

            // Create the AlertDialog
            val builder = AlertDialog.Builder(context)
            builder.setView(dialogView)
                .setTitle("Edit or Delete Meal")
                .setNegativeButton("Delete") { dialog: DialogInterface, which: Int ->
                    // Handle the button click
                    val inputMealID = dialogView.findViewById<EditText>(R.id.inputMealID)
                    val MealID = inputMealID.text.toString()
                    if (inputMealID.text.toString().isEmpty())
                    {
                        Toast.makeText(
                            context,
                            "Failed: Please enter the Meal ID",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    deleteMeal("${url}/${MealID}", context)
                    dialog.dismiss()
                }
                .setPositiveButton("Modify") { dialog: DialogInterface, which: Int ->
                    // Handle the button click
                    val inputMealID = dialogView.findViewById<EditText>(R.id.inputMealID)
                    val inputMealName = dialogView.findViewById<EditText>(R.id.inputNewMealName)
                    val inputMealCalories = dialogView.findViewById<EditText>(R.id.inputNewMealCalories)
                    val inputMealProtein = dialogView.findViewById<EditText>(R.id.inputNewMealProtein)
                    val inputMealCarbs = dialogView.findViewById<EditText>(R.id.inputNewMealCarbs)

                    if (inputMealID.text.toString().isEmpty() ||
                        inputMealName.text.toString().isEmpty() ||
                        inputMealCalories.text.toString().isEmpty() ||
                        inputMealProtein.text.toString().isEmpty() ||
                        inputMealCarbs.text.toString().isEmpty()
                    ) {
                        Toast.makeText(
                            context,
                            "Failed: Please fill out all fields",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@setPositiveButton
                    }
                    val name = inputMealName.text.toString()
                    val carbs = inputMealCarbs.text.toString()
                    val protein = inputMealProtein.text.toString()
                    val calories = inputMealCalories.text.toString()

                    val meal = MealEntry(name, calories.toInt(), carbs.toInt(), protein.toInt(), time, date)
                    postMeal(url, meal, context)
                    dialog.dismiss()
                }
                .setNeutralButton(
                    "Cancel"
                ) { dialog: DialogInterface, which: Int -> dialog.dismiss() }
                .create()
                .show()
        }
        /**
         * Posts a new meal to the server.
         *
         * @param URL
         * @param meal
         * @param context
         */
        fun postMeal(
            URL: String,
            meal: MealEntry,
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
                        Toast.makeText(context, "Meal added", Toast.LENGTH_SHORT).show()
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
        /**
         * Posts a new meal to the server.
         *
         * @param URL
         * @param context
         */
        fun deleteMeal(
            URL: String,
            context: Activity
        ) {
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.DELETE, URL, null,
                { response: JSONObject? ->
                    Toast.makeText(context, "Meal deleted", Toast.LENGTH_SHORT).show()
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


        private fun modifyWorkout(
            meal: MealEntry,
            URL: String,
            context: Activity
        ) {
            val modifyURL: String = URL + "/" + meal.id
            val inputs = JSONObject().apply {
                put("mealName", meal.name)
                put("calories", meal.calories)
                put("protein", meal.protein)
                put("carbs", meal.carbs)
                put("time", meal.time)
                put("date", meal.date)
            }

            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.PUT, modifyURL, inputs,
                { response: JSONObject? ->
                    Toast.makeText(context, "Meal modified", Toast.LENGTH_SHORT).show()
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
