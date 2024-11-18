package com.example.CyTrack.Leaderboard.TimeEntryBoard

import android.util.Log
import org.json.JSONException

import org.json.JSONArray


/**
 * Utility class for handling operations related to the Timeboard.
 */
class TimeboardUtils() {

    /**
     * Companion object to hold static methods and properties.
     */
    companion object {
        /**
         * Parses a JSON string and populates the provided list with TimeBoardEntry objects.
         *
         * @param msg The JSON string containing the timeboard data.
         * @param TimeEntryList The list to be populated with TimeBoardEntry objects.
         */
        @JvmStatic
        fun getBoard(
            msg: String,
            TimeEntryList: MutableList<TimeBoardEntry>,
        ) {
            try {
                val data = JSONArray(msg)
                TimeEntryList.clear()
                for (i in 0 until data.length()) {
                    val entry = data.getJSONObject(i)
                    TimeEntryList.add(
                        TimeBoardEntry(
                            entry.getLong("userID"),
                            entry.getString("username"),
                            entry.getInt("totalTime"),
                        )
                    )
                    Log.d("Tag", "${entry.getLong("userID")} ID");
                    //Log.d("Tag","${TimeEntryList} ID");
                }

            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }
}