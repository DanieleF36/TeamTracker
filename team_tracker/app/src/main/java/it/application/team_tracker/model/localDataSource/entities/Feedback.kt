package it.application.team_tracker.model.localDataSource.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = [ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["user_id"], onDelete = ForeignKey.CASCADE),
                       ForeignKey(entity = Team::class, parentColumns = ["id"], childColumns = ["team_id"], onDelete = ForeignKey.CASCADE)],
        tableName = "feedback")
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
