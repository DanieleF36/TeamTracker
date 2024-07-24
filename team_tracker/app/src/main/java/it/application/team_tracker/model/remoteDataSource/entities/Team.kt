package it.application.team_tracker.model.remoteDataSource.entities

import com.google.firebase.Timestamp


data class Team (
    override val id: String,
    val name: String,
    val description: String?,
    val invitationLink: String?,
    val photo: String?,
    val creator: String,
    val creationDate: Timestamp,
    val deliveryDate: Timestamp?,
    /** Map<idUser, role> */
    val membersAndRole: Map<String, String>,
    val members: List<String>,
    /**
       Map<comment || evaluation || userId, String>
       this map will have always 3 element: comment, evaluation, userId
     */
    val feedbacks: List<Map<String, String>>
):Entity