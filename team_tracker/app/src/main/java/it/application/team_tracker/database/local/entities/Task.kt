package it.application.team_tracker.database.local.entities

import android.icu.util.Calendar
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task")
data class Task (
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "due_date")
    val dueDate: Calendar,
    @ColumnInfo(name = "creation_date")
    val creationDate: Calendar,
    @ColumnInfo(name = "closing_date")
    val closingDate: Calendar,
    @ColumnInfo(name = "state")
    val state: Int,
    @ColumnInfo(name = "creator")
    val creator: String,
    @ColumnInfo(name = "closer")
    val closer: String?,
    @ColumnInfo(name = "priority")
    val priority: Int,
    @ColumnInfo(name = "team_id")
    val teamId: String,
    val tags: List<String>
)