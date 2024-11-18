package com.example.CyTrack.Social.Friends

import java.io.Serializable

/**
 * Data class representing a Friend.
 *
 * @property firstName The first name of the friend.
 * @property username The username of the friend.
 * @property userID The user ID of the friend.
 * @property friendID The friend ID.
 */
data class Friend(
    val firstName: String,
    val username: String,
    val userID: Int,
    val friendID: Int
) : Serializable