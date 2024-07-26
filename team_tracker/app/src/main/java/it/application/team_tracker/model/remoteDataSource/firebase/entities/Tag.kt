package it.application.team_tracker.model.remoteDataSource.firebase.entities

data class Tag(
    override val id: String,
    val name: String,
    /** idTask */
    val tasksId: List<String>
): Entity
