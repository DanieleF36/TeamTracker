package it.application.team_tracker.model.remoteDataSource.firebase.entities

import com.google.firebase.firestore.PropertyName

data class Tag(
    override var id: String,
    val name: String,
    /** idTask */
    @PropertyName("task_id")
    val tasksId: List<String>
): Entity
