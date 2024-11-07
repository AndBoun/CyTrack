package com.example.CyTrack.Leaderboard.StreakBoard

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import org.json.JSONException

import com.example.CyTrack.Utilities.NetworkUtils // Connection Imports
import com.example.CyTrack.Utilities.User
import com.example.CyTrack.Utilities.VolleySingleton

class LeaderboardUtils(){

    companion object {
        @JvmStatic
        fun getListOfUsers(
            context: Activity,
            userList: MutableList<User>,
            url: String,
            arrName: String,
        ) {

            val jsonObjectRequest = JsonArrayRequest(
                Request.Method.GET, url, null,
                { response ->
                    try {
                        for (i in 0 until response.length()) {
                            val user = response.getJSONObject(i)
                            try {
                                userList.add(
                                    User(
                                        user.getInt("userID"),
                                        user.getString("username"),
                                        user.getString("firstName"),
                                        user.getString("lastName"),
                                        user.getInt("age"),
                                        user.getString("gender"),
                                        user.getInt("currentStreak")
                                    )
                                )
                                Log.d("Tag","${user.getString("firstName")}");
                            }
                            catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                        userList.sortBy { it.streak }
                        Log.d("Tag", "${userList}")

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                { error ->
                    Toast.makeText(context, NetworkUtils.errorResponse(error), Toast.LENGTH_LONG).show()
                }

            )
            VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest)
        }
    }
}