package it.application.team_tracker.model.remoteDataSource.entities

import com.google.firebase.firestore.PropertyName

data class User(
    override val id: String,
    val nickname: String,
    @get:PropertyName("fullname")
    val fullName: String,
    val email: String,
    val location: String,
    val phone: String,
    val description: String,
    val photo: String?,
    /** Map<idTeam, role> */
    @get:PropertyName("team_members")
    val teamMembers: Map<String, String>,
    /**
       Map<comment || evaluation || teamId, String>
       this map will have always 3 element: comment, evaluation, teamId
     */
    val feedbacks: Map<String, String>,
    /** Map<idUser, role> */
    @get:PropertyName("task_members")
    val taskMembers: Map<String, String>,
    @get:PropertyName("favorite_team")
    val favoriteTeam: List<String>
):Entity
