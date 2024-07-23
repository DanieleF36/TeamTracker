package it.application.team_tracker.model.entities

import java.util.Calendar
data class Task(
    val id: String,
    val name: String,
    val description: String,
    val dueDate: Calendar,
    val creationDate: Calendar,
    val closingDate: Calendar,
    val state: Int,
    val creator: String,
    val closer: String?,
    val priority: Int,
    val teamId: String,
    val timeSpent: Float,
    /** Map<idUser, role> */
    val taskMembers: Map<String, String>,
    val tags: List<String>
)
