package it.application.team_tracker.model.entities

import android.net.Uri
data class User(
    val id: String,
    val nickname: String,
    val fullName: String,
    val email: String,
    val location: String,
    val phone: String,
    val description: String,
    val photo: Uri?,
    val teamsId: List<String>?,
    val tasksId: List<String>?,
    /** this is the ids of users with this user has started a private chat */
    val privateChatsId: List<String>?
)