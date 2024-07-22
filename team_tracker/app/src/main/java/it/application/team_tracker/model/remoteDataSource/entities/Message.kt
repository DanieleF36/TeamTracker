package it.application.team_tracker.model.remoteDataSource.entities

import com.google.firebase.Timestamp

data class Message(
    override val id: String,
    val message: String,
    val date: Timestamp,
    val teamId: String?,
    val sender: String,
    val receiver: String?
): Entity
