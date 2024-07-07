package it.application.team_tracker.database.local.entities

import android.icu.util.Calendar
import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "attachement")
data class Attachement (
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "url")
    val url: Uri,
    @ColumnInfo(name = "creator")
    val creator: String,
    @ColumnInfo(name = "date")
    val date: Calendar,
    @ColumnInfo(name = "task_id")
    val task_id: String,
    @ColumnInfo(name = "user_id")
    val user_id: String
)