package it.application.team_tracker.model.remoteDataSource.firebase.entities

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName

data class Comment(
    override var id: String,
    val message: String,
    val date: Timestamp,
    @PropertyName("task_id")
    val taskId: String,
    @PropertyName("user_id")
    val userId: String
): Entity
