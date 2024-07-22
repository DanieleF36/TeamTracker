package it.application.team_tracker.model.remoteDataSource.entities

import com.google.firebase.Timestamp



data class Attachment(
    override val id: String,
    val url: String,
    val creator: String,
    val date: Timestamp,
    val taskId: String,
    val userId: String
): Entity
