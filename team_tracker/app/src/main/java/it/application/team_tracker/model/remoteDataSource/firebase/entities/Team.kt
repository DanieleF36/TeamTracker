package it.application.team_tracker.model.remoteDataSource.firebase.entities

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName


data class Team (
    override var id: String,
    val name: String,
    val description: String?,
    @PropertyName("invitation_link")
    val invitationLink: String?,
    val photo: String?,
    val creator: String,
    @PropertyName("creation_date")
    val creationDate: Timestamp,
    @PropertyName("delivery_date")
    val deliveryDate: Timestamp?,
    /** Map<idUser, role> */
    @PropertyName("members_role")
    val membersAndRole: Map<String, String>,
    val members: List<String>,
    /**
       Map<comment || evaluation || userId, String>
       this map will have always 3 element: comment, evaluation, userId
     */
    val feedbacks: List<Map<String, String>>
): Entity