package it.application.team_tracker.database.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "feedback")
data class Feedback (
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "team_id")
    val teamId: String,
    @ColumnInfo(name = "user_id")
    val userId: String,
    @ColumnInfo(name = "comment")
    val comment: String,
    @ColumnInfo(name = "evaluation")
    val evaluation: Int
)
