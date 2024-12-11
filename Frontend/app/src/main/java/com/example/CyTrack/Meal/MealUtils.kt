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
            urlin: String,
            time: String,
            date: String,
            onComplete: (nutrientsum: NutrientSummary) -> Unit = {}
        ) {
            Log.d("Nutrient Summary URL Checker", "${urlin}")
            val url = urlin + "/nutrients/${date}"
            Log.d("Nutrient Summary URL Checker", "${url}")
            var totalCalories = 0
            var totalCarbs = 0
            var totalProtein = 0
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET, url, null,
                { response ->
                    val summary = response.getJSONObject("data")
                    Log.d("Nutrient Summary JSON Content Checker", summary.toString())
                    totalCalories = summary.getInt("totalCalories")
                    totalCarbs = summary.getInt("totalCarbs")
                    totalProtein = summary.getInt("totalProtein")
                    onComplete(NutrientSummary(totalCalories, totalCarbs, totalProtein, time, date))
                },
                { error ->
                    Log.d("MyMeals", "Error: $error")
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
            mealList: MutableList<MealEntry>,
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
                    else {
                        val name = inputMealName.text.toString()
                        val carbs = inputMealCarbs.text.toString()
                        val protein = inputMealProtein.text.toString()
                        val calories = inputMealCalories.text.toString()

                        val meal = MealEntry(0, name, calories.toInt(), carbs.toInt(), protein.toInt(), time, date)
                        postMeal("${url}/meal", mealList, meal, context)
                    }
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
            mealList: MutableList<MealEntry>,
            url: String,
            time: String,
            date: String,
            mealId: Int,
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
                    Log.d("Delete Meal URL Checker", "${url}/meal/${mealId.toString()}")
                    deleteMeal("${url}/meal/${mealId.toString()}", mealList, context)
                    dialog.dismiss()
                }
                .setPositiveButton("Modify") { dialog: DialogInterface, which: Int ->
                    // Handle the button click
                    val inputMealName = dialogView.findViewById<EditText>(R.id.inputNewMealName)
                    val inputMealCalories = dialogView.findViewById<EditText>(R.id.inputNewMealCalories)
                    val inputMealProtein = dialogView.findViewById<EditText>(R.id.inputNewMealProtein)
                    val inputMealCarbs = dialogView.findViewById<EditText>(R.id.inputNewMealCarbs)

                    if (inputMealName.text.toString().isEmpty() ||
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
                    else {
                        val name = inputMealName.text.toString()
                        val carbs = inputMealCarbs.text.toString()
                        val protein = inputMealProtein.text.toString()
                        val calories = inputMealCalories.text.toString()

                        val meal = MealEntry(mealId, name, calories.toInt(), carbs.toInt(), protein.toInt(), time, date)
                        modifyMeal(meal, mealList, "${url}/meal", context)
                    }
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
            mealList: MutableList<MealEntry>,
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
            Log.d("Post Meal URL Checker", "${URL}")
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
            mealList: MutableList<MealEntry>,
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


        private fun modifyMeal(
            meal: MealEntry,
            mealList: MutableList<MealEntry>,
            URL: String,
            context: Activity
        ) {
            Log.d("Post Meal URL Checker", "${URL}")
            val modifyURL: String = URL + "/" + meal.id
            Log.d("Post Meal URL Checker", "${modifyURL}")
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
