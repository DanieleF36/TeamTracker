package it.application.team_tracker.database.local.entities

import android.icu.util.Calendar
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "message")
data class Message (
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "message")
    val message: String,
    @ColumnInfo(name = "date")
    val date: Calendar,
    @ColumnInfo(name = "team_id")
    val teamId: String?,
    @ColumnInfo(name = "sender")
    val sender: String,
    @ColumnInfo(name = "receiver")
    val receiver: String?
)