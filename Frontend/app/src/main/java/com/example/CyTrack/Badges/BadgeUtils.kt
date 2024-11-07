package com.example.CyTrack.Badges

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.compose.material3.Badge
import androidx.core.content.ContextCompat.startActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONException

import com.example.CyTrack.Leaderboard.main.LeaderboardActivity // Main Kotlin UI Framework

import com.example.CyTrack.Utilities.NetworkUtils // Connection Imports
import com.example.CyTrack.Utilities.User
import com.example.CyTrack.Utilities.VolleySingleton

class BadgeUtils(){

    companion object {
        @JvmStatic
        fun getListOfBadges(
            context: Activity,
            BadgeList: MutableList<BadgeObject>,
            usermain: User,
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
                            val user = data.getJSONObject(i)
                            val id = user.getInt("userID")
                            if (id == usermain.id) {
                                BadgeList.add(
                                    BadgeObject(
                                        user.getString("username"),
                                        user.getString("firstName"),
                                    )
                                )
                            }
                        }
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