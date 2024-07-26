package it.application.team_tracker.model.remoteDataSource.firebase.entities

data class Page(
    override val id: String,
    val name: String,
    val position: Int,
    val teamId: String
): Entity
