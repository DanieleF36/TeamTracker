package it.application.team_tracker.model.entities

import android.net.Uri
import java.util.Calendar

data class Attachment(
    val id: String,
    var url: Uri,
    val creator: String,
    val date: Calendar,
    val taskId: String,
    val userId: String
)
