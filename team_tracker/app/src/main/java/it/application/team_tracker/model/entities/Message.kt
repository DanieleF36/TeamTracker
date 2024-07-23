package it.application.team_tracker.model.entities

import java.util.Calendar

data class Message(
    val id: String,
    val message: String,
    val date: Calendar,
    val teamId: String?,
    val sender: String,
    val receiver: String?,
    val isLastRead: Boolean
)