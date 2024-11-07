package com.example.CyTrack.Leaderboard.main

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.example.CyTrack.Badges.BadgeObject
import com.example.CyTrack.Social.Messaging.DirectMessage
import org.json.JSONException

import com.example.CyTrack.Utilities.NetworkUtils // Connection Imports
import com.example.CyTrack.Utilities.User
import com.example.CyTrack.Utilities.VolleySingleton
import org.json.JSONArray
import org.json.JSONObject


class TimeboardUtils(){

    companion object {
        @JvmStatic
        fun getBoard(
            msg: String,
            TimeEntryList: MutableList<TimeBoardEntry>
        ) {
            try {
                val data = JSONArray(msg)
                for (i in 0 until data.length()) {
                    val entry = data.getJSONObject(i)

                    TimeEntryList.add(
                        TimeBoardEntry(
                            entry.getLong("userID"),
                            entry.getString("username"),
                            entry.getInt("totalTime"),
                        )
                    )
                    Log.d("Tag","${entry.getLong("userID")} ID");
                    //Log.d("Tag","${TimeEntryList} ID");
                }

            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }
}