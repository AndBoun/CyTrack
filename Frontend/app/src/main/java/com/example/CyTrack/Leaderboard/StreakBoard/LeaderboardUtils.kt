package com.example.CyTrack.Leaderboard.StreakBoard

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.example.CyTrack.Leaderboard.TimeEntryBoard.TimeBoardEntry
import org.json.JSONException

import com.example.CyTrack.Utilities.NetworkUtils // Connection Imports
import com.example.CyTrack.Utilities.User
import com.example.CyTrack.Utilities.VolleySingleton
import org.json.JSONArray

/**
 * Utility class for handling leaderboard-related operations.
 */
class LeaderboardUtils() {

    /**
     * Companion object to hold static methods and properties for LeaderboardUtils.
     */
    companion object {
        /**
         * Parses a JSON string to extract user information and populate the provided user list.
         *
         * @param msg The JSON string containing user data.
         * @param userList The list to be populated with User objects.
         */
        @JvmStatic
        fun getListOfUsers(
            msg: String,
            userList: MutableList<User>,
        ) {
            try {
                val data = JSONArray(msg)
                userList.clear()
                for (i in 0 until data.length()) {
                    val user = data.getJSONObject(i)
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
                        userList.sortBy { it.streak }
                        Log.d("Tag", "${userList}")
                        Log.d("Tag", "${user.getString("firstName")}");
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }
}