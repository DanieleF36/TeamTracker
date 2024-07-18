package it.application.team_tracker.model.remoteDataSource.entities

import com.google.firebase.Timestamp


data class Team (
    val id: String,
    val name: String,
    val description: String?,
    val invitationLink: String?,
    val photo: String?,
    val creator: String,
    val creationDate: Timestamp,
    val deliveryDate: Timestamp?,
    /** Map<idUser, role> */
    val teamMembers: List<Map<String, String>>,
    /**
       Map<comment || evaluation || userId, String>
       this map will have always 3 element: comment, evaluation, teamId
     */
    val feedbacks: List<Map<String, String>>
)