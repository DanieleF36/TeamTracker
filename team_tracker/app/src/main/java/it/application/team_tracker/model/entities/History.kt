package it.application.team_tracker.model.entities

import java.util.Calendar

data class History(
    val id: String,
    val type: String,
    val message: String,
    val date: Calendar,
    val taskId: String
)
