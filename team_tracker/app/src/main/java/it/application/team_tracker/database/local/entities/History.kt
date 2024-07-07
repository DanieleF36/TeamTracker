package it.application.team_tracker.database.local.entities

import android.icu.util.Calendar
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
data class History (
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "type")
    val type: String,
    @ColumnInfo(name = "message")
    val message: String,
    @ColumnInfo(name = "date")
    val date: Calendar,
    @ColumnInfo(name = "task_id")
    val taskId: String
)