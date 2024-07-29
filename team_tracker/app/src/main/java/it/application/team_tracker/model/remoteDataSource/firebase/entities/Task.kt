package it.application.team_tracker.model.remoteDataSource.firebase.entities

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName


data class Task(
    override var id: String,
    val name: String,
    val description: String,
    val dueDate: Timestamp,
    @PropertyName("creation_date")
    val creationDate: Timestamp,
    @PropertyName("closing_date")
    val closingDate: Timestamp,
    val state: Int,
    val creator: String,
    val closer: String?,
    val priority: Int,
    @PropertyName("team_id")
    val teamId: String,
    @PropertyName("time_spent")
    val timeSpent: Float,
    /** Map<idUser, role> */
    @PropertyName("members_role")
    val membersAndRole: Map<String, String>,
    /** It contains all the ids of he members */
    val members: List<String>,
    /** ids of tags */
    val tags: List<String>
): Entity
