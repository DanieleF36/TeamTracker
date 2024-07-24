package it.application.team_tracker.model.entities

import android.net.Uri
import java.util.Calendar

data class Team(
    val id: String,
    val name: String,
    val description: String?,
    val invitationLink: String?,
    val photo: Uri?,
    val creator: String,
    val creationDate: Calendar,
    val deliveryDate: Calendar?,
    /** Map<idUser, role> */
    val teamMembers: Map<String, String>,
    val feedbacks: List<Feedback>
)
