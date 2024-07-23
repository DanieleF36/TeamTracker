package it.application.team_tracker.model.remoteDataSource.entities

import com.google.firebase.Timestamp


data class Task(
    override val id: String,
    val name: String,
    val description: String,
    val dueDate: Timestamp,
    val creationDate: Timestamp,
    val closingDate: Timestamp,
    val state: Int,
    val creator: String,
    val closer: String?,
    val priority: Int,
    val teamId: String,
    val timeSpent: Float,
    /** Map<idUser, role> */
    val membersAndRole: Map<String, String>,
    /** It contains all the ids of he members */
    val members: List<String>,
    val tags: List<String>
): Entity
