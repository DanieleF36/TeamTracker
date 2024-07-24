package it.application.team_tracker.model.entities

data class Feedback(
    val id: String,
    val comment: String,
    val evaluation: Int,
    val userId: String
)
