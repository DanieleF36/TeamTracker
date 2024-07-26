package it.application.team_tracker.model.remoteDataSource.firebase.entities

import com.google.firebase.Timestamp

data class Message(
    override val id: String,
    val message: String,
    val date: Timestamp,
    val teamId: String?,
    val sender: String,
    val receiver: String?,
    /**
     * This is used for team message and if there is some userId this is his lastReadMessage
     */
    val isLastRead: List<String>
): Entity
