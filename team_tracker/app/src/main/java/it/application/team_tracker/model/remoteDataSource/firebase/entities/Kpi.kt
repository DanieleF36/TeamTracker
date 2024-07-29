package it.application.team_tracker.model.remoteDataSource.firebase.entities

import com.google.firebase.firestore.PropertyName

data class Kpi(
    override var id: String,
    val name: String,
    val value: Int,
    @PropertyName("user_id")
    val userId: String
): Entity
