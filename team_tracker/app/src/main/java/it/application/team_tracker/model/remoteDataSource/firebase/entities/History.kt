package it.application.team_tracker.model.remoteDataSource.firebase.entities

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName

data class History(
    override var id: String,
    val type: String,
    val message: String,
    val date: Timestamp,
    @PropertyName("task_id")
    val taskId: String
): Entity
