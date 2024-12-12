package com.example.CyTrack.Social.Messaging

data class MessageListData (
    var chatType: String = "",
    var senderUsername: String = "",
    var receiverUsername: String = "",
    var groupName: String = "",
    var content: String = "",
    var time: String = "",
    val groupOrReceiverID: Int,
    val userID: Int
)