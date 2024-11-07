package com.example.CyTrack.Leaderboard.TimeEntryBoard

import android.util.Log
import org.json.JSONException

import org.json.JSONArray


class TimeboardUtils(){

    companion object {
        @JvmStatic
        fun getBoard(
            msg: String,
            TimeEntryList: MutableList<TimeBoardEntry>,
            Temp: MutableList<TimeBoardEntry>
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
                    Log.d("Tag","${entry.getLong("userID")} ID");
                    //Log.d("Tag","${TimeEntryList} ID");
                }

            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }
}