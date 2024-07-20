package it.application.team_tracker.model.entities

import java.util.Calendar

data class Comment(
    val id: String,
    val message: String,
    val date: Calendar,
    val taskId: String,
    val userId: String
)
