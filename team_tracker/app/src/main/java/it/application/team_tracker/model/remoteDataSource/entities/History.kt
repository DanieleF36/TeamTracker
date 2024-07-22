package it.application.team_tracker.model.remoteDataSource.entities

import com.google.firebase.Timestamp

data class History(
    override val id: String,
    val type: String,
    val message: String,
    val date: Timestamp,
    val taskId: String
): Entity
