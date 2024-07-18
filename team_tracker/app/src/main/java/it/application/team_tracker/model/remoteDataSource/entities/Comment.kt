package it.application.team_tracker.model.remoteDataSource.entities

import com.google.firebase.Timestamp

data class Comment(
    val id: String,
    val message: String,
    val date: Timestamp,
    val taskId: String,
    val userId: String
)
