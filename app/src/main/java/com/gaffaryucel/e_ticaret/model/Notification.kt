package com.gaffaryucel.e_ticaret.model

data class Notification(
    val to: String,
    val notification: NotificationBody,
)
data class NotificationBody(
    val title: String,
    val body: String,
)
