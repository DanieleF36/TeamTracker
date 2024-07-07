package it.application.team_tracker.database.local.entities

import android.icu.util.Calendar
import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "team")
data class Team (
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "description")
    val description: String?,
    @ColumnInfo(name = "invitation_link")
    val invitationLink: String?,
    @ColumnInfo(name = "photo")
    val photo: Uri?,
    @ColumnInfo(name = "creator")
    val creator: String,
    @ColumnInfo(name = "creation_date")
    val creationDate: Calendar,
    @ColumnInfo(name = "delivery_date")
    val deliveryDate: Calendar?,
    val teamMember: List<String>
)