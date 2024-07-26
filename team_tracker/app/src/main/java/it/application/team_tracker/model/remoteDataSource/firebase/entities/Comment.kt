package it.application.team_tracker.model.remoteDataSource.firebase.entities

import com.google.firebase.Timestamp

data class Comment(
    override val id: String,
    val message: String,
    val date: Timestamp,
    val taskId: String,
    val userId: String
): Entity
