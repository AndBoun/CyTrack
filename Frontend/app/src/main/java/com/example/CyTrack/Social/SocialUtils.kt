package com.example.CyTrack.Social

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.example.CyTrack.Utilities.NetworkUtils
import com.example.CyTrack.Utilities.User
import com.example.CyTrack.Utilities.VolleySingleton
import org.json.JSONException

class SocialUtils {

    companion object {
        @JvmStatic
        fun messageUserScreen(user: User, recipient: User, context: Activity) {
            val intent = Intent(context, DirectMessage::class.java).apply {
                putExtra("user", user)
                putExtra("recipient", recipient)
            }
            startActivity(context, intent, null)
        }


        @JvmStatic
        fun getListOfUsers(
            context: Activity,
            userList: MutableList<User>,
            url: String,
            arrName: String,
        ) {
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.POST, url, null,
                { response ->
                    try {
                        val message = response.getString("message")
                        val data = response.getJSONObject("data").getJSONArray(arrName)

                        for (i in 0 until data.length()) {
                            val user = data.getJSONObject(i)
                            userList.add(
                                User(
                                    user.getInt("userID"),
                                    user.getString("username"),
                                    user.getString("firstName"),
                                    user.getString("lastName"),
                                    user.getInt("age"),
                                    user.getString("gender"),
                                    user.getInt("streak")
                                )
                            )
                        }
                        userList.sortBy { it.firstName }
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