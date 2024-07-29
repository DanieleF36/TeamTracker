package it.application.team_tracker.model.remoteDataSource.firebase.entities

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName

data class Message(
    override var id: String,
    val message: String,
    val date: Timestamp,
    @PropertyName("team_id")
    val teamId: String?,
    val sender: String,
    val receiver: String?,
    /**
     * This is used for team message and if there is some userId this is his lastReadMessage
     */
    @PropertyName("is_last_read")
    val isLastRead: List<String>
): Entity
