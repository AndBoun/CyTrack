package com.example.CyTrack.Social.Friends

import java.io.Serializable

data class Friend(
    val firstName: String,
    val username: String,
    val userID: Int,
    val friendID: Int
) : Serializable