package it.application.team_tracker.model.remoteDataSource.firebase.entities

import com.google.firebase.firestore.PropertyName

data class Page(
    override var id: String,
    val name: String,
    val position: Int,
    @PropertyName("team_id")
    val teamId: String
): Entity
