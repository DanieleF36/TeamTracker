package it.application.team_tracker.model.remoteDataSource.entities

data class Tag(
    val id: String,
    val name: String,
    /** idTask */
    val tags: List<String>
)
