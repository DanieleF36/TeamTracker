package it.application.team_tracker.model.remoteDataSource.firebase.entities

data class Kpi(
    override var id: String,
    val name: String,
    val value: Int,
    val userId: String
): Entity
