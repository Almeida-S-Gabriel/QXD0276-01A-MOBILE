package com.example.msgapp.model

data class Message(
    val id: String = "",
    val senderId: String = "",
    val senderName: String ="",
    val texto: String = "",
    val timestamp: Long = 0L
)